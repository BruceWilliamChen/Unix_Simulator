package cs131.pa1.filter.concurrent;

public class BackgroundCommand {
	/*
	 * this class is to indicate an object that contains a string command
	 * and the thread it runs on
	 */
	private String command;
	private Thread thread;
	
	public BackgroundCommand(String command, Thread thread) {
		this.command = command;
		this.thread = thread;
	}
	
	public Thread getThread() {
		return this.thread;
	}
	
	public String getCommand() {
		return this.command;
	}
}
