package recommend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommend.dao.SqlDao;
import recommend.model.Keywords;
import recommend.model.Paper;
import recommend.model.Topic;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
@Service
public class PaperService {

    @Autowired
    private SqlDao sqlDao;

    public static List<Keywords> removeDuplicateByItem_ut(List<Keywords> klist){
        Set<String> test = new HashSet<String>();
        for(int i = 0 ; i < klist.size() ; i++){
            if(test.contains(klist.get(i).getItem_ut())){
                klist.remove(i);
                i--;
            }else{
                test.add(klist.get(i).getItem_ut());
            }
        }
        return klist;
    }

    public static List<Keywords> removeDuplicate(List<Keywords> klist){
        Set<String> test = new HashSet<String>();
        for(int i = 0 ; i < klist.size() ; i++){
            if(test.contains(klist.get(i).getKeywords())){
                klist.remove(i);
                i--;
            }else{
                test.add(klist.get(i).getKeywords());
            }
        }
        return klist;
    }

    public List<Paper> recommendBasedOnKeywords(String[] wordList, HttpSession session){
        List<Paper> paperList = new ArrayList<Paper>();
        List<Keywords> klist = new ArrayList<Keywords>();
        String wlist = "";
        String plist = "";
        for(int i = 0 ; i < wordList.length - 1 ; i++){
            wlist += "'" + wordList[i] + "', ";
        }
        wlist += "'" + wordList[wordList.length - 1] + "'";
        if(!wlist.equals("''")){
            klist = sqlDao.selectArticleIdBasedOnKeywords(wlist);
            //去重
            klist = removeDuplicateByItem_ut(klist);
            for(int i = 0 ; i < klist.size() ; i++){
                System.out.println(klist.get(i).getItem_ut() + "\t" + klist.get(i).getKeywords());
            }

            for(int i = 0 ; i < Math.min(5, klist.size()) - 1 ; i++){
                plist += "'" + klist.get(i).getItem_ut() + "', ";
            }
            plist += "'" + klist.get(Math.min(5, klist.size()) - 1) + "'";
            paperList = sqlDao.selectPaperByIdList(plist);

            //数据不足进行补充
            if(paperList.size() < 5){
                List<Paper> supplyment = sqlDao.getSupplement(5 - paperList.size(), plist);
                paperList.addAll(supplyment);
            }
        }else{
            paperList = sqlDao.getRandom(5);
        }

        session.setAttribute("haveRecommendIdList", new HashSet<String>());
        session.setAttribute("mayLoveIdList", new HashSet<String>());

        return paperList;
    }

    public List<Keywords> getKeywordsList(){
        List<Keywords> klist = new ArrayList<Keywords>();
        try{
            klist = sqlDao.getKeywordsList();
            klist = removeDuplicate(klist);
        }catch (Exception e){
            e.printStackTrace();
        }
        return klist;
    }

    public static double cosSimilarity(Vector<Double> v1, Vector<Double> v2){
        double res = 0.0;
        double v1square = 0.0;
        double v2square = 0.0;
        for(int i = 0 ; i < v1.size() ; i++){
            res += v1.get(i) * v2.get(i);
            v1square += Math.pow(v1.get(i), 2.0);
            v2square += Math.pow(v2.get(i), 2.0);
        }
        return res/(Math.sqrt(v1square * v2square));
    }

    public List<Paper> recommendBasedOnScoreLda(HttpSession session, String score){
        Map<String, Map<String, Double>> simMatrix = (Map<String, Map<String, Double>>)session.getAttribute("simMatrix");
        Set<String> haveRecommendIdList = (HashSet<String>)session.getAttribute("haveRecommendIdList");
        Set<String> mayLoveIdList = (HashSet<String>)session.getAttribute("mayLoveIdList");
        Map<String, Double> finalScoreMap = new TreeMap<String, Double>();
        List<Paper> paperList = new ArrayList<Paper>();
        String res = "";
        if(!(score == null || score.equals(""))){
            if(simMatrix != null){//session 结果不空
                String[] inputScore = score.split(";");
                for(int i = 0 ; i < inputScore.length ; i++){
                    String[] idScorePair = inputScore[i].split(",");
                    String pid = idScorePair[0];
                    double pScore = Double.parseDouble(idScorePair[1]);
                    haveRecommendIdList.add(pid);
                    if(pScore > 0.01){
                        mayLoveIdList.add(pid);
                    }
                    Map<String, Double> tmp = simMatrix.get(pid);
                    for(Object key : tmp.keySet()){
                        if(finalScoreMap.containsKey(key)){
                            double tmpVal = finalScoreMap.get(key);
                            tmpVal += tmp.get(key) * pScore;
                            finalScoreMap.put(key.toString(), tmpVal);
                        }else{
                            double tmpVal = tmp.get(key) * pScore;
                            finalScoreMap.put(key.toString(), tmpVal);
                        }
                    }
                }
                //这里将map.entrySet()转换成list
                List<Map.Entry<String,Double>> slist = new ArrayList<Map.Entry<String,Double>>(finalScoreMap.entrySet());
                //然后通过比较器来实现排序
                Collections.sort(slist,new Comparator<Map.Entry<String,Double>>() {
                    //降序排序
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                int count = 0;
                for(int i = 0 ; i < slist.size() ; i++){
                    Map.Entry<String, Double> entryT = slist.get(i);
                    if(!haveRecommendIdList.contains(entryT.getKey())){
                        if(count < 4){
                            res += "'" + entryT.getKey() + "', ";
                            count++;
                        }else{
                            res += "'" + entryT.getKey() + "'";
                            break;
                        }
                    }
                }
                session.setAttribute("haveRecommendIdList", haveRecommendIdList);
                session.setAttribute("mayLoveIdList", mayLoveIdList);

                paperList = sqlDao.selectPaperByIdList(res);
                System.out.print(paperList.size());
            }
        }else{
            paperList = sqlDao.getRandom(5);
        }
        return paperList;
    }

    public void calculateSimilarity(HttpSession session){
        //计算全部文章的相似度
        Map<String, Map<String, Double>> simMatric;
        List<Topic> tList;
        try{
            tList = sqlDao.getAllArticleTopic();
            simMatric = new HashMap<String, Map<String, Double>>();
            for(int i = 0 ; i < tList.size() ; i++){
                String[] topics = tList.get(i).getTopic_dist().split(",");
                Vector td = new Vector<Double>();
                for(int k = 0 ; k < topics.length ; k++){
                    td.add(Double.parseDouble(topics[k]));
                }
                tList.get(i).setTd(td);
            }
            for(int i = 0 ; i < tList.size() ; i++){
                Map<String, Double> tmp = new HashMap<String, Double>();
                for(int j = 0 ; j < tList.size() ; j++){
                    double sim = cosSimilarity(tList.get(i).getTd(), tList.get(j).getTd());
                    tmp.put(tList.get(j).getPaper_id(), sim);
                }
                simMatric.put(tList.get(i).getPaper_id(), tmp);
            }
            session.setAttribute("simMatrix", simMatric);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Paper> getMayLove(HttpSession session){
        List<Paper> paperList;
        Set<String> mayLoveIdList = (HashSet<String>)session.getAttribute("mayLoveIdList");
        String res = "";
        int count = 0;
        for(String str : mayLoveIdList){
            if(count == Math.min(5, mayLoveIdList.size() - 1)){
                res += "'" + str + "'";
                break;
            }
            count++;
            res += "'" + str + "', ";
        }
        paperList = sqlDao.selectPaperByIdList(res);
        return paperList;
    }

}
