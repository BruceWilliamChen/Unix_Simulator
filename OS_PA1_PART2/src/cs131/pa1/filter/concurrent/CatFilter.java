package cs131.pa1.filter.concurrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs131.pa1.filter.Message;

public class CatFilter extends ConcurrentFilter{
	private Scanner reader;
	
	//two variables to hold other possible values
	private int count, total;
	
	public CatFilter(String line) throws Exception {
		super();
		count = 0;
		
		//parsing the cat options
		String[] args = line.split(" ");
		String filename;
		//obviously incorrect number of parameters
		if(args.length == 1) {
			System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
			throw new Exception();
		} else if (args[1].charAt(0) == '-') {
			try {
				total = Integer.parseInt(args[1].substring(1));
			} catch (Exception e) {
				System.out.printf(Message.INVALID_PARAMETER.toString(), line);
				throw new Exception();
			}
			
			//then check the length of args
			if (args.length > 2) filename = args[2];
			else {
				System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
				throw new Exception();
			}
		} else {
			//then no option then just read whatever
			total = 10;
			filename = args[1];
		}
		try {
			reader = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.printf(Message.FILE_NOT_FOUND.toString(), line);
			throw new FileNotFoundException();
		}
	}

	public void process() throws InterruptedException {
		//milk strategy
		this.markFinished(false);
		while (count < total) {
			String cooked = this.processLine("whatever");
			if (cooked == null) break;
			this.output.put(cooked);
		}
		//when this is done
		reader.close();
		this.markFinished(true);
	}

	public String processLine(String line) {
		if(reader.hasNextLine()) {
			count++;
			return reader.nextLine();
		} else {
			return null;
		}
	}
}
