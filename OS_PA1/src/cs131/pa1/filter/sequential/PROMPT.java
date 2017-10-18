package cs131.pa1.filter.sequential;

import cs131.pa1.filter.*;

import java.io.*;

public class PROMPT extends SequentialFilter {
	String filepath;
	
	/*
	 * the constructor
	 */
	public PROMPT(String filepath) {
		String fullpath = SequentialREPL.currentWorkingDirectory + Filter.FILE_SEPARATOR + filepath;
		this.filepath = fullpath;
	}
	
	
	@Override
	public void process() {
		File file = new File(filepath);
		//if it exists then delete the stuff
		if (file.exists()) file.delete();
		try {
			PrintWriter pw = new PrintWriter(filepath);
			//then use a loop to catch stuff in the input and write them out
			while (!this.input.isEmpty()) {
				String line = input.poll();
				if (line != null) pw.println(line);
			}
			//then check if it is done, if yes then close the print writer
			if (this.isDone()) pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
}
