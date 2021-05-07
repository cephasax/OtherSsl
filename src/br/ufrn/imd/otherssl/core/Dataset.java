package br.ufrn.imd.otherssl.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Dataset {

	private Instances instances;

	private String datasetName;
	private double totalWeight;

	private HashMap<String, Integer> positions;

	public Dataset() {

	}

	public Dataset(String pathAndDataSetName) throws Exception {
		instances = DataSource.read(pathAndDataSetName);
		instances.setClassIndex(instances.numAttributes() - 1);
		this.datasetName = instances.relationName();
		this.totalWeight = -1.0;

		this.positions = new HashMap<String, Integer>();
	}

	public Dataset(Instances instances) {
		this.instances = new Instances(instances);
		this.datasetName = instances.relationName();
		this.totalWeight = -1.0;

		this.positions = new HashMap<String, Integer>();
	}

	public Dataset(Dataset dataset) {
		this.instances = new Instances(dataset.getInstances());
		this.datasetName = instances.relationName();
		this.totalWeight = -1.0;

		this.positions = new HashMap<String, Integer>();
	}

	public void shuffleInstances(int seed) {
		this.instances.randomize(new Random(seed));

	}

	public void addInstance(Instance instance) {
		Instance a = instance;
		this.instances.add(a);
	}

	public void addLabelledInstance(Instance instance) {
		Instance a = instance;
		this.instances.add(a);
	}

	public void clearInstances() {
		this.instances.clear();
		this.totalWeight = 0.0;
		this.positions = new HashMap<String, Integer>();
	}

	public void increaseTotalWeight(double value) {
		this.totalWeight += value;
	}

	public void decreaseTotalWeight(double value) {
		this.totalWeight -= value;
	}

	public String getMyInstancesSummary() {

		StringBuilder sb = new StringBuilder();
		sb.append(
				"id; [        instance        ]: weight; instanceClass; -> result: {agreement per class}; bestClass; bestResult\n");
		return sb.toString();
	}

	// STATIC METHODS
	public static ArrayList<Dataset> splitDataset(Dataset dataset, int numberOfParts) {

		ArrayList<Dataset> splitedDataset = new ArrayList<Dataset>();
		ArrayList<Instance> myData = new ArrayList<Instance>();
		ArrayList<Instance> part = new ArrayList<Instance>();

		int size = dataset.getInstances().size() / numberOfParts;

		for (Instance i : dataset.getInstances()) {
			myData.add(i);
		}

		int i = 0;
		int control = 0;

		for (i = 0; i < myData.size(); i++) {
			part.add(myData.get(i));

			if (part.size() == size) {
				Dataset d = new Dataset();
				d.setDatasetName(dataset.getInstances().relationName());
				d.setInstances(new Instances(dataset.getInstances()));
				d.getInstances().clear();
				d.getInstances().addAll(part);

				splitedDataset.add(d);
				part = new ArrayList<Instance>();

				control = i;
			}
		}
		int x = 0;

		while (control < (myData.size() - 1)) {
			splitedDataset.get(x).addInstance(myData.get(control));
			control++;
			x++;
			if (x == (splitedDataset.size() - 1)) {
				x = 0;
			}
		}

		return splitedDataset;

	}

	public static Dataset joinDatasets(ArrayList<Dataset> folds) {

		Dataset d = new Dataset();
		d.setDatasetName(folds.get(0).getInstances().relationName());
		d.setInstances(new Instances(folds.get(0).getInstances()));
		d.getInstances().clear();

		for (Dataset dd : folds) {
			for (Instance i : dd.getInstances()) {
				d.addInstance(i);
			}
		}

		return d;

	}

	// GETTERS AND SETTERS

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Instances getInstances() {
		return instances;
	}

	public void setInstances(Instances instances) {
		this.instances = instances;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public HashMap<String, Integer> getPositions() {
		return positions;
	}

	public void setPositions(HashMap<String, Integer> positions) {
		this.positions = positions;
	}

}
