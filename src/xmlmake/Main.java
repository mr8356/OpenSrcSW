package xmlmake;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		// TODO Auto-generated method stub
		String path = "html";
		File file[] = null;
		try {
			file = makeFileList(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		for (int i=0; i<file.length ; i++ ) {
			org.jsoup.nodes.Document html =  Jsoup.parse(file[i] , "UTF-8");
			
			String titleDat = html.title();
			String bodyDat = html.body().text();
			
			//book
			
			
			Element d = doc.createElement("doc");
			docs.appendChild(d);
			d.setAttribute("id" , String.valueOf(i));
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(titleDat));
			d.appendChild(title);
			
			Element body = doc.createElement("body");
			body.appendChild(doc.createTextNode(bodyDat));
			d.appendChild(body);
			
		}
		
		TransformerFactory transformfac = TransformerFactory.newInstance();
		Transformer transformer = transformfac.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("xml/collection.xml")));
		transformer.transform(source, result);
		

	}

	public static File[] makeFileList(String path){
		File dir = new File(path);
		return dir.listFiles();
	}
}


