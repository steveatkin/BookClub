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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.impl.cookie.DefaultCookieSpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class TwitterInsights {
	private static final Logger logger = LoggerFactory.getLogger(TwitterInsights.class);
	private static String twitterService = "twitterinsights";
	private static String baseURLTwitter = "";
	private static String usernameTwitter = "";
	private static String passwordTwitter = "";


	static {
		JSONObject sysEnv = getVcapServices();

		logger.info("Processing VCAP_SERVICES");
		logger.info("Looking for: "+ twitterService);

		if (sysEnv != null && sysEnv.containsKey(twitterService)) {
			JSONArray services = (JSONArray)sysEnv.get(twitterService);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			baseURLTwitter = (String)credentials.get("host");
			usernameTwitter = (String)credentials.get("username");
			passwordTwitter = (String)credentials.get("password");
			logger.info("baseURL  = "+ baseURLTwitter);
			logger.info("username   = "+ usernameTwitter);
			logger.info("password = "+ passwordTwitter);
		}
		else {
			logger.info("Attempting to use locally defined service credentials");
			baseURLTwitter = System.getenv("TWITTER_URL");
			usernameTwitter = System.getenv("TWITTER_USERNAME");
			passwordTwitter = System.getenv("TWITTER_PASSWORD");
			logger.debug("Twitter url {} username {} and password {}", baseURLTwitter, usernameTwitter, passwordTwitter);
		}
	}

	private static JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        JSONObject sysEnv = null;

        if (envServices == null) {
        	logger.info("VCAP Services not found, using predfined meta-information");
        	return null;
        }

        try {
        	sysEnv = JSONObject.parse(envServices);
        }
        catch(Exception e) {
        	logger.error("Error parsing VCAP_SERVICES: {}", e.getMessage());
        }

        return sysEnv;
    }

	public TwitterInsights() {

	}

	public Sentiment getSentimentCount(String bookTitle, String bookAuthor, String sentimentType) {
		SentimentSearch returnedSentiment = new SentimentSearch();

		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(usernameTwitter,passwordTwitter));

			CookieStore cookieStore = new BasicCookieStore();
			CookieSpecProvider csf = new CookieSpecProvider() {
	            @Override
	            public CookieSpec create(HttpContext context)
	            {
	                return new DefaultCookieSpec() {
	                    @Override
	                    public void validate(Cookie cookie, CookieOrigin origin)
	                        throws MalformedCookieException
	                    {
	                        // Allow all cookies
	                    }
	                };
	            }
	        };

	        RequestConfig requestConfig = RequestConfig.custom()
	                .setCookieSpec("easy")
	                .setSocketTimeout(15 * 1000)
					.setConnectTimeout(15 * 1000)
	                .build();

			CloseableHttpClient httpClient =
			    HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
			    .setDefaultCookieStore(cookieStore)
			    .setDefaultCookieSpecRegistry(RegistryBuilder.<CookieSpecProvider>create()
                                              .register(CookieSpecs.DEFAULT, csf)
                                              .register("easy", csf).build())
                .setDefaultRequestConfig(requestConfig).build();

			URIBuilder builder = new URIBuilder();
			builder.setScheme("https").setHost(baseURLTwitter).setPath("/api/v1/messages/count")
		    .setParameter("q", "\"" + bookTitle + "\"" + " AND " + "\"" + bookAuthor + "\"" + " AND " + "sentiment:" + sentimentType);
			URI uri = builder.build();
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				// Read all the books from the best seller list
				ObjectMapper mapper = new ObjectMapper();
				returnedSentiment = mapper.readValue(rd, SentimentSearch.class);
			}
			else {
				logger.error("could not get tweets from IBM insights http code {}", httpResponse.getStatusLine().getStatusCode());
			}

		}
		catch(Exception e) {
			logger.error("Twitter error: {}", e.getMessage());
		}

		return new Sentiment(sentimentType, returnedSentiment.getCount());
	}

	public TweetList getTweetList(String bookTitle, String bookAuthor) {
		TweetList returnedTweets = new TweetList();

		try {

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(usernameTwitter,passwordTwitter));

			CookieStore cookieStore = new BasicCookieStore();
			CookieSpecProvider csf = new CookieSpecProvider() {
	            @Override
	            public CookieSpec create(HttpContext context)
	            {
	                return new DefaultCookieSpec() {
	                    @Override
	                    public void validate(Cookie cookie, CookieOrigin origin)
	                        throws MalformedCookieException
	                    {
	                        // Allow all cookies
	                    }
	                };
	            }
	        };

	        RequestConfig requestConfig = RequestConfig.custom()
	                .setCookieSpec("easy")
	                .setSocketTimeout(10 * 1000)
					.setConnectTimeout(10 * 1000)
	                .build();

			CloseableHttpClient httpClient =
			    HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
			    .setDefaultCookieStore(cookieStore)
			    .setDefaultCookieSpecRegistry(RegistryBuilder.<CookieSpecProvider>create()
                                              .register(CookieSpecs.DEFAULT, csf)
                                              .register("easy", csf).build())
                .setDefaultRequestConfig(requestConfig).build();

			URIBuilder builder = new URIBuilder();
			builder.setScheme("https").setHost(baseURLTwitter).setPath("/api/v1/messages/search")
		    .setParameter("q", "\"" + bookTitle + "\"" + " AND " + "\"" + bookAuthor + "\"")
		    .setParameter("size", "5");
			URI uri = builder.build();
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				// Read all the books from the best seller list
				ObjectMapper mapper = new ObjectMapper();
				returnedTweets = mapper.readValue(rd, TweetList.class);
			}
		}
		catch(Exception e) {
			logger.error("Twitter error: {}", e.getMessage());
		}

		return returnedTweets;
	}

}
