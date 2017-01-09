package com.ibm.alchemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.likethecolor.alchemy.api.Client;
import com.likethecolor.alchemy.api.call.AbstractCall;
import com.likethecolor.alchemy.api.entity.ConceptAlchemyEntity;
import com.likethecolor.alchemy.api.entity.Response;
import com.likethecolor.alchemy.api.call.RankedConceptsCall;
import com.likethecolor.alchemy.api.call.type.CallTypeUrl;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

public class Alchemy {
	private static final Logger logger = LoggerFactory.getLogger(Alchemy.class);
	private static String apiKey;
	private String reviewURL;

	static {
		apiKey = System.getenv("ALCHEMY_API_KEY");
		logger.debug("Alchemy api key {}", apiKey);
	}
	
	public Alchemy(String u) {
		reviewURL = u;
		logger.debug("Review URL {}", reviewURL);
	}

	public Alchemy() {
		logger.debug("Default constructor called");
	}

	public String getSentiment(String reviewDoc) {
		AlchemyLanguage service = new AlchemyLanguage();
		service.setApiKey(apiKey);
		String tweetSentiment = "";

		service.setEndPoint("https://gateway-a.watsonplatform.net/calls");

		logger.info("Collecting Twitter sentiment for {}", reviewDoc);

		Map<String,Object> params = new HashMap<String, Object>();
		params.put(AlchemyLanguage.TEXT, reviewDoc);
		try {
			DocumentSentiment sentiment = service.getSentiment(params).execute();
			logger.info("Twitter sentiment: {}", sentiment.toString());
			tweetSentiment = sentiment.getSentiment().getType().toString();
		}
		catch(Exception e) {
			logger.error("Error getting sentiment {}", e.getMessage());
		}
		return tweetSentiment;
	}

	public ArrayList<Concept> getConcepts() {
		ArrayList<Concept> concepts = new ArrayList<Concept>();
		
		try {
			if(reviewURL != null && apiKey != null) {
				Client client = new Client(apiKey);
				
				AbstractCall<ConceptAlchemyEntity> rankedConceptsCall = new RankedConceptsCall(new CallTypeUrl(reviewURL));
				Response<ConceptAlchemyEntity> conceptResponse = client.call(rankedConceptsCall);
		
				ConceptAlchemyEntity entity;
				Iterator<ConceptAlchemyEntity> iter = conceptResponse.iterator();
			
				while(iter.hasNext()) {
					entity = iter.next();
					Concept concept = new Concept();
					concept.setConcept(entity.getConcept());
					concept.setRelevance(entity.getScore());
					concepts.add(concept);
				}
			}
		}
		catch(IOException e) {
			logger.error("could not get concepts from alchemy {}", e.getMessage());
		}
		
		logger.debug("Concepts {}", concepts.toString());
		return concepts;
	}
}
