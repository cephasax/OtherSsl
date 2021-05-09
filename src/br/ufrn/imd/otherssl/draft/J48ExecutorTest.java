package br.ufrn.imd.otherssl.draft;

import java.util.ArrayList;

import br.ufrn.imd.otherssl.core.CollectiveResult;
import br.ufrn.imd.otherssl.core.Dataset;
import br.ufrn.imd.otherssl.core.FoldResult;
import br.ufrn.imd.otherssl.core.Measures;
import br.ufrn.imd.otherssl.core.OutputWriter;
import br.ufrn.imd.otherssl.core.Prediction;
import weka.classifiers.collective.CollectiveClassifier;
import weka.classifiers.collective.functions.LLGC;
import weka.classifiers.collective.meta.YATSI;
import weka.classifiers.trees.J48;
import weka.core.Instance;

public class J48ExecutorTest {

	public static ArrayList<Dataset> datasets;
	public static int numFolds = 10;
	public static ArrayList<Dataset> folds;
	public static int seed;

	public static OutputWriter llgcOw;
	public static String llgcOwBasePath = "src/main/resources/results/";

	public static void main(String[] args) throws Exception {

		ArrayList<String> versions = new ArrayList<String>();
		//versions.add("YATSI");
		//versions.add("LLGC");
		versions.add("J48_Training_90");
		datasets = new ArrayList<Dataset>();
		folds = new ArrayList<Dataset>();
		seed = 19;

		populateDatasets();
		for (String s : versions) {
			for (Dataset d : datasets) {
				run(d, s);
			}
		}

	}

	public static void run(Dataset dataset, String sslVersion) throws Exception {

		System.out.println("Init " + sslVersion + " over " + dataset.getDatasetName() + " dataset");

		llgcOw = new OutputWriter(llgcOwBasePath + sslVersion + "_" + dataset.getDatasetName());

		dataset.shuffleInstances(seed);

		ArrayList<Dataset> datasets = new ArrayList<Dataset>();
		datasets = Dataset.splitDataset(dataset, 10);

		ArrayList<Dataset> foldsForTest = new ArrayList<Dataset>();

		CollectiveResult collectiveResult = new CollectiveResult(10, dataset.getDatasetName(), sslVersion);

		Dataset train = new Dataset();
		Dataset test = new Dataset();
		Dataset valid = new Dataset();

		System.out.print("CURRENT FOLD ... ");
		// separate test and validation
		for (int i = 0; i < numFolds; i++) {

			System.out.print(i + 1);
			System.out.print(" ");
			valid = new Dataset(datasets.get(i));
			for (int j = 0; j < 10; j++) {
				if (i != j) {
					foldsForTest.add(datasets.get(j));
				}
			}

			// create labelled (train) and unlabelled (Test) sets by
			Dataset ddd = Dataset.joinDatasets(foldsForTest);
			ddd.getInstances().stratify(10);
			test = new Dataset(ddd.getInstances().testCV(10, 0));
			train = new Dataset(ddd.getInstances().trainCV(10, 0));

			// for Weka collective classifiers (e.g LLGC, YATSI),
			// the validation set should bt joined with unlabelled set(test)
			//for (Instance ii : valid.getInstances()) {
			//	test.addInstance(ii);
			//}

			if (i == 0) {
				collectiveResult.setBegin(System.currentTimeMillis());
			}

			// configure collective classifier (LLGC or YATSI)
			//CollectiveClassifier classifier = getSslClassifier(sslVersion);

			// build classifier
			//classifier.buildClassifier(train.getInstances(), test.getInstances());

			J48 j48 = new J48();
			try {
				j48.setOptions(weka.core.Utils.splitOptions("-C 0.05 -M 2"));
				j48.buildClassifier(train.getInstances());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Prediction pred = new Prediction(valid);

			for (Instance j : valid.getInstances()) {
				pred.addPrediction(j.classValue(), j48.classifyInstance(j));
			}
			pred.buildMetrics();

			Measures measures = new Measures(pred);

			FoldResult foldResult = new FoldResult();
			foldResult.setAccuracy(measures.getAccuracy());
			foldResult.setError(measures.getError());
			foldResult.setfMeasure(measures.getFmeasureMean());
			foldResult.setPrecision(measures.getPrecisionMean());
			foldResult.setRecall(measures.getRecallMean());

			collectiveResult.addFoldResult(foldResult);

			foldsForTest = new ArrayList<Dataset>();
		}
		System.out.println();
		collectiveResult.setEnd(System.currentTimeMillis());

		collectiveResult.calcAverageResult();

		llgcOw.logGeneralDetails(dataset.getDatasetName());

		// output evaluation results
		llgcOw.addContentLine(collectiveResult.getResult());

		llgcOw.printContent();
		llgcOw.writeInFile();

		llgcOw.saveAndClose();
	}

	public static void populateDatasetsTest() {
		String basePath = new String("src/main/resources/datasets/test/");

		ArrayList<String> sources = new ArrayList<String>();
		sources.add("Iris.arff");
		sources.add("Abalone.arff");

		for (String s : sources) {
			Dataset d;
			try {
				d = new Dataset(basePath + s);
				datasets.add(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void populateDatasets() {
		String basePath = new String("src/main/resources/datasets/all/");

		ArrayList<String> sources = new ArrayList<String>();
		sources.add("Abalone.arff");
		sources.add("Adult.arff");
		sources.add("Arrhythmia.arff");
		sources.add("Automobile.arff");
		sources.add("Btsc.arff");
		sources.add("Car.arff");
		sources.add("Cnae.arff");
		sources.add("Dermatology.arff");
		sources.add("Ecoli.arff");
		sources.add("Flags.arff");
		sources.add("GermanCredit.arff");
		sources.add("Glass.arff");
		sources.add("Haberman.arff");
		sources.add("HillValley.arff");
		sources.add("Ilpd.arff");
		sources.add("ImageSegmentation_norm.arff");
		sources.add("KrVsKp.arff");
		sources.add("Leukemia.arff");
		sources.add("Madelon.arff");
		sources.add("MammographicMass.arff");
		sources.add("MultipleFeaturesKarhunen.arff");
		sources.add("Mushroom.arff");
		sources.add("Musk.arff");
		sources.add("Nursery.arff");
		sources.add("OzoneLevelDetection.arff");
		sources.add("PenDigits.arff");
		sources.add("PhishingWebsite.arff");
		sources.add("Pima.arff");
		sources.add("PlanningRelax.arff");
		sources.add("Secom.arff");
		sources.add("Seeds.arff");
		sources.add("Semeion.arff");
		sources.add("SolarFlare.arff");
		sources.add("SolarFlare1.arff");
		sources.add("Sonar.arff");
		sources.add("SpectfHeart.arff");
		sources.add("TicTacToeEndgame.arff");
		sources.add("Twonorm.arff");
		sources.add("Vehicle.arff");
		sources.add("Waveform.arff");
		sources.add("Wilt.arff");
		sources.add("Wine.arff");
		sources.add("Yeast.arff");

		for (String s : sources) {
			Dataset d;
			try {
				d = new Dataset(basePath + s);
				datasets.add(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static CollectiveClassifier getYatsi() {
		YATSI yatsi = new YATSI();
		yatsi.setKNN(10);
		yatsi.setNoWeights(true);
		return yatsi;
	}

	private static CollectiveClassifier getLlgc() {
		LLGC llgc = new LLGC();
		llgc.setSeed(10);
		return llgc;
	}

	private static CollectiveClassifier getSslClassifier(String version) {
		// configure classifier
		if (version.equals("LLGC")) {
			return getLlgc();
		} else if (version.equals("YATSI")) {
			return getYatsi();
		} else {
			return null;
		}
	}

}
