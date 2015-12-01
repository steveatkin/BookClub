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

package com.ibm.nytimes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.nytimes.BestSellerList;

public class NewYorkTimes {
	private static final Logger logger = LoggerFactory.getLogger(NewYorkTimes.class);
	private static String url;
	private static String apiKey;
	private String listName;
	private String listDate;

	static {
		url = System.getenv("NY_TIMES_URL");
		apiKey = System.getenv("NY_TIMES_API_KEY");
		logger.debug("NY times url {} and api key {}", url, apiKey);
	}

	public NewYorkTimes(String list, String date) {
		listName = list;
		listDate = date;
		logger.debug("List name {} and date {}", listName, listDate);
	}

	public BestSellerList getList() throws Exception {
		BestSellerList returnedList = new BestSellerList();

		try {
			if(listName != null && listDate != null && url != null && apiKey != null) {
				RequestConfig config = RequestConfig.custom()
					    .setSocketTimeout(10 * 1000)
					    .setConnectTimeout(10 * 1000)
					    .build();
				CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(config).build();

				URIBuilder builder = new URIBuilder();
				builder.setScheme("http").setHost(url).setPath("/svc/books/v2/lists/" + listDate + "/" + listName)
				.setParameter("api-key", apiKey);
				URI uri = builder.build();
				HttpGet httpGet = new HttpGet(uri);
				httpGet.setHeader("Content-Type", "text/plain");

				HttpResponse httpResponse = httpclient.execute(httpGet);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

					// Read all the books from the best seller list
					ObjectMapper mapper = new ObjectMapper();
					returnedList = mapper.readValue(rd, BestSellerList.class);
				}
				else {
					logger.error("could not get list from ny times http code {}", httpResponse.getStatusLine().getStatusCode());
				}
			}
		}
		catch(Exception e) {
			logger.error("could not get list from ny times {}", e.getMessage());
			throw e;
		}

		return returnedList;
	}

}
