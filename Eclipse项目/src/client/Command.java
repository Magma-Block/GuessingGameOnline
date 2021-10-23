package client;

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
	public void pause()
	{
		try {
			new ProcessBuilder("cmd", "/c", "pause")
			.inheritIO()//cls
			.start()
			.waitFor();//cls	
		}catch(InterruptedException e) {}
		catch(IOException e) {}
	}

}
