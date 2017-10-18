package cs131.pa1.filter.concurrent;

import java.util.concurrent.TimeUnit;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	@Override
	public void concurrentProcess() {
		try {
			this.processLine(this.input.poll(500, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String processLine(String line) {
		if (line != null) System.out.println(line);
		return null;
	}
}
