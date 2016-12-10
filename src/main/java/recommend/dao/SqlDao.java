package recommend.dao;

import recommend.model.Keywords;
import recommend.model.Paper;
import recommend.model.Topic;

import java.util.List;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
public interface SqlDao {

    public List<Keywords> selectArticleIdBasedOnKeywords(String wordList);

    public List<Paper> selectPaperByIdList(String pidList);

    public List<Keywords> getKeywordsList();

    public List<Topic> getAllArticleTopic();

    public List<Paper> getSupplement(int len, String pidList);

    public List<Paper> getRandom(int len);
}
