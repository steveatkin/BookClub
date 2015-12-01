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
import java.util.Locale;

import javax.servlet.AsyncContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.idreambooks.BookReviewList;
import com.ibm.idreambooks.CriticReview;
import com.ibm.idreambooks.DreamBooks;
import com.ibm.json.java.JSONObject;
import com.ibm.watson.WatsonTranslate;

public class ReviewAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ReviewAsyncService.class);
	private AsyncContext ac;

	public ReviewAsyncService(AsyncContext context) {
		this.ac = context;
	}

	private String translate(WatsonTranslate watson, String review) {
    	return watson.translate(review);
    }

	@Override
	  public void run() {
		String bookTitle = ac.getRequest().getParameter("title");
		boolean translate = Boolean.parseBoolean(ac.getRequest().getParameter("enable"));

		logger.debug("Requested book title {}", bookTitle);

		try {
			PrintWriter writer = ac.getResponse().getWriter();

			DreamBooks dream = new DreamBooks(bookTitle);
			BookReviewList bookReviewList = dream.getReviewList();
			ArrayList<CriticReview> reviews = bookReviewList.getBookReview().getCriticReviews();

			logger.debug("Review snippets {}", reviews.toString());

			Locale locale = ac.getRequest().getLocale();
			WatsonTranslate watson = new WatsonTranslate(locale);

			String synopsisLink = bookReviewList.getBookReview().getDetailLink();

			if(!synopsisLink.equals("")) {
				JSONObject json = new JSONObject();
				json.put("synopsis_link", synopsisLink);
				writer.write(("event: synopsis\n"));
				writer.write(("data: " + json.toString() + "\n\n"));
				writer.flush();
			}

			for(CriticReview review : reviews) {
				JSONObject json = new JSONObject();

				if(translate) {
					json.put("snippet", translate(watson, review.getSnippet()));
				}
				else {
					json.put("snippet", review.getSnippet());
				}

				json.put("link", review.getLink());
				json.put("polarity", review.getPolarity());
				json.put("source", review.getSource());
				writer.write(("data: " + json.toString() + "\n\n"));
				writer.flush();
			}

			writer.write(("event: finished\n"));
			writer.write(("data: \n\n"));
			writer.flush();
			writer.close();
		}
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		catch(NullPointerException e) {
			logger.error("Exception Review Async Service {}", e.getMessage());
		}
		finally {
			ac.complete();
		}
	}

}
