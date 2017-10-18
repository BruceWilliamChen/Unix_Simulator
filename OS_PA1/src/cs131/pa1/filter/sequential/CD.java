package cs131.pa1.filter.sequential;

import java.util.*;

public class CD extends SequentialFilter {
	String path;
	
	public CD(String path) {
		this.path = path;
	}
	
	@Override
	public void process() {
		if (path.equals(".")) {
			SequentialREPL.currentWorkingDirectory = SequentialREPL.currentWorkingDirectory;
		} else if (path.equals("..")) {
			int indexOfBound = SequentialREPL.currentWorkingDirectory.lastIndexOf(FILE_SEPARATOR);
			SequentialREPL.currentWorkingDirectory = SequentialREPL.currentWorkingDirectory.substring(0, indexOfBound);
		} else {
			//if not . or ..
			SequentialREPL.currentWorkingDirectory = SequentialREPL.currentWorkingDirectory + FILE_SEPARATOR + this.path;
		}
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
}
