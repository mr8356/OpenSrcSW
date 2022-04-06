package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher {
    private String path;

    public searcher(String path) {
        this.path = path;
    }

    public String CalcSim(String query) throws IOException, ClassNotFoundException {
        int idNum , keyNum;
        KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query , true);
        keyNum =  kl.size();
        ArrayList<String> key = new ArrayList<String>();
        ArrayList<Double> qVec = new ArrayList<Double>();
        //ArrayList<Double[]> postVec = new ArrayList<Double[]>();
        
        for (Keyword kwrd : kl) {
            key.add(kwrd.getString());
            qVec.add((double) kwrd.getCnt());
        }

        FileInputStream filein = new FileInputStream(path);
        ObjectInputStream obin = new ObjectInputStream(filein);
        Object obj = obin.readObject();
        obin.close();
        HashMap<String  , String> outhash = (HashMap<String  , String>)obj;
        Set<String> post = outhash.keySet();

        idNum = ((((String) outhash.get(post.iterator().next())).split(" ")).length)/2;
        double[][] postVec = new double[idNum][keyNum];

        for (int i = 0; i < keyNum; i++) {//ki j문서
                String temp[] =  outhash.get(key.get(i)).split(" ");
                for (int j = 0; j<idNum; j++) {
                    double w = Double.parseDouble(temp[j*2+1]);
                    postVec[j][i] = w;
                }
        }


        // for (String k : key) {
        //     System.out.println("단어들 "+k);
        // }

        double[] result = new double[idNum];
        
        double sum=0;
        
        for (int i = 0; i < idNum; i++) { //i 문서
            sum=0.0;
            for (int j = 0; j <keyNum; j++) {
                sum+= (double) qVec.get(j)* postVec[i][j];
            }
            result[i] = sum;
        }

        // for (int k = 0; k < result.length; k++) {
        //     System.out.println("id는 "+k+ "  "+result[k]);
        // }

        ArrayList<Integer> maxindex = new ArrayList<Integer>();
        for (int i=0; i<3; i++) {
            int tempindex=0;
            double Max = 0.0;
            for (int j=0; j<idNum; j++) {
                if(result[j]>=Max){
                    Max = result[j];
                    tempindex = j;
                }
            }
            if(Max==0)
                continue;
            maxindex.add(tempindex);
            result[tempindex]=0;
        }
        File xmlFile = null;
		try {
			xmlFile = new File("index.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.jsoup.nodes.Document xml =  Jsoup.parse(xmlFile , "UTF-8" , "" , Parser.xmlParser() );
		Elements titlelist = xml.select("title");
        String str=new String();
        for (Integer i : maxindex) {
            str+=titlelist.get(i).text()+" ";
        }
        return str;
    }
}
