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

package com.ibm.twitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Sentiment {

	@JsonIgnore
	private SentimentCount count = new SentimentCount();

	@JsonIgnore
	public Sentiment(String p, SentimentCount c) {
		polarity = p;
		count = c;
	}

	public Sentiment() {

	}

	@JsonProperty("polarity")
	private String polarity = "";

	@JsonProperty("polarity")
	public String getPolarity() {
		return polarity;
	}

	@JsonProperty("polarity")
	public void setPolarity(String p){
		polarity = p;
	}

	@JsonIgnore
	public void setCount(long l) {
		 count.setCount(l);
	}

	@JsonIgnore
	public long getCount() {
		return count.getCount();
	}

	public String toString() {
		return "SENTIMENT: " + polarity;
	}

}
