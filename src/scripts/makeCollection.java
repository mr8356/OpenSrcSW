package scripts;

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
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class makeCollection {

    private static String p= new String();
    

    public makeCollection(String p) {
        this.p = p;
    }

    public static int makeXml() throws ParserConfigurationException, IOException, TransformerException {
		File file[] = null;
		try {
			file = makeFileList(p);
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
		StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));
		transformer.transform(source, result);

		return NumOfId;
	}

    public static File[] makeFileList(String p){
		File dir = new File(p);
		return dir.listFiles();
	}
}
