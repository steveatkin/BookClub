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

package com.ibm.watson;

import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Translation;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


public class WatsonTranslate {
	private static final Logger logger = LoggerFactory.getLogger(WatsonTranslate.class);

	private static String translationService = "language_translator";
	private static String baseURLTranslation = "";
	private static String usernameTranslation = "";
	private static String passwordTranslation = "";
	private Language targetLang = Language.ENGLISH;

	static {
		processVCAP_Services();
	}

	private static void processVCAP_Services() {
    	logger.info("Processing VCAP_SERVICES");

			JSONObject sysEnv = getVcapServices();

      logger.info("Looking for: "+ translationService);

      if (sysEnv != null && sysEnv.containsKey(translationService)) {
      	JSONArray services = (JSONArray)sysEnv.get(translationService);
				JSONObject service = (JSONObject)services.get(0);
				JSONObject credentials = (JSONObject)service.get("credentials");
				baseURLTranslation = (String)credentials.get("url");
				usernameTranslation = (String)credentials.get("username");
				passwordTranslation = (String)credentials.get("password");
				logger.info("baseURL  = "+baseURLTranslation);
				logger.info("username   = "+usernameTranslation);
				logger.info("password = "+passwordTranslation);
    	}
			else {
				logger.info("Attempting to use locally defined service credentials");
				baseURLTranslation = System.getenv("WATSON_URL");
				usernameTranslation = System.getenv("WATSON_USERNAME");
				passwordTranslation = System.getenv("WATSON_PASSWORD");
				logger.debug("Watson url {} username {} and password {}", baseURLTranslation, usernameTranslation, passwordTranslation);
			}
    }

	private static JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        if (envServices == null) return null;
        JSONObject sysEnv = null;
        try {
        	 sysEnv = JSONObject.parse(envServices);
        } catch (IOException e) {
        	// Do nothing, fall through to defaults
        	logger.error("Error parsing VCAP_SERVICES: {} ", e.getMessage());
        }
        return sysEnv;
    }

	public WatsonTranslate(Locale locale) {
		String bcp47Tag = locale.toLanguageTag();
		String isoLang = locale.getLanguage();

		logger.debug("BCP 47 language tag {}", bcp47Tag);
		logger.debug("ISO language tag {}", isoLang);
		// see if this is a supported language for Watson translate

		if(isoLang.equalsIgnoreCase("es")) {
			targetLang = Language.SPANISH;
		}
		else if (isoLang.equalsIgnoreCase("fr")) {
			targetLang = Language.FRENCH;
		}
		else if(bcp47Tag.equalsIgnoreCase("pt-BR")) {
			targetLang = Language.PORTUGUESE;
		}
	}

	public String translate(String text) {
		String translatedText = text;

		if(text != null && !text.equals("") && !targetLang.equals("")) {
			LanguageTranslator service = new LanguageTranslator();
			service.setUsernameAndPassword(usernameTranslation, passwordTranslation);
			TranslationResult translationResult = service.translate(text, Language.ENGLISH, targetLang).execute();

			Iterator<Translation> itr = translationResult.getTranslations().iterator();
			while(itr.hasNext()) {
				translatedText = itr.next().getTranslation();
			}

		}

		return translatedText;
	}

}
