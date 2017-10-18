package cs131.pa1.filter.sequential;

import java.util.*;
import java.io.*;

public class CAT extends SequentialFilter {
	private String[] files;
	
	public CAT(String[] files) {
		this.files = files;
	}
	
	@Override
	public void process() {
		for (String file: files) {
			try {
			    File f = new File(file);
			    //then try to do something with it
			    //remember to throw exception
				Scanner scan = new Scanner(f);
				//then read every line
				//and add them to the output queue
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					this.output.offer(line);
				}
				scan.close();
				if (this.output.isEmpty()) this.output.offer("EMPTY");
			} catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String processLine(String line) {
		return null;
	}
}
