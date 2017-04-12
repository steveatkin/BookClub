package com.ibm.alchemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;

// This class has been updated to use Watson Natural Language Understanding rather than Alchemy

public class Alchemy {
	private static final Logger logger = LoggerFactory.getLogger(Alchemy.class);
	private String reviewURL;
	NaturalLanguageUnderstanding service;

	private static String naturalLanguageService = "natural-language-understanding";
	private static String baseURL = "";
	private static String username = "";
	private static String password = "";

	static {
		processVCAP_Services();
	}
	

	private static void processVCAP_Services() {
    	logger.info("Processing VCAP_SERVICES");

			JSONObject sysEnv = getVcapServices();

      logger.info("Looking for: "+ naturalLanguageService);

      if (sysEnv != null && sysEnv.containsKey(naturalLanguageService)) {
      	JSONArray services = (JSONArray)sysEnv.get(naturalLanguageService);
				JSONObject service = (JSONObject)services.get(0);
				JSONObject credentials = (JSONObject)service.get("credentials");
				baseURL = (String)credentials.get("url");
				username = (String)credentials.get("username");
				password = (String)credentials.get("password");
				logger.info("baseURL  = "+baseURL);
				logger.info("username   = "+username);
				logger.info("password = "+password);
				logger.info("Watson url {} username {} and password {}", baseURL, username, password);
    	}
			else {
				logger.info("Attempting to use locally defined service credentials");
				baseURL = System.getenv("NATURAL_LANGUAGE_URL");
				username = System.getenv("NATURAL_LANGUAGE_USERNAME");
				password = System.getenv("NATURAL_LANGUAGE_PASSWORD");
				logger.info("Watson url {} username {} and password {}", baseURL, username, password);
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

	public Alchemy(String u) {
		reviewURL = u;
		logger.debug("Review URL {}", reviewURL);
		service = new NaturalLanguageUnderstanding(
  			NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
  			username,
  			password
		);
	}

	public Alchemy() {
		logger.debug("Default constructor called");
		service = new NaturalLanguageUnderstanding(
  			NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
  			username,
  			password
		);
	}

	public String getSentiment(String reviewDoc) {
		String tweetSentiment = "";

		logger.debug("Collecting Twitter sentiment for {}", reviewDoc);

		SentimentOptions sentimentOptions = new SentimentOptions.Builder().document(true).build();
		Features features = new Features.Builder().sentiment(sentimentOptions).build();
		AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(reviewDoc).features(features).build();
		AnalysisResults results = service.analyze(parameters).execute();
		DocumentSentimentResults sentimentResult = results.getSentiment().getDocument();
		Double score = sentimentResult.getScore();

		// positive, negative, neutral

		if (score <= -0.25) {
			tweetSentiment = "negative";
		}
		else if(score >= 0.25) {
			tweetSentiment = "positive";
		}
		else {
			tweetSentiment = "neutral";
		}

		logger.debug("Tweet sentiment {}", tweetSentiment);

		return tweetSentiment;
	}

	public ArrayList<Concept> getConcepts() {
		ArrayList<Concept> concepts = new ArrayList<Concept>();
		
		try {
			if(reviewURL != null) {
				ConceptsOptions conceptOptions = new ConceptsOptions.Builder().limit(5).build();
				Features features = new Features.Builder().concepts(conceptOptions).build();
				AnalyzeOptions parameters = new AnalyzeOptions.Builder().url(reviewURL).features(features).build();
				AnalysisResults results = service.analyze(parameters).execute();

				List<ConceptsResult> identifiedConcepts = results.getConcepts();
				Iterator<ConceptsResult> iter = identifiedConcepts.iterator();
				ConceptsResult conceptResult;

				while(iter.hasNext()) {
					conceptResult = iter.next();
					Concept concept = new Concept();
					concept.setConcept(conceptResult.getText());
					concept.setRelevance(conceptResult.getRelevance());
					concepts.add(concept);
				}
			}
		}
		catch(Exception e) {
			logger.error("could not get concepts from Watson natural language understanding {}", e.getMessage());
		}
		
		logger.debug("Concepts {}", concepts.toString());
		return concepts;
	}
}
