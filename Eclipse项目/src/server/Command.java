package server;

import java.io.*;

public class Command {
	public void cls()
	{
		try {
			new ProcessBuilder("cmd", "/c", "cls")
			.inheritIO()//cls
			.start()
			.waitFor();//cls	
		}catch(InterruptedException e) {}
		catch(IOException e) {}
	}

}
