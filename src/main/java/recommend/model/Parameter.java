package recommend.model;

/**
 * Created by zhaokangpan on 2016/12/25.
 */
public class Parameter {
    private double similarity = 0.25;
    private double time = 0.25;
    private double length = 0.25;
    private double cite = 0.25;

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getCite() {
        return cite;
    }

    public void setCite(double cite) {
        this.cite = cite;
    }
}
