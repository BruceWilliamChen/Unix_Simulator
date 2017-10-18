package cs131.pa1.filter.sequential;

import java.util.*;

public class UNIQ extends SequentialFilter {
	//use a set to hold a non-duplicate status of the contetns
	HashSet<String> holder = new HashSet<String>();
	
	@Override
	protected String processLine(String line) {
		//if contained then add and return
		if (holder.contains(line)) return null;
		holder.add(line);
		return line;
	}
}
