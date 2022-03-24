package scripts;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		// TODO Auto-generated method stub
		// createXml();
		// createIndex("xml/collection.xml");
		//
		//String command = args[0];   
		//	String path = args[1];
	
			//if(command.equals("-c")) {
				makeCollection collection = new makeCollection("html");
				collection.makeXml();
			//}
			//else if(command.equals("-k")) {
				makeKeyword keyword = new makeKeyword("./collection.xml");
				keyword.convertXml();
			//}

			indexer ind = new indexer("indexfolder/index.xml");
			ind.post();
	}
	
}


