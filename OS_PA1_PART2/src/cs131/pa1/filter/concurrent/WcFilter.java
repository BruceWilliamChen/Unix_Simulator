package cs131.pa1.filter.concurrent;

import java.util.concurrent.TimeUnit;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	@Override
	public void process() throws InterruptedException {
		//mark unfinished
		this.markFinished(false);
		//do while the prevfilter is not null and its input is not done
		while (this.getPrevConcurrentFilter() != null &&
			  !this.getPrevConcurrentFilter().isDone()) {
			this.partOne();
		}
		//then keeping polling out
		while (!this.input.isEmpty()) this.partTwo();
		//then mark finish
		this.markFinished(true);
	}
	
	/*
	 * this method is to process the input until a line is processed to be null
	 */
	public void partOne() throws InterruptedException {
		try {
			String raw = this.input.poll(500, TimeUnit.MILLISECONDS);
			if (raw != null) {
				// if it is not null
				this.linecount++;
				String[] units = raw.split(" ");
				this.wordcount += units.length;
				String[] chars = raw.split("|");
				this.charcount += chars.length;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * this method is to return something
	 */
	public void partTwo() {
		try {
			String raw;
			raw = this.input.poll(500, TimeUnit.MILLISECONDS);
			if (raw == null) {
				this.output.put(linecount + " " + wordcount + " " + charcount);
				return;
			}
			if (raw.equals("")) {
				String[] units = raw.split(" ");
				this.wordcount += units.length;
				String[] chars = raw.split("|");
				this.charcount += chars.length;
				linecount++;
				this.output.put(linecount + " " + wordcount + " " + charcount);
				return;
			}
			linecount++;
			this.wordcount += raw.split(" ").length;
			this.charcount += raw.split("|").length;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String processLine(String line) {
		return null;
	}
}
