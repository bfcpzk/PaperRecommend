package recommend.model;

import recommend.service.lda.Jdbc_Util;

import java.io.*;

/**
 * Created by zhaokangpan on 2016/12/9.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Jdbc_Util db = new Jdbc_Util();
        String sql = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/zhaokangpan/Desktop/paper_keywords.csv")),"utf-8"));
        String line = "";
        String id = "";
        while((line = br.readLine()) != null){
            String[] tmp = line.split(",");
            String item_ut = tmp[0];
            String keywords = tmp[1].replace("\"","").replace(",","").toLowerCase();
            sql = "insert into paper_keywords (`item_ut`, `keywords`) values ('" + item_ut + "','" + keywords + "')";
            db.add(sql);
        }
        /*while ((line = br.readLine()) != null){
            String[] commaSplit = line.split(",");
            id = commaSplit[0];
            String ltmp = "";
            for(int i = 4 ; i < commaSplit.length ; i++){
                ltmp += commaSplit[i];
            }
            ltmp = ltmp.replace("\"\"","");
            String[] spl = ltmp.split("\"");
            ltmp = "";
            for(int i = 0 ; i < spl.length - 1 ; i++ ){
                ltmp += spl[i];
            }
            sql = "update paper_info set abstract='" + ltmp.replace("'", "") + "' where item_ut='" + id + "'";
            db.add(sql);
            System.out.println(id + "\t" + ltmp);
        }*/
    }
}
