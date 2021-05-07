package br.ufrn.imd.otherssl.workers;

import java.io.File;
import java.util.Random;

import weka.classifiers.CollectiveEvaluation;
import weka.classifiers.collective.meta.YATSI;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Teste3 {

	public static void main(String[] args) throws Exception {

		String base = new String("src/main/Iris.arff");

		File f = new File(base);

		// load data, set class attribute to last
		Instances data = DataSource.read(f.getAbsolutePath());;		
		data.setClassIndex(data.numAttributes() - 1);

		// configure classifier
		YATSI yatsi = new YATSI();
		yatsi.setKNN(10);
		yatsi.setNoWeights(true);

		// cross-validate classifier
		CollectiveEvaluation eval = new CollectiveEvaluation(data);
		eval.crossValidateModel(yatsi, data, 10, new Random(1));

		// output evaluation results
		System.out.println(eval.toSummaryString());
	}
}