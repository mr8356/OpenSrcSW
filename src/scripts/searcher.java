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

    public double InnerProduct(ArrayList<Double> qVec , double[] postVec) throws IOException, ClassNotFoundException {
        double sum =0.0;
        for (int j = 0; j <qVec.size(); j++) {
            sum+= (double) qVec.get(j)* postVec[j];
        }
        return sum;
    }

}
