package scripts;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


public class Main {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		// TODO Auto-generated method stub
		// createXml();
		// createIndex("xml/collection.xml");
		makeCollection cXml = new makeCollection("html");
		makeKeyword cInd = new makeKeyword("xml/collection.xml");

		cXml.createXml();
		cInd.createIndex();

	}

	
}


