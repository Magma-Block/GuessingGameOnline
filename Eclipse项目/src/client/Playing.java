package client;
import java.util.*;
import java.net.*;
public class Playing {
	//����𰸺ͷ���
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
		System.out.println("��"+nowid+"�����"+Name+"����һ��:\n����"+small+"��С��"+big+"����");
		if(nowid==myid) {
			while(true) {
				System.out.println("������:");
				n=new Scanner(System.in).nextInt();
				if(n>small&&n<big)break;
				else System.out.println("�������\n\n");
			}	
			new SendMessage("//ans "+n,server);
		}
	}

}
