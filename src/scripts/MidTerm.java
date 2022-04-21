package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MidTerm {

    private String path;

    public MidTerm(String path) {
        this.path = path;
    }

    public void showSnippet(String query) throws ParserConfigurationException, IOException {
        KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query , true);
        
        ArrayList <String> klarr = new ArrayList<String>();
        for (int i = 0; i < kl.size(); i++) {
            klarr.add(kl.get(i).getString());
        }

        File xmlFile = null;
		try {
			xmlFile = new File(this.path);
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
		//System.out.println(bodylist.get(0).text());
		int IdNum = doclist.size();
		for (int i=0; i<IdNum ; i++ ){
			String bodyDat = bodylist.get(i).text();
            char[] bodychar = bodyDat.toCharArray();
            int charsize = bodychar.length;
            int max=0;
            String maxString="";
            for (int j = 0; j < charsize-30; j++) {
                int score=0;
                String temp = "";
                for (int k = j; k < j+30; k++) {
                    temp += bodychar[k];
                }
                KeywordExtractor ketemp = new KeywordExtractor();
		        KeywordList kltemp = ketemp.extractKeyword(temp , true);
                for (Keyword k : kltemp) {
                    if(klarr.contains(k.getString())){
                        score+=k.getCnt();
                    }
                }
                if (score>=max) {
                    max = score;
                    maxString = temp;
                }
            }
            if(max==0){
                continue;
            }
            else{
                System.out.println(titlelist.get(i).text()+" ,"+ maxString +", "+max);
            }
        }
    }
}
