package recommend.model;

import java.util.Vector;

/**
 * Created by zhaokangpan on 2016/12/8.
 */
public class Topic {
    private String paper_id;
    private String topic_dist;
    private Vector<Double> td;

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getTopic_dist() {
        return topic_dist;
    }

    public void setTopic_dist(String topic_dist) {
        this.topic_dist = topic_dist;
    }

    public Vector<Double> getTd() {
        return td;
    }

    public void setTd(Vector<Double> td) {
        this.td = td;
    }
}
