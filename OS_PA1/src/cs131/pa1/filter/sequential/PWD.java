package cs131.pa1.filter.sequential;

import java.util.*;

public class PWD extends SequentialFilter {
	//very simple class
	//only need to show the current working directory
	
	//it will just use the default constructor
	@Override
	public void process() {
		this.output = new LinkedList<String>();
		output.add(SequentialREPL.currentWorkingDirectory);
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
}
