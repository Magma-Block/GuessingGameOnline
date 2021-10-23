package client;
import java.util.*;
import java.net.*;
public class Playing {
	//输入答案和返回
	int nowid,big,small,myid;
	Socket server;
	String Name;
	Command c=new Command();
	public Playing(int nowid,String Name,int big,int small,int myid,Socket server) {
		this.nowid=nowid;
		this.Name=Name;
		this.big=big;
		this.small=small;
		this.myid=myid;
		this.server=server;
		answer();
	}
	
	public void answer() {
		int n;
		c.cls();
		System.out.println("请"+nowid+"号玩家"+Name+"输入一个:\n大于"+small+"且小于"+big+"的数");
		if(nowid==myid) {
			while(true) {
				System.out.println("请输入:");
				n=new Scanner(System.in).nextInt();
				if(n>small&&n<big)break;
				else System.out.println("输入错误\n\n");
			}	
			new SendMessage("//ans "+n,server);
		}
	}

}
