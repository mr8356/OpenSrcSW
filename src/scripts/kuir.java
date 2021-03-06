package scripts;


import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;

public class kuir {
	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, ClassNotFoundException {
		//String path ="./index.xml";
		//TODO Auto-generated method stub
		String command = args[0]; 
		String path = args[1];
			// 데이터를 
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
			else if(command.equals("-s")) {
				searcher ser = new searcher(path);
				String query = args[3];
				System.out.println(ser.CalcSim(query));
			}
			else if(command.equals("-m")) {
				String query ="";
				for (int i = 3; i < args.length; i++) {
					query+=args[i];
				}
				MidTerm mid  = new MidTerm(path);
				mid.showSnippet(query);
			}

	}
	
}

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
java -cp ./jars/jsoup-1.14.3.jar:./jars/kkma-2.1.jar:bin scripts.kuir -s ./index.post -q "라면에는 면 분말 스프가 있다."

-s ./index.post -q "라면과 스프"
*/

