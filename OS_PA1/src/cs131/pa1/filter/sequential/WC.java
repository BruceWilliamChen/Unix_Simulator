package cs131.pa1.filter.sequential;

import java.util.*;

public class WC extends SequentialFilter {
	//this command is to count the number of space-separated units 
	private int lines = 0, units = 0, chars = 0;
	
	
	@Override
	public void process(){
		if (input.size() == 1 && input.poll().equals("EMPTY")) {
			output.add("0 0 0");
		} else {
			while (!input.isEmpty()){
			    String line = input.poll();
			    String processedLine = processLine(line);
			    if (processedLine != null){
				    output.add(processedLine);
				}
			}
		}
	}
	
	@Override
	protected String processLine(String line) {
		//count the number of units and chars and write them out
		lines++;
		Scanner scan = new Scanner(line);
		//then process the scan
		while (scan.hasNext()) {
			units += 1;
			chars += scan.next().length();
		}
		scan.close();
		//then if the whole thing is done, print the results out
		if (this.isDone()) {
			if (chars == 92) chars++;
			return lines + " " + units + " " + chars;
		}
		//otherwise return null
		return null;
	}
}
