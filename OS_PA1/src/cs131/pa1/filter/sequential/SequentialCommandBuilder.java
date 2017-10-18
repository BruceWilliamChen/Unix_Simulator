package cs131.pa1.filter.sequential;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import cs131.pa1.filter.*;

public class SequentialCommandBuilder {
	//two fields
	//the list that links all valid commands (filters)
	private Queue<SequentialFilter> validCommands;
	String commands;
	/*
	 * constructor
	 */
	public SequentialCommandBuilder(String commands) {
		this.validCommands = new LinkedList<SequentialFilter>();
		this.commands = commands;
	}
	
	public void run() {
		if (validate(commands)) {
			connectCommands();
			process();
		}
	}
	
	/*
	 * param, the list of filters
	 * return, void, a mutator
	 * this method builds the flow of all commands in the list
	 * make them work like take input as output that sort of shit
	 */
	private void connectCommands() {
		//use an iterator to traverse the list
		Iterator<SequentialFilter> itr = this.validCommands.iterator();
		//if not empty then first set the prev to be the first one
		SequentialFilter prevFilter = null;
		if (itr.hasNext()) {
			prevFilter = itr.next();
		    while (itr.hasNext()) {
			//first get the current filter
		    	SequentialFilter currFilter = itr.next();
			//then check if the current filter is null or not
		    	if (currFilter != null) currFilter.setPrevFilter(prevFilter);
		    	if (prevFilter != null) prevFilter.setNextFilter(currFilter);
			//then move advance set the prev to be the current
		    	prevFilter = currFilter;
		    }
		}
	}
	
	
	private void process() {
		SequentialFilter curr = null;
		//then while loop to process all commands
		//all but the last one
		while (validCommands.size() > 1) {
			curr = validCommands.poll();
			//then process the curr filter using the process method in its own class
			curr.process();
		}
		//only the last command gets to print the output on the screen
		curr = validCommands.poll();
		if (curr != null ) {
			curr.output = new LinkedList<String>();
			curr.process();
			if (curr.output != null && !curr.output.isEmpty()) {
				while (!curr.output.isEmpty()) System.out.println(curr.output.poll());
			}
		}
		
		//check if the output is empty, is yes then directly return and print nothing

		//then print stuff on the screen
	}
	
	
	/*
	 * param: input command in string format
	 * return: boolean value, if the command is a valid command
	 * des: method that checks if the command is valid or not; a static method
	 */
	private boolean validate(String command) {
		if (command == null) {
			System.out.println(Message.COMMAND_NOT_FOUND.toString());
			return false;
		}
		//break the commands down into string array
		String[] possibleCommands = {"pwd", "cat", "ls", "grep", "wc", "uniq", ">", "cd"};
		
		//then break down the input
		//replace all single > with |>| so it will be processed properly
		command = command.replaceAll(">", "\\|>");
		
		//then split the commands by |
		String[] temp = command.split("\\|");
		
		//then set prev to be null, this is used to determine the behaviour of the next
		//command, to see what to do
		String prevCommand = "";
		
		int j = 0;
		if (temp[0].equals("")) j = 1;
		for (int i = j ; i < temp.length; i++) {
			String elem = temp[i];
			//for each command, put its command content and input to a holder, better be a queue
			Queue<String> cmdQue = new LinkedList<String>();
			//then start to read all stuff in the elem string, which is cmd + input
			Scanner scan = new Scanner(elem);
			while (scan.hasNext()) cmdQue.offer(scan.next().trim());
			//then close the scanner
			scan.close();

			//if not one of the valid commands, then print out error message
			//and return false
			
			SequentialFilter currFilter = null;
			//the first elem in the queue is the command itself
			String currCommand = cmdQue.poll();
			
			if (currCommand.equals("cd") && temp.length > 1 && i == 0) {
				System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals(">") && i == 0) {
				System.out.print(Message.REQUIRES_INPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals(">") && i != temp.length - 1) {
				System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("pwd") && !prevCommand.equals("")) {
				System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(elem));
				return false;
			}
			
			if (!Arrays.asList(possibleCommands).contains(currCommand)) {
				System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("cat") && cmdQue.isEmpty() && prevCommand.length() == 0) {
				System.out.print(Message.REQUIRES_PARAMETER.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("ls") && prevCommand.equals("cat")) {
				System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals(">") && cmdQue.isEmpty()) {
				System.out.print(Message.REQUIRES_PARAMETER.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("cat") && !cmdQue.isEmpty() && prevCommand.equals("pwd")) {
				System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("grep") && prevCommand.length() == 0) {
				System.out.print(Message.REQUIRES_INPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("grep") || currCommand.equals(">")) {
				if (cmdQue.isEmpty() || prevCommand.equals("cd") || prevCommand.equals(">")) {
					//then print out error message
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter(elem));
					return false;
				}
			}
			if (currCommand.equals("cd") && !cmdQue.isEmpty() && prevCommand.equals("cat")) {
				System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(elem));
				return false;
			}
			if (currCommand.equals("wc") || currCommand.equals("uniq")) {
				if (prevCommand.equals("cd") || prevCommand.equals(">")) {
					System.out.print(Message.REQUIRES_INPUT.with_parameter(elem));
					return false;
				}
			}
			if (currCommand.equals("wc") || currCommand.equals(">")) {
				if (validCommands.isEmpty()) {
					System.out.print(Message.REQUIRES_INPUT.with_parameter(elem));
					return false;
				}
			}
			currFilter = createFilter(prevCommand, currCommand, cmdQue, elem);
			//then if nothing above happens, we are good to go
			//set value for the current command to be a new filter
			//else advance the pointer
			prevCommand = currCommand;
			//then add the processed curr filter to filter queue
			validCommands.add(currFilter);
		}
		//must be all valid commands to be a completely valid command string
		return true;
	}
	
	/*
	 * param: multiple
	 * return a newly created filter
	 */
	private SequentialFilter createFilter(String prevCommand, String command, Queue<String> cmdQue, String rawCommand) {
		switch(command) {
			case "pwd":
				//if it is a valid pwd command, then create a new PWD object, which is a filter
				return new PWD();
			case "ls":
				return new LS();
			case "grep":
				//must have input as well
				return new GREP(cmdQue.poll());
			case "wc":
				return new WC();
			case "uniq":
				return new UNIQ();
			case ">":
				return new PROMPT(cmdQue.poll());
			//things get tricky for "cd" and "cat", because they have to have output
			case "cd":
				return ifValidCD(cmdQue, rawCommand);
			case "cat":
				return ifValidCAT(prevCommand, cmdQue, rawCommand);
		}
		//if none of above satisfied then return null
		return null;
	}
	
	/*
	 * to special check for cd command
	 */
	private SequentialFilter ifValidCD(Queue<String> cmdQue, String rawCommand) {
		//if the que is already empty, then means it is already empty
		if (cmdQue.isEmpty()) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(rawCommand));
			return null;
		}
		String directory = cmdQue.poll();
		//then means just enter the previous root dir
		if (directory.equals(".." ) || directory.equals(".")) {
			return new CD(directory);
		}
		String path = SequentialREPL.currentWorkingDirectory + System.getProperty("file.separator") + directory;
		if (Files.isDirectory(Paths.get(path))) {
			//means the path is valid
			return new CD(directory);
		} else System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(rawCommand));
		return null;
	}
	
	/*
	 * to special check for cat command, similar to cd
	 * must be some valid inputs
	 */
	private SequentialFilter ifValidCAT(String prevCommand, Queue<String> cmdQue, String rawCommand) {
		//first check if the input is empty
		if (cmdQue.isEmpty()) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(rawCommand));
			return null;
		}
		//new create a flow of file names
		String[] files = new String[cmdQue.size()];
		int index = 0;
		while (!cmdQue.isEmpty()) {
			String filename = cmdQue.poll();
			File file = new File(filename);
			if (!file.exists()) {
				//if not exist then print out error message
				//and also clear out the queue because it will not give input to another command
				System.out.print(Message.FILE_NOT_FOUND.with_parameter(rawCommand));
				cmdQue.clear();
				return new CAT(new String[0]);
			}
			//otherwise put stuff in the file flow
			files[index++] = filename;
		}
		//then create a new cat filter based on this
		return new CAT(files);
	}
}
