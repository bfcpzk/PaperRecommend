package recommend.service.lda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaokangpan on 2016/11/27.
 */
public class DataCombine {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("userFeatures.txt")), "UTF-8"));
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("userIds.txt")), "UTF-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("userFeatures1.txt")), "utf-8"));

        String line = "";
        List<String> l = new ArrayList<String>();
        List<String> l1 = new ArrayList<String>();
        while ((line = br.readLine()) != null){
            l.add(line);
        }
        while((line = br1.readLine()) != null){
            l1.add(line);
        }
        for(int i = 0 ; i < l1.size() ; i++){
            line = l1.get(i) + "\t" + l.get(i) + "\n";
            bw.write(line);
        }
        br.close();
        br1.close();
        bw.close();
    }
}
