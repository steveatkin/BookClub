/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.ibm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;

import com.ibm.json.java.JSONObject;
import com.ibm.watson.WatsonTranslate;

import twitter4j.*;
import com.ibm.alchemy.*;

public class TwitterAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(TwitterAsyncService.class);
	private AsyncContext ac;

	public TwitterAsyncService(AsyncContext context) {
		this.ac = context;
	}


	private String translate(WatsonTranslate watson, String message) {
    	return watson.translate(message);
    }


	@Override
	  public void run() {
		String bookTitle = ac.getRequest().getParameter("title");
		String bookAuthor = ac.getRequest().getParameter("author");
		boolean translate = Boolean.parseBoolean(ac.getRequest().getParameter("enable"));

		logger.debug("Requested book title {} and author {}", bookTitle, bookAuthor);

		Query query = new Query(bookTitle + " " + bookAuthor);
	    query.setResultType(Query.RECENT);
	    	
	    Twitter twitter = TwitterFactory.getSingleton();


		try {
			PrintWriter writer = ac.getResponse().getWriter();
			Locale locale = ac.getRequest().getLocale();
			WatsonTranslate watson = new WatsonTranslate(locale);

			// Just get the first page of results to avoid exceeding the Twitter rate limit
	    	QueryResult result = twitter.search(query);

			Alchemy alchemy = new Alchemy();
	        	
	    	List<Status> tweets = result.getTweets();
	        	
	    	logger.debug("Current tweets {}", tweets.toString());

			for(Status tweetMessage : tweets) {
				JSONObject json = new JSONObject();
				JSONObject tweet = new JSONObject();
				// We need to put the tweet and link into an inner object
				// so that we can use a special formatter in bootstrap table

				json.put("screenName", tweetMessage.getUser().getScreenName());

				if(translate) {
					String message = watson.translate(tweetMessage.getText());
	    			tweet.put("message", message);
					json.put("sentiment", alchemy.getSentiment(message));
				}
				else {
					tweet.put("message", tweetMessage.getText());
					json.put("sentiment", alchemy.getSentiment(tweetMessage.getText()));
				}

				String url= "https://twitter.com/" + tweetMessage.getUser().getScreenName() 
    			+ "/status/" + tweetMessage.getId();

				tweet.put("link", url);

				json.put("tweet", tweet);

    			writer.write(("data: " + json.toString() + "\n\n"));
    			writer.flush();
			}

			writer.write(("event: finished\n"));
			writer.write(("data: \n\n"));
			writer.flush();
			writer.close();
		}
		catch(TwitterException e) {
	    		logger.error("Twitter Error {}",e.getMessage());
	    }
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		catch(NullPointerException e) {
			logger.error("Exception Twitter Async Service {}", e.getMessage());
		}
		finally {
			ac.complete();
		}
	}

}
