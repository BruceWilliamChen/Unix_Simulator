package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		try {
			//mark start and done
			this.markFinished(false);
			this.output.put(processLine("whatever"));
			this.markFinished(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
