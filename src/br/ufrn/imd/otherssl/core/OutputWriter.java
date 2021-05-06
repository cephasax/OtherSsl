package br.ufrn.imd.otherssl.core;

import java.io.IOException;

public class OutputWriter extends FileOutputWriter {
	
	public OutputWriter(String partOfFileName) throws IOException {
		super(partOfFileName);
	}
	
	public void logGeneralDetails(String datasetName) throws IOException{
		addContentLine("");
		addContentLine("================================================================================================");        
		addContentLine("DATASET: " + datasetName);
		addContentLine("================================================================================================");
		
		writeInFile();
	}
	
	
	public void logDetailsAboutTime(long begin, long end) throws IOException{
		
		long d = (end - begin) / 1000;
		
		addContentLine("");
		addContentLine("TIME ELAPSED:\t" + NumberUtils.doubleToString(d) + " SECONDS");
		addContentLine("====================================================");
		
		writeInFile();
	}
	
	public void printLine(String string) {
		System.out.println(string);
	}
		
	public void printContent() {
		System.out.println(toText.toString());
	}
	
	public void outputDatasetInfo(String dataset){
		addContentLine("");
		addContentLine("================================================================================================");
		addContentLine("Dataset: " + dataset);
		addContentLine("================================================================================================");
		printContent();
	}
	
	

}
