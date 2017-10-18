package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;

public class GREP extends SequentialFilter {
	//this class is only to implement grep command
	private String keyElem;
	
	public GREP(String keyElem) {
		this.keyElem = keyElem;
	}
	
	@Override
	protected String processLine(String line) {
		//process each line to check if contains the key element
		if (line.contains(this.keyElem)) return line;
		//otherwise return null
		return null;
	}
}
