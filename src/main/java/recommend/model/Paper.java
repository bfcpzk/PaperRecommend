package recommend.model;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
public class Paper{

    private String item_ut;
    private String article_title;
    private String document_type;
    private String meeting_abstract_number;
    private String abs;
    private String beginning_page;
    private String ending_page;
    private int page_count;
    private String item_t9;
    private int cited_count;
    private String paper_id;
    private String item_ui;
    private String full_source_title;
    private String volume;
    private String issue;
    private String publication_type;
    private int publication_year;
    private String publication_date;

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }


    public String getPublication_type() {
        return publication_type;
    }

    public void setPublication_type(String publication_type) {
        this.publication_type = publication_type;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getFull_source_title() {
        return full_source_title;
    }

    public void setFull_source_title(String full_source_title) {
        this.full_source_title = full_source_title;
    }

    public String getItem_ui() {
        return item_ui;
    }

    public void setItem_ui(String item_ui) {
        this.item_ui = item_ui;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getItem_t9() {
        return item_t9;
    }

    public void setItem_t9(String item_t9) {
        this.item_t9 = item_t9;
    }

    public String getEnding_page() {
        return ending_page;
    }

    public void setEnding_page(String ending_page) {
        this.ending_page = ending_page;
    }

    public String getBeginning_page() {
        return beginning_page;
    }

    public void setBeginning_page(String beginning_page) {
        this.beginning_page = beginning_page;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getMeeting_abstract_number() {
        return meeting_abstract_number;
    }

    public void setMeeting_abstract_number(String meeting_abstract_number) {
        this.meeting_abstract_number = meeting_abstract_number;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getItem_ut() {
        return item_ut;
    }

    public void setItem_ut(String item_ut) {
        this.item_ut = item_ut;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getCited_count() {
        return cited_count;
    }

    public void setCited_count(int cited_count) {
        this.cited_count = cited_count;
    }

    public int getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }
}
