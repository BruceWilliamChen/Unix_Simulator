package cs131.pa1.filter.sequential;

import cs131.pa1.filter.Message;
import java.util.*;

public class SequentialREPL {
	//this string is to keep a track of current working dir
	static String currentWorkingDirectory;
	
	//the program that interacts with the user
	public static void main(String[] args){
		System.out.print(Message.WELCOME.toString());
		System.out.print(Message.NEWCOMMAND.toString());
		
		//then ask the user to input message
		Scanner usr = new Scanner(System.in);
		String commands = usr.nextLine();
		
		currentWorkingDirectory = System.getProperty("user.dir");
		
		//then start to use these commands and call commandbuilder class
		while (!commands.equals("exit")) {
			//every pipped command will trigger the command builder class
			SequentialCommandBuilder flow = new SequentialCommandBuilder(commands);
			flow.run();
			System.out.print(Message.NEWCOMMAND.toString());
			commands = usr.nextLine();
		}
		usr.close();
		System.out.print(Message.GOODBYE.toString());
	}

}
