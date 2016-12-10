package recommend.dao.imp;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import recommend.dao.SqlDao;
import recommend.model.Keywords;
import recommend.model.Paper;
import recommend.model.Topic;

import java.util.List;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
public class SqlDaoImp implements SqlDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Keywords> selectArticleIdBasedOnKeywords(String wordList){
        List<Keywords> result=null;
        String sql = "select * from paper_keywords where keywords in (" + wordList + ")";
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Keywords.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Paper> selectPaperByIdList(String pidList){
        List<Paper> result=null;
        String sql = "select * from paper_info where item_ut in (" + pidList + ")";
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Paper.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public List<Keywords> getKeywordsList(){
        List<Keywords> result=null;
        String sql = "select * from paper_keywords";
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Keywords.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Topic> getAllArticleTopic(){
        List<Topic> result=null;
        String sql = "select * from paper_topic";
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Topic.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Paper> getSupplement(int len, String pidList){
        List<Paper> result=null;
        String sql = "SELECT * FROM paper_info where item_ut not in (" + pidList + ") ORDER BY RAND() LIMIT " + len;
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Paper.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Paper> getRandom(int len){
        List<Paper> result=null;
        String sql = "SELECT * FROM paper_info ORDER BY RAND() LIMIT " + len;
        try{
            result = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Paper.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
