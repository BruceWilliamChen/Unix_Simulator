package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.*;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static boolean ifNeedsBackground;
	static List<BackgroundCommand> backgroundcommands = new ArrayList<BackgroundCommand>();
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if (!command.trim().equals("")) {
				//if it is empty then call the static method of concurrent command builder class
				List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				//now determine if it needs to be run on the background
				boolean runBackground = background(command);
				
				//then if the filters is not null, start to iterate over it and do something
				if (filters != null) {
					//possible threads
					List<Thread> threads = new ArrayList<Thread>();
					for (int i = 0; i < filters.size(); i++) {
						Runnable curr = filters.get(i);
						//create new thread
						Thread thread = new Thread(curr, command);
						threads.add(thread);
						if (runBackground) backgroundcommands.add(new BackgroundCommand(command, thread));
						//then start the thread
						thread.start();
					}
					
					//then do something if the command is not running background
					if (!runBackground) {
						//make the thread point the last thread
						Thread thread = threads.get(threads.size() - 1);
						if (thread.isAlive()) {
							try {
								thread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
			  	}
			} else if (command.trim().equals("repl_jobs")) { 
				//use a set to hold all non-duplicates
				Set<String> currs = new HashSet<String>();
				for (BackgroundCommand background: backgroundcommands) {
					if (background.getThread().isAlive()) currs.add(background.getCommand());
				}
				//then push all commands to a stack
				Stack<String> commandStack = new Stack<String>();
				for (String curr: currs) commandStack.push(curr);
				
				//then pop items out from the stack
				int index = 1;
				while (!commandStack.isEmpty()) {
					System.out.println("\t" + index + ". " + commandStack.pop());
					index++;
				}
				//then re-initialize the background commands list
				backgroundcommands = new ArrayList<BackgroundCommand>();
			}
	    }
		s.close();
		System.out.print(Message.GOODBYE);
    }
	
	public static boolean background(String command) {
		if (command.charAt(command.length() - 1) == '&') return true;
		return false;
	}
}
