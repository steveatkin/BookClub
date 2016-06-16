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

package com.ibm.globalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class Globalization {
	private static final Logger logger = LoggerFactory.getLogger(Globalization.class);
	private static String globalizationService = "IBM Globalization";
	private static String baseURLGlobalization = "";
	private static String globalizationApiKey = "";


	static {
		JSONObject sysEnv = getVcapServices();

		logger.info("Processing VCAP_SERVICES");
		logger.info("Looking for: "+ globalizationService);

		if (sysEnv != null && sysEnv.containsKey(globalizationService)) {
			JSONArray services = (JSONArray)sysEnv.get(globalizationService);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			baseURLGlobalization = (String)credentials.get("uri");
			globalizationApiKey = (String)credentials.get("api_key");
			logger.info("baseURL  = "+ baseURLGlobalization);
			logger.info("api key   = "+ globalizationApiKey);
		}
		else {
			logger.info("Attempting to use locally defined service credentials");
			baseURLGlobalization = System.getenv("GLOBALIZATION_URL");
			globalizationApiKey = System.getenv("GLOBALIZATION_API_KEY");
			logger.debug("Globalization url {} and api key {}", baseURLGlobalization, globalizationApiKey);
		}
	}

	public static String getURI() {
		return baseURLGlobalization;
	}

	public static String getAPIKey() {
		return globalizationApiKey;
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
}
