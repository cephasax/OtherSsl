package br.ufrn.imd.otherssl.draft;

import java.io.File;
import java.util.ArrayList;

import br.ufrn.imd.otherssl.core.CollectiveResult;
import br.ufrn.imd.otherssl.core.Dataset;
import br.ufrn.imd.otherssl.core.FoldResult;
import br.ufrn.imd.otherssl.core.Measures;
import br.ufrn.imd.otherssl.core.Prediction;
import weka.classifiers.collective.functions.LLGC;
import weka.classifiers.collective.meta.YATSI;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Teste2 {

	public static void main(String[] args) throws Exception {

		
		
		String base = new String("src/main/resources/datasets/test/Iris.arff");

		File f = new File(base);

		// load data, set class attribute to last
		Instances d = DataSource.read(f.getAbsolutePath());
		d.setClassIndex(d.numAttributes() - 1);

		Dataset data = new Dataset(d);
		data.shuffleInstances(19);

		ArrayList<Dataset> datasets = new ArrayList<Dataset>();
		datasets = Dataset.splitDataset(data, 10);

		ArrayList<Dataset> foldsForTest = new ArrayList<Dataset>();

		Dataset train = new Dataset();
		Dataset test = new Dataset();
		Dataset valid = new Dataset();

		CollectiveResult collectiveResult = new CollectiveResult(10, data.getDatasetName(), "collective_test");
		
		// separate test and validation
		for (int i = 0; i < 10; i++) {
			valid = new Dataset(datasets.get(i));
			for (int j = 0; j < 10; j++) {
				if (i != j) {
					foldsForTest.add(datasets.get(j));

				}
			}

			// create labelled (train) and unlabelled (Test) sets by 
			Dataset ddd = Dataset.joinDatasets(foldsForTest);
			ddd.getInstances().stratify(10);
			train = new Dataset(ddd.getInstances().testCV(10, 0));
			test = new Dataset(ddd.getInstances().trainCV(10, 0));

			// for Weka collective classifiers (e.g LLGC, YATSI),
			// the validation set should bt joined with unlabelled set(test)
			for (Instance ii : valid.getInstances()) {
				test.addInstance(ii);
			}
			
			if(i == 0) {
				collectiveResult.setBegin(System.currentTimeMillis());
			}
			
			// configure classifier
			LLGC llgc = new LLGC();
			llgc.setSeed(10);

			// build classifier
			llgc.buildClassifier(train.getInstances(), test.getInstances());

			Prediction pred = new Prediction(valid);

			for (Instance j : valid.getInstances()) {
				pred.addPrediction(j.classValue(), llgc.classifyInstance(j));
			}
			pred.buildMetrics();

			System.out.println(pred.getMatrix());
			
			Measures measures = new Measures(pred);

			FoldResult foldResult = new FoldResult();
			foldResult.setAccuracy(measures.getAccuracy());
			foldResult.setError(measures.getError());
			foldResult.setfMeasure(measures.getFmeasureMean());
			foldResult.setPrecision(measures.getPrecisionMean());
			foldResult.setRecall(measures.getRecallMean());

			collectiveResult.addFoldResult(foldResult);

		}
		collectiveResult.setEnd(System.currentTimeMillis());
		
		collectiveResult.calcAverageResult();
		collectiveResult.showResult();

	}

}