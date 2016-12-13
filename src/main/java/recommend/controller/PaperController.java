package recommend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import recommend.model.Keywords;
import recommend.model.Paper;
import recommend.model.Row;
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
        List<Keywords> klist = new ArrayList<Keywords>();
        List<Row> rlist = new ArrayList<Row>();
        try {
            klist = paperService.getKeywordsList();
            for(int j = 0 ; j < klist.size() ; j++){
                if(j % 6 == 0){
                    Row r = new Row();
                    r.setKlist(klist.subList(j, j + 6));
                    rlist.add(r);
                    if(j == 30){
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mav.addObject("rowSize", 6);
        mav.addObject("klist", rlist);
        return mav;
    }

    @RequestMapping("/recommendBasedOnKeywords.do")
    public ModelAndView recommendBasedOnKeywords(String keywordlist, HttpSession session){
        ModelAndView mav = new ModelAndView("recommendlist");
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
    public ModelAndView recommendBasedOnScoreLda(HttpSession session, String iter, String score){
        ModelAndView mav = new ModelAndView("recommendlist");
        List<Paper> resList;
        try{
            resList = paperService.recommendBasedOnScoreLda(session,  score);
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
