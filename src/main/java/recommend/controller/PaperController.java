package recommend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import recommend.model.*;
import recommend.service.PaperService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
@Controller
public class PaperController {

    @Autowired
    private PaperService paperService;

    @RequestMapping("/toWelcomePaper.do")
    public ModelAndView toWelcomePaper(){
        ModelAndView mav = new ModelAndView("selectkeyword");
        //List<Keywords> kList;
        List<WordCount> wcList = new ArrayList<WordCount>();
        //List<Row> rList = new ArrayList<Row>();
        try {
            wcList = paperService.getWordCount();
            /*kList = paperService.getKeywordsList();
            for(int j = 0 ; j < kList.size() ; j++){
                if(j % 6 == 0){
                    Row r = new Row();
                    r.setKlist(kList.subList(j, j + 6));
                    rList.add(r);
                    if(j == 30){
                        break;
                    }
                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        mav.addObject("wcList", wcList);
       /* mav.addObject("rowSize", 6);
        mav.addObject("klist", rList);*/
        return mav;
    }

    @RequestMapping("/recommendBasedOnKeywords.do")
    public ModelAndView recommendBasedOnKeywords(String keywordlist, String time, String length, String cite, HttpSession session){
        ModelAndView mav = new ModelAndView("recommendlist");
        Parameter p = new Parameter();
        p.setTime(Double.parseDouble(time) + p.getTime());
        p.setLength(Double.parseDouble(length) + p.getLength());
        p.setCite(Double.parseDouble(cite) + p.getCite());
        session.setAttribute("recommendParam", p);
        System.out.println(keywordlist);
        String[] wordList = keywordlist.split("@");
        List<Paper> resList = new ArrayList<Paper>();
        try{
            resList = paperService.recommendBasedOnKeywords(wordList, session);
            paperService.calculateSimilarity(session);
        }catch(Exception e){
            e.printStackTrace();
        }
        mav.addObject("iter", 1);
        mav.addObject("paperList", resList);
        return mav;
    }

    @RequestMapping("/recommendBasedOnScoreLda.do")
    public ModelAndView recommendBasedOnScoreLda(HttpSession session, String time, String length, String cite, String iter, String score){
        ModelAndView mav = new ModelAndView("recommendlist");
        Parameter p = (Parameter)session.getAttribute("recommendParam");
        p.setTime(p.getTime() + Double.parseDouble(time));
        p.setLength(p.getLength() + Double.parseDouble(length));
        p.setCite(p.getCite() + Double.parseDouble(cite));
        List<Paper> resList;
        try{
            resList = paperService.recommendBasedOnScoreLda(session,  score, p);
            mav.addObject("iter", Integer.parseInt(iter) + 1);
            mav.addObject("paperList", resList);
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "/mayLoveList.do", method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody List<Paper> mayLoveList(HttpSession session){
        List<Paper> resList = new ArrayList<Paper>();
        try{
            resList = paperService.getMayLove(session);
            System.out.println("maylovelist:" + resList.size());
        }catch(Exception e){
            e.printStackTrace();
        }
        return resList;
    }

    @RequestMapping("/logOut.do")
    public ModelAndView logOut(HttpSession session){
        session.removeAttribute("mayLoveIdList");
        session.removeAttribute("haveRecommendIdList");
        session.removeAttribute("simMatrix");
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

}
