package main;

import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.CollectiveEvaluation;
import weka.classifiers.collective.functions.LLGC;

public class LlgcExecutor {

	public static ArrayList<Dataset> datasets;
	public static int numFolds = 10;
	public static ArrayList<Dataset> folds;
	public static int seed;

	public static String llgcVersion = "LLGC";

	public static LlgcOutputWriter llgcOw;
	public static String llgcOwBasePath = "src/main/resources/results/";

	public static void main(String[] args) throws Exception {

		datasets = new ArrayList<Dataset>();
		folds = new ArrayList<Dataset>();
		seed = 19;

		populateDatasetsTest();

		for (Dataset d : datasets) {
			run(d, llgcVersion);
		}
	}

	public static void run(Dataset dataset, String llgcVersion) throws Exception {

		System.out.println("Init LLGC over " + dataset.getDatasetName() + " dataset");

		llgcOw = new LlgcOutputWriter(llgcOwBasePath + llgcVersion + "_" + dataset.getDatasetName());

		dataset.shuffleInstances(seed);

		long begin = System.currentTimeMillis();

		// configure classifier
		LLGC llgc = new LLGC();
		llgc.setSeed(seed);

		// cross-validate classifier
		CollectiveEvaluation eval = new CollectiveEvaluation(dataset.getInstances());
		eval.crossValidateModel(llgc, dataset.getInstances(), 10, new Random(157));

		long end = System.currentTimeMillis();

		llgcOw.logGeneralDetails(dataset.getDatasetName());
		llgcOw.logDetailsAboutTime(begin, end);

		// output evaluation results
		llgcOw.addContentLine(eval.toClassDetailsString());
		llgcOw.addContentLine(eval.toSummaryString());
		llgcOw.addContentLine(eval.toMatrixString());

		llgcOw.printContent();
		llgcOw.writeInFile();

		llgcOw.saveAndClose();
		System.out.println();
	}

	public static void populateDatasetsTest() {
		String basePath = new String("src/main/resources/datasets/test/");

		ArrayList<String> sources = new ArrayList<String>();
		sources.add("Iris.arff");
		// sources.add("Abalone.arff");

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

}
