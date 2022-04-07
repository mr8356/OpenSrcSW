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

    public double[] InnerProduct(String query) throws IOException, ClassNotFoundException {
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
                if (!post.contains(key.get(i))) {
                    for (int j = 0; j<idNum; j++) {
                        postVec[j][i] = 0;
                    }
                    continue;
                }
                String temp[] =  outhash.get(key.get(i)).split(" ");
                for (int j = 0; j<idNum; j++) {
                    double w = Double.parseDouble(temp[j*2+1]);
                    postVec[j][i] = w;
                }
        }


        double[] result = new double[idNum];
        
        double sum=0;
        
        for (int i = 0; i < idNum; i++) { //i 문서
            sum=0.0;
            for (int j = 0; j <keyNum; j++) {
                sum+= (double) qVec.get(j)* postVec[i][j];
            }
            result[i] = sum;
        }

        return result;
    }
}
