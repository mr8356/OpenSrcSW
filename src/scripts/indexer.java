package scripts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class indexer {
    private String path = new String();


    public indexer(String s) {
        this.path = s;
    }

    public void post() throws IOException {
        
        HashMap hash = new HashMap<String , String>();

        File indexfile = null;
		try {
			indexfile = new File(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        
		org.jsoup.nodes.Document ind =  Jsoup.parse(indexfile , "UTF-8" , "" , Parser.xmlParser() );
		
		//Elements doclist = ind.select("doc");
		//Elements titlelist = ind.select("title");
		Elements bodylist = ind.select("body");

        
        int N = bodylist.size();

        
        HashMap stringandid = new HashMap<String , Integer>(); // df
        for (Element el : bodylist){
            String str = el.text();
            String mapstr[]=  str.split("#");
            for (String s : mapstr) {
                String temp[] = s.split(":");
                if(stringandid.containsKey(temp[0]))
                    stringandid.put(temp[0] , (int)stringandid.get(temp[0]) + 1);
                else{
                    stringandid.put(temp[0],1);
                }
            }
        }

        for (int i=0; i<bodylist.size();i++){
            Element el = bodylist.get(i);
            HashMap indexmap = new HashMap<String , Integer>(); // 이 문서의 키와 빈도수 
            String str = bodylist.get(i).text();
            String mapstr[]=  str.split("#");
            for (String s : mapstr) {
                String temp[] = s.split(":");
                indexmap.put(temp[0], Integer.parseInt(temp[1])); //tf
                double df = (int)stringandid.get(temp[0]);
                double w = (int)indexmap.get(temp[0]) * Math.log((N/df));
                String strtemp = String.valueOf(i) +" "+ String.format("%.2f", w);
                if(hash.containsKey(temp[0]))
                    hash.put(temp[0], hash.get(temp[0])+" "+ strtemp);
                    else
                    hash.put(temp[0], strtemp);
            }
        }

        Iterator<Entry<Integer, String>> entries = hash.entrySet().iterator();
        while(entries.hasNext()){
        HashMap.Entry<Integer, String> entry = entries.next();
        System.out.println( entry.getKey() +  entry.getValue());
        }
    } 
}


    

