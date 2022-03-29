package scripts;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


public class kuir {
//javac -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8

//run code//
/*
java -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar:bin scripts.kuir 
*/

/*
-c data , -k ./collection.xml , -i ./index.xml 
java -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar:bin scripts.kuir -c data
java -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar:bin scripts.kuir -k ./collection.xml
java -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar:bin scripts.kuir -i ./index.xml
*/


	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, ClassNotFoundException {
		//String path ="./index.xml";
		//TODO Auto-generated method stub
		String command = args[0]; 
		String path = args[1];
			if(command.equals("-c")) {
				makeCollection collection = new makeCollection(path);
				collection.makeXml();
			}
			else if(command.equals("-k")) {
				makeKeyword keyword = new makeKeyword(path);
				keyword.convertXml();
			}
			else if(command.equals("-i")) {
				indexer ind = new indexer(path);
				ind.post();
			}

	}
	
}


