package client;
import java.net.*;
import java.util.*;
public class DealReceiveMessage extends Thread{
	//处理收到的消息
	
	private String message;
	private char[] chmsg=new char[10000];
	private String msgtype="";
	private TalkingWindow tw;
	private int id;
	private Socket server;
	public DealReceiveMessage(String message,TalkingWindow tw,int id,Socket server) {
		this.message=message;
		this.tw=tw;
		this.server=server;
		this.id=id;
	}
	
	public void msg(int i,int len)
	{
		i++;
		String message="";
		while(i<=len-1)
		{
			message+=chmsg[i];
			i++;
		}
		//System.out.println(message);
		tw.Put(message);
	}
	
	public void gameover(int i)
	{
		i++;
		String loser="";
		while(i<=this.message.length()-1)
		{
			loser+=chmsg[i];
			i++;
		}
		System.out.println("游戏结束\n\n"+loser+"输了\n\n");
		new Command().pause();
		System.exit(0);
	}
	
	public void ques(int i) {
		i++;
		String a="";
		String Name="";
		int id=-1,big=-1,small=-1;
		while(chmsg[i]!=' ') {
			a+=chmsg[i];
			i++;
		}
		id=Integer.parseInt(a);
		i++;
		a="";
		while(chmsg[i]!=' ') {
			a+=chmsg[i];
			i++;
		}
		Name=a;
		i++;
		a="";
		while(chmsg[i]!=' ') {
			a+=chmsg[i];
			i++;
		}
		big=Integer.parseInt(a);
		i++;
		a="";
		while(chmsg[i]!=' ') {
			a+=chmsg[i];
			i++;
		}
		small=Integer.parseInt(a);
		new Playing(id,Name,big,small,this.id,server);
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
				if(msgtype.equals("msg"))msg(i,message.length());
				else if(msgtype.equals("ques"))ques(i);
				else if(msgtype.equals("gameover"))gameover(i);
			}
			i++;
		}
	}

}
