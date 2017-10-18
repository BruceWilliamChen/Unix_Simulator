package cs131.pa1.filter.concurrent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cs131.pa1.filter.Filter;


//this class should also implements Runnable to be able to go concurrent
public abstract class ConcurrentFilter extends Filter implements Runnable {
	//needs to implement some concurrent stuff
	//and to do this a blocking queue should come to play instead of the normal queue
	
	protected BlockingQueue<String> input;
	protected BlockingQueue<String> output;
	
	protected boolean ifFinished = true;
	
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				//if the output of this current filter is null,
				//then it means the output should just be an empty linked blocking queue
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	/*
	 * param, none
	 * return, the previous concurrent filter
	 */
	protected ConcurrentFilter getPrevConcurrentFilter() {
		//just return the prev if it is not null
		return this.prev == null? null : (ConcurrentFilter) this.prev;
	}
	
	/*
	 * return the next filter
	 */
	protected ConcurrentFilter getNextConcurrentFilter() {
		return this.next == null? null : (ConcurrentFilter) this.next;
	}
	
	@Override
	public void run() {
		//when this gets called the whole filter will be put in a new thread
		try {
		    this.process();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * param, none
	 * return, none
	 * this method now is not simply process the input queue
	 * it is instead 
	 */
	public void process() throws InterruptedException {
		//this little marker is like the note on the door to get the milk
		//first mark undone
		this.markFinished(false);
		//then do stuff when the previous filter has not done its job
		//and it cannot be null
		while (this.getPrevConcurrentFilter() != null && 
			   !this.getPrevConcurrentFilter().isDone()) this.concurrentProcess();
		//then finish the process for the current filter until input is all polled out
		while (!this.input.isEmpty()) this.concurrentProcess();
		//then mark done
		this.markFinished(true);
	}
	
	/*
	 * param, none
	 * return, none
	 * this method now is to then process the input and add them to the output
	 */
	protected void concurrentProcess() {
		//process this line by calling process line
		String cookedLine = null;
		try {
			cookedLine = processLine(input.poll(500, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//add it to the output if is not null
		try {
		    //has to use put() on the blocking queue
			if (cookedLine != null) this.output.put(cookedLine);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isDone() {
		return ifFinished;
	}
	
	public void markFinished(boolean ifFinished) {
		this.ifFinished = ifFinished;
	}
	
	protected abstract String processLine(String line);
	
}
