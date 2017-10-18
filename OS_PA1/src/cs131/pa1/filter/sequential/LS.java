package cs131.pa1.filter.sequential;


import java.io.*;
import java.util.*;


public class LS extends SequentialFilter {
	//list has no field, it only lists out all the files in the current working directory
	public void process() {
		//then use file open it
		File present = new File(SequentialREPL.currentWorkingDirectory);
		//put everything in a list using list()
		//then add all stuff to output
		String[] contents = present.list();
		for (int i = 0; i < contents.length - 1; i++) {
			this.output.offer(contents[i]);
		}
		this.output.offer(contents[contents.length - 1]);
	}
	
	public String processLine(String line) {
		return null;
	}
}
