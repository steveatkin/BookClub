package com.ibm.alchemy;


public class Concept {
	private String concept = "";
	private double relevance;


	public String getConcept() {
		return concept;
	}
	
	public void setConcept(String c) {
		concept = c;
	}
	
	public double getRelevance() {
		return relevance;
	}
	

	public void setRelevance(double d) {
		relevance = d;
	}
	
	public String toString() {
		return "CONCEPT: " + concept + " RELEVANCE: " + Double.toString(relevance); 
	}
}
