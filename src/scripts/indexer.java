package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.jsoup.select.Elements;

public class indexer {
    private String path = new String();


    public indexer(String s) {
        this.path = s;
    }

    public void post() throws IOException, ClassNotFoundException {
        
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
            //Element el = bodylist.get(i);
            HashMap indexmap = new HashMap<String , Integer>(); // 이 문서의 키와 빈도수 
            String str = bodylist.get(i).text();
            String mapstr[]=  str.split("#");
            ArrayList arr = new ArrayList<String>();
            for (String s : mapstr) {
                String temp[] = s.split(":");
                //arr.add(temp[0]);
                indexmap.put(temp[0], Integer.parseInt(temp[1])); //tf
                double df = (int)stringandid.get(temp[0]);
                double w = (int)indexmap.get(temp[0]) * Math.log((N/df));//계산
                String strtemp = String.valueOf(i) +" "+ String.format("%.2f", w);
                
                if(hash.containsKey(temp[0]))
                    hash.put(temp[0], hash.get(temp[0])+" "+ strtemp);
                    else
                    hash.put(temp[0], strtemp);
            }
            Set<String> tt = stringandid.keySet();
            for(String k : tt){
                if (!indexmap.containsKey(k)) {
                    if(hash.containsKey(k))
                        hash.put(k,hash.get(k)+" "+ String.valueOf(i) +" "+ "0.0");
                    else
                        hash.put(k,String.valueOf(i) +" "+ "0.0");
                }
            }
            
        }
        // Set<String> sett = hash.keySet();
        // for (String k : sett) {
        //     System.out.println( k + " -> "+ hash.get(k));
        // }

        FileOutputStream fileout = new FileOutputStream("index.post");
        ObjectOutputStream obout =  new ObjectOutputStream(fileout);
        obout.writeObject(hash);
        obout.close();

        FileInputStream filein = new FileInputStream("index.post");
        ObjectInputStream obin = new ObjectInputStream(filein);
        Object obj = obin.readObject();
        obin.close();

        
        HashMap outputhash = (HashMap)obj;
        Iterator<String> inter = outputhash.keySet().iterator();
        while(inter.hasNext()){
            String k= inter.next();
            System.out.println( k + " -> "+ outputhash.get(k));
        }
        
        

        
    } 
}


    

