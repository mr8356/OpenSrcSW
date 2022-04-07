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
                if (!post.contains(key.get(i))) {
                    for (int j = 0; j<idNum; j++) {
                        postVec[j][i] = 0.0;
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
            double temp = getABsize(qVec, postVec[i]);
            if(temp==0)
                result[i] = 0.0;
            else
                result[i] = InnerProduct(qVec , postVec[i])/getABsize(qVec, postVec[i]);
        }

        ArrayList<Integer> index = new ArrayList<Integer>();
        double[] maxCalc = result.clone();
        for (int i=0; i<3; i++) {
            int maxindex=0;
            for (int j=0; j<idNum; j++) {
                if(maxCalc[j]>=maxCalc[maxindex]){
                    maxindex = j;
                }
            }
            if(maxCalc[maxindex]==0)
                continue;
            index.add(maxindex);
            maxCalc[maxindex]=0.0;

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
        for (int i: index) {
            str+=titlelist.get(i).text()+" "+result[i] +"\n";
        }
        return str;
    }

    private double getABsize(ArrayList<Double> q , double postV[]){
        double tempA = 0.0;
        double tempB = 0.0;
        for (double d : postV) {
            tempB += d*d;
        }
        for (double d : q) {
            tempA += d*d;
        }
        return Math.sqrt(tempB)*Math.sqrt(tempA);
    }

    public double InnerProduct(ArrayList<Double> qVec , double[] postVec) throws IOException, ClassNotFoundException {
        double sum =0.0;
        for (int j = 0; j <qVec.size(); j++) {
            sum+= (double) qVec.get(j)* postVec[j];
        }
        return sum;
    }

}
