/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/29 17:03</create-date>
 *
 * <copyright file="Corpus.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package recommend.service.lda;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a set of documents
 * 语料库，也就是文档集合
 *
 * @author hankcs
 */
public class Corpus
{
    List<String> idList;
    List<int[]> documentList;
    Vocabulary vocabulary;

    public Corpus()
    {
        idList = new ArrayList<String>();
        documentList = new LinkedList<int[]>();
        vocabulary = new Vocabulary();
    }

    public int[] addDocument(List<String> document)
    {
        int[] doc = new int[document.size()];
        int i = 0;
        for (String word : document)
        {
            doc[i++] = vocabulary.getId(word, true);
        }
        documentList.add(doc);
        return doc;
    }

    public int[][] toArray()
    {
        return documentList.toArray(new int[0][]);
    }

    public int getVocabularySize()
    {
        return vocabulary.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (int[] doc : documentList)
        {
            sb.append(Arrays.toString(doc)).append("\n");
        }
        sb.append(vocabulary);
        return sb.toString();
    }

    /**
     * Load documents from disk
     *
     * @param folderPath is a folder, which contains text documents.
     * @return a corpus
     * @throws IOException
     */
    public static Corpus load(String folderPath) throws IOException
    {
        Corpus corpus = new Corpus();
        File folder = new File(folderPath);
        for (File file : folder.listFiles())
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            List<String> wordList = new LinkedList<String>();
            while ((line = br.readLine()) != null)
            {
                String[] words = line.split(" ");
                for (String word : words)
                {
                    if (word.trim().length() < 2) continue;
                    wordList.add(word);
                }
            }
            br.close();
            corpus.addDocument(wordList);
        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static Corpus load_file(String filename) throws IOException
    {
        Corpus corpus = new Corpus();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null)
        {
            if(line.split("\t").length == 2 && isNumeric(line.split("\t")[0])){
                line = line.split("\t")[1];
                List<String> wordList = new LinkedList<String>();
                String[] words = line.split(",");
                for (String word : words)
                {
                    if (word.trim().length() < 2) continue;
                    wordList.add(word);
                }
                corpus.addDocument(wordList);
            }
        }
        br.close();
        if (corpus.getVocabularySize() == 0) return null;
        return corpus;
    }

    public static void saveTopic(Jdbc_Util db, double[][] theta, Corpus corpus){
        String sql = "";
        for(int i = 0 ; i < theta.length ; i++){
            String res = "";
            for(int j = 0 ; j < theta[0].length; j++){
                res += theta[i][j] + ",";
            }
            sql = "insert into `paper_topic` (`paper_id`, `topic_dist`) values ('" + corpus.idList.get(i) + "', '" + res + "');\n";
            db.add(sql);
        }
    }

    public static Corpus load_mysql(Jdbc_Util db) throws IOException, SQLException
    {
        Corpus corpus = new Corpus();
        String sql = "select item_ut, abs from paper_info";
        ResultSet rs;
        rs = db.select(sql);
        while(rs.next()){
            String line = rs.getString("abs");
            String id = rs.getString("item_ut");

            line = line.replace(".","").replace("(","").replace(")","").toLowerCase();
            List<String> wordList = new LinkedList<String>();
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                wordList.add(word);
            }
            corpus.addDocument(wordList);
            corpus.idList.add(id);

        }
        if (corpus.getVocabularySize() == 0) return null;
        return corpus;
    }

    /*public static Corpus mongo_load(String db_name,String collection_name,int begin,int end) throws IOException
    {
        Corpus corpus = new Corpus();
        Mongo mongo = new Mongo("127.0.0.1",12345);
        DB db = mongo.getDB(db_name);
        DBCollection coll = db.getCollection(collection_name);
        DBCursor cursor = coll.find().limit(end).skip(begin);
        while(cursor.hasNext())
        {
            String line;
            line = cursor.next().get("description").toString();
            List<String> wordList = new LinkedList<String>();
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                wordList.add(word);
            }
            corpus.addDocument(wordList);
        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }*/

    public Vocabulary getVocabulary()
    {
        return vocabulary;
    }

    public int[][] getDocument()
    {
        return toArray();
    }

    public static int[] loadDocument(String path, Vocabulary vocabulary) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        List<Integer> wordList = new LinkedList<Integer>();
        while ((line = br.readLine()) != null)
        {
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                Integer id = vocabulary.getId(word);
                if (id != null)
                    wordList.add(id);
            }
        }
        br.close();
        int[] result = new int[wordList.size()];
        int i = 0;
        for (Integer integer : wordList)
        {
            result[i++] = integer;
        }
        return result;
    }
}
