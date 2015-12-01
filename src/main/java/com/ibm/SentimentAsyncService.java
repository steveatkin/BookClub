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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;

import com.ibm.json.java.JSONObject;
import com.ibm.twitter.Sentiment;
import com.ibm.twitter.TwitterInsights;

public class SentimentAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SentimentAsyncService.class);
	private AsyncContext ac;

	public SentimentAsyncService(AsyncContext context) {
		this.ac = context;
	}

	@Override
	  public void run() {
		String bookTitle = ac.getRequest().getParameter("title");
		String bookAuthor = ac.getRequest().getParameter("author");

		logger.debug("Requested book title {} and author {}", bookTitle, bookAuthor);

		try {
			PrintWriter writer = ac.getResponse().getWriter();

			TwitterInsights twitter = new TwitterInsights();
			JSONObject json = new JSONObject();

			Sentiment sentiment = twitter.getSentimentCount(bookTitle, bookAuthor, "positive");
			logger.debug("Positive sentiment {}", sentiment.toString());
			json.put("sentiment", sentiment.getPolarity());
			json.put("count", sentiment.getCount());
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();

			sentiment = twitter.getSentimentCount(bookTitle, bookAuthor, "negative");
			logger.debug("Negative sentiment {}", sentiment.toString());
			json.put("sentiment", sentiment.getPolarity());
			json.put("count", sentiment.getCount());
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();

			sentiment = twitter.getSentimentCount(bookTitle, bookAuthor, "neutral");
			logger.debug("Neutral sentiment {}", sentiment.toString());
			json.put("sentiment", sentiment.getPolarity());
			json.put("count", sentiment.getCount());
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();

			writer.write(("event: finished\n"));
			writer.write(("data: \n\n"));
			writer.flush();
			writer.close();
		}
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		catch(NullPointerException e) {
			logger.error("Exception Sentiment Async Service {}", e.getMessage());
		}
		finally {
			ac.complete();
		}
	}

}
