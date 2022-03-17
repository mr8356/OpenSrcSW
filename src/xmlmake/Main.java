package xmlmake;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.*;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		// TODO Auto-generated method stub
		createXml("html");
		createIndex("xml/collection.xml");

	}

	public static void createIndex(String xmlPath) throws TransformerException, ParserConfigurationException, IOException{
		String path = xmlPath;
		File xmlFile = null;
		try {
			xmlFile = new File(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		org.jsoup.nodes.Document xml =  Jsoup.parse(xmlFile , "UTF-8" , "" , Parser.xmlParser() );
		
		Elements doclist = xml.select("doc");
		Elements titlelist = xml.select("title");
		Elements bodylist = xml.select("body");
		//Elements bodylist = xml.select("doc");
		System.out.println(bodylist.get(0).text());
		int IdNum = doclist.size();
		//int IdNum = 1;

		for (int i=0; i<IdNum ; i++ ){
			Element d = doc.createElement("doc");
			docs.appendChild(d);
			d.setAttribute("id" , String.valueOf(i));
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(titlelist.get(i).text()));
			d.appendChild(title);
			String bodyDat = bodylist.get(i).text();

			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(bodyDat , true);

			//키워드1:빈도수# 키워드2:빈도수# 키워드3:빈도수#
			String bodyText = new String();

			for( int j = 0; j < kl.size(); j++ ) {
				Keyword kwrd = kl.get(j);
				bodyText += kwrd.getString() + ":" + kwrd.getCnt() + "#";
			}

			Element body = doc.createElement("body");
			body.appendChild(doc.createTextNode(bodyText));
			d.appendChild(body);

		}
		
		TransformerFactory transformfac = TransformerFactory.newInstance();
		Transformer transformer = transformfac.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("indexfolder/index.xml")));
		transformer.transform(source, result);
	}

	public static int createXml(String p) throws ParserConfigurationException, IOException, TransformerException {
		String path = p;
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
		int NumOfId = file.length;
		for (int i=0; i<NumOfId ; i++ ) {
			org.jsoup.nodes.Document html =  Jsoup.parse(file[i] , "UTF-8");
			String titleDat = html.title();
			String bodyDat = html.body().text();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(bodyDat , true);
			
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

		return NumOfId;
	}

	public static File[] makeFileList(String path){
		File dir = new File(path);
		return dir.listFiles();
	}
}


