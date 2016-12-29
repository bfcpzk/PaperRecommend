package recommend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommend.dao.SqlDao;
import recommend.model.*;

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

    public List<WordCount> getWordCount(){
        List<WordCount> wcList = new ArrayList<WordCount>();
        try{
            wcList = sqlDao.getWordCount(30);
        }catch(Exception e){
            e.printStackTrace();
        }
        return wcList;
    }

    public List<Paper> recommendBasedOnKeywords(String[] wordList, HttpSession session, String secMacAddr){
        List<Paper> paperList;
        List<Keywords> kList;
        String wList = "";
        String pList = "";
        for(int i = 0 ; i < wordList.length - 1 ; i++){
            wList += "'" + wordList[i] + "', ";
        }
        wList += "'" + wordList[wordList.length - 1] + "'";
        if(!wList.equals("''")){
            kList = sqlDao.selectArticleIdBasedOnKeywords(wList);
            //去重
            kList = removeDuplicateByItem_ut(kList);
            for(int i = 0 ; i < kList.size() ; i++){
                System.out.println(kList.get(i).getItem_ut() + "\t" + kList.get(i).getKeywords());
            }

            for(int i = 0 ; i < Math.min(5, kList.size()) - 1 ; i++){
                pList += "'" + kList.get(i).getItem_ut() + "', ";
            }
            pList += "'" + kList.get(Math.min(5, kList.size()) - 1) + "'";
            paperList = sqlDao.selectPaperByIdList(pList);

            //数据不足进行补充
            if(paperList.size() < 5){
                List<Paper> supplyment = sqlDao.getSupplement(5 - paperList.size(), pList);
                paperList.addAll(supplyment);
            }
        }else{
            paperList = sqlDao.getRandom(5);
        }

        session.setAttribute("haveRecommendIdList_" + secMacAddr, new HashSet<String>());
        session.setAttribute("mayLoveIdList_" + secMacAddr, new HashSet<String>());

        return paperList;
    }

    public List<Keywords> getKeywordsList(){
        List<Keywords> kList = new ArrayList<Keywords>();
        try{
            kList = sqlDao.getKeywordsList();
            kList = removeDuplicate(kList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return kList;
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

    public static List<Paper> calComplexIndex(List<Paper> paperList, Parameter p){
        Map<String, Double> complexRecommendMap = new TreeMap<String, Double>();
        int timeMax = -1, lengthMax = -1, citeMax = -1;
        int timeMin = 99999,lengthMin = 99999,citeMin = 99999;
        for(int i = 0 ; i < paperList.size() ; i++){
            if(timeMax < paperList.get(i).getPublication_year()){
                timeMax = paperList.get(i).getPublication_year();
            }
            if(timeMin > paperList.get(i).getPublication_year()){
                timeMin = paperList.get(i).getPublication_year();
            }
            if(lengthMax < paperList.get(i).getPage_count()){
                lengthMax = paperList.get(i).getPage_count();
            }
            if(lengthMin > paperList.get(i).getPage_count()){
                lengthMin = paperList.get(i).getPage_count();
            }
            if(citeMax < paperList.get(i).getCited_count()){
                citeMax = paperList.get(i).getCited_count();
            }
            if(citeMin > paperList.get(i).getCited_count()){
                citeMin = paperList.get(i).getCited_count();
            }
        }
        for(int i = 0 ; i < paperList.size() ; i++){
            Paper pTmp = paperList.get(i);
            double perScore = 0.0;
            double timeTmp = (pTmp.getPublication_year() - timeMin)/(timeMax - timeMin);
            double citeTmp = (pTmp.getCited_count() - citeMin)/(citeMax - citeMin);
            double lengthTmp = (pTmp.getPage_count() - lengthMin)/(lengthMax - lengthMin);
            perScore = timeTmp * p.getTime() + citeTmp * p.getCite() + lengthTmp * p.getLength();
            complexRecommendMap.put(pTmp.getItem_ut(), perScore);
        }
        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Double>> sList = new ArrayList<Map.Entry<String,Double>>(complexRecommendMap.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(sList,new Comparator<Map.Entry<String,Double>>() {
            //降序排序
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Set<String> idSet = new HashSet<String>();
        for(int i = 0 ; i < sList.size(); i++){
            Map.Entry<String, Double> entryT = sList.get(i);
            idSet.add(entryT.getKey());
            if(idSet.size() >= 5){
                break;
            }
        }
        for(int i = 0 ; i < paperList.size() ; i++){
            if(!idSet.contains(paperList.get(i).getItem_ut())){
                paperList.remove(i);
                i--;
            }
        }
        return paperList;
    }

    public List<Paper> recommendBasedOnScoreLda(HttpSession session, String score, Parameter p, String secMacAddr){
        Map<String, Map<String, Double>> simMatrix = (Map<String, Map<String, Double>>)session.getAttribute("simMatrix_" + secMacAddr);
        Set<String> haveRecommendIdList = (HashSet<String>)session.getAttribute("haveRecommendIdList_" + secMacAddr);
        Set<String> mayLoveIdList = (HashSet<String>)session.getAttribute("mayLoveIdList_" + secMacAddr);
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
                    p.setSimilarity(p.getSimilarity() + pScore * 0.05);
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
                List<Map.Entry<String,Double>> sList = new ArrayList<Map.Entry<String,Double>>(finalScoreMap.entrySet());
                //然后通过比较器来实现排序
                Collections.sort(sList,new Comparator<Map.Entry<String,Double>>() {
                    //降序排序
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                System.out.println("sim : " + p.getSimilarity());
                System.out.println("sList : " + sList.size());
                Set<String> idSet = new HashSet<String>();
                for(int i = 0 ; i < sList.size() * p.getSimilarity() ; i++){
                    Map.Entry<String, Double> entryT = sList.get(i);
                    if(!haveRecommendIdList.contains(entryT.getKey())){
                        idSet.add(entryT.getKey());
                    }
                }
                paperList = sqlDao.getAllPaper();
                for(int i = 0 ; i < paperList.size() ; i++){
                    if(!idSet.contains(paperList.get(i).getItem_ut())){
                        paperList.remove(i);
                        i--;
                    }
                }
                System.out.print(paperList.size());

                //根据多指标推荐结果重新获取数据
                paperList = calComplexIndex(paperList, p);
                p.setSimilarity(0.25);

                session.setAttribute("recommendParam_" + secMacAddr, p);
                session.setAttribute("haveRecommendIdList_" + secMacAddr, haveRecommendIdList);
                session.setAttribute("mayLoveIdList_" + secMacAddr, mayLoveIdList);
            }
        }else{
            paperList = sqlDao.getRandom(5);
        }
        return paperList;
    }

    public void calculateSimilarity(HttpSession session, String secMacAddr){
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
            session.setAttribute("simMatrix_" + secMacAddr, simMatric);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Paper> getMayLove(HttpSession session, String secMacAddr){
        List<Paper> paperList;
        Set<String> mayLoveIdList = (HashSet<String>)session.getAttribute("mayLoveIdList_" + secMacAddr);
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