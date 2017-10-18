package cs131.pa1.filter.concurrent;
import java.io.File;

public class LsFilter extends ConcurrentFilter{
	int counter;
	File folder;
	File[] flist;
	
	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}
	
	@Override
	public void process() {
		//this method needs some update
		
		//first mark unfinished
		try {
			this.markFinished(false);
			while (counter < flist.length) this.output.put(processLine("whatever")); //processLine needs no param here
			//then mark done
			this.markFinished(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}
}
