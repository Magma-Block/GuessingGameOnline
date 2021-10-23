package server;
import java.net.*;
import java.util.*;
public class DealReceiveMessage extends Thread{
	//处理收到的消息
	
	private String message;
	private List<Client> clients;
	private char[] chmsg=new char[10000];
	private String msgtype="";
	private Client thisclient;
	private Playing playing;
	public DealReceiveMessage(String message,List<Client> clients,Client thisclient,Playing p) {
		this.message=message;
		this.clients=clients;
		this.thisclient=thisclient;
		this.playing=p;
	}
	
	public void msg(int i)
	{
		for(Client c:clients)
		{
			new SendMessage(this.message,c.s);
			System.out.println("sent");
		}
	}
	
	public void ready(int i) {
		thisclient.ready=true;
		for(Client c:clients)
		{
			new SendMessage("//msg "+thisclient.Name+" 已准备",c.s);
			System.out.println("sent");
		}
	}
	
	public void ans(int i) {
		i++;
		String res="";
		while(i<=this.message.length()-1)
		{
			res+=chmsg[i];
			i++;
		}
		int ans=Integer.parseInt(res);
		playing.gotans(ans);
		
	}
	
	public void run() {
		message.getChars(0, message.length(), chmsg, 0);
		int i=0;
		while(i<message.length()) {
			if(chmsg[i]=='/'&&chmsg[i+1]=='/')
			{
				i+=2;
				while(chmsg[i]!=' ') {
					msgtype+=chmsg[i];
					i++;
				}
				System.out.println(msgtype+msgtype.equals("msg"));
				if(msgtype.equals("msg"))msg(i);
				else if(msgtype.equals("ans"))ans(i);
				else if(msgtype.equals("ready"))ready(i);
			}
			i++;
		}
	}

}
