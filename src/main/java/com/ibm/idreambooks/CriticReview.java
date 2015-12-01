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

package com.ibm.idreambooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CriticReview {
	@JsonProperty("snippet")
	private String snippet = "";

	@JsonProperty("source")
	private String source = "";

	@JsonProperty("review_link")
	private String link = "";

	@JsonProperty("pos_or_neg")
	private String polarity = "";

	@JsonProperty("source")
	public String getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(String s) {
		source = s;
	}

	@JsonProperty("snippet")
	public String getSnippet() {
		return snippet;
	}

	@JsonProperty("snippet")
	public void setSnippet(String s) {
		snippet = s;
	}

	@JsonProperty("review_link")
	public String getLink() {
		return link;
	}

	@JsonProperty("review_link")
	public void setLink(String l) {
		link = l;
	}

	@JsonProperty("pos_or_neg")
	public String getPolarity() {
		return polarity;
	}

	@JsonProperty("pos_or_neg")
	public void setPolarity(String p) {
		polarity = p;
	}

	public String toString() {
		return "SNIPPET: " + snippet + " POLARITY: " + polarity + " LINK: " + link;
	}
}
