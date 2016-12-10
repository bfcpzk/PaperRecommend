package recommend.service.lda;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ldatest {

	public static void main(String[] args) throws IOException,SQLException {
		// TODO Auto-generated method stub
		Jdbc_Util db = new Jdbc_Util();

		// 1. Load corpus from disk
		Corpus corpus = Corpus.load_mysql(db);
		//Corpus corpus = Corpus.load("data/mini");
		// 2. Create a LDA sampler
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
		// 3. Train it
		ldaGibbsSampler.gibbs(10);
		// 4. The phi matrix is a LDA model, you can use LdaUtil to explain it.
		double[][] phi = ldaGibbsSampler.getPhi();
		Map<String, Double>[] topicMap = LdaUtil.translate(phi, corpus.getVocabulary(), 10);
		LdaUtil.explain(topicMap);
		double[][] theta = ldaGibbsSampler.getTheta();

		corpus.saveTopic(db, theta, corpus);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("userFeatures.txt")), "utf-8"));

		for(int i = 0 ; i < theta.length ; i++){
			//bw.write(l.get(i) + ",");
			for(int j = 0 ; j < theta[i].length - 1 ; j++){
				bw.write(theta[i][j] + ",");
			}
			bw.write(theta[i][theta[i].length - 1] + "\n");
		}
		bw.close();
	}
}