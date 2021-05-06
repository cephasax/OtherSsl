package br.ufrn.imd.otherssl.workers;

import java.io.File;
import java.util.Random;

import br.ufrn.imd.otherssl.core.Dataset;
import weka.classifiers.CollectiveEvaluation;
import weka.classifiers.collective.functions.LLGC;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Teste {

	public static void main(String[] args) throws Exception {

		String base = new String("src/main/Abalone.arff");

		File f = new File(base);

		// load data, set class attribute to last
		Instances data = DataSource.read(f.getAbsolutePath());;		
		data.setClassIndex(data.numAttributes() - 1);
		
		Dataset d = new Dataset(data);
		
		// configure classifier
		LLGC llgc = new LLGC();
		llgc.setSeed(10);
		
		// cross-validate classifier
		CollectiveEvaluation eval = new CollectiveEvaluation(data);
		eval.crossValidateModel(llgc, d.getInstances(), 10, new Random(17));

		// output evaluation results
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toSummaryString());
		System.out.println(eval.toMatrixString());
	}
}