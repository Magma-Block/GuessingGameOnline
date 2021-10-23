package client;
import java.util.*;
import java.net.*;
import java.io.*;
public class GameClient {
	Scanner s=new Scanner(System.in);
	String IP,Name;
	int port,id;
	Socket socket;
	TalkingWindow tw;
	public GameClient() {
		System.out.print("请输入服务器IP地址:");
		IP=s.next();
		System.out.print("请输入服务器端口:");
		port=s.nextInt();
		try {
			socket=new Socket(IP,port);
			if(socket.isConnected()) {
				BufferedReader bfr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				String res1=bfr.readLine();
				if(res1.equals("//AGREE")) {
					System.out.print("已连接\n\n请输入你的名字:");
					Name=s.next();
					pw.println(Name);
					String res=bfr.readLine();
					System.out.println("已收到id"+res);
					
					try{
						id=Integer.parseInt(res);
					}catch(NumberFormatException e) {e.printStackTrace();}
					tw=new TalkingWindow(Name,socket);
					new Thread(new ReceiveMessage(socket,tw)).start();
					
				}
				else if(res1.equals("//REFUSE")) {
					System.out.println("服务器拒绝了加入，原因:"+bfr.readLine());
				}
			}
		}catch(IOException e) {e.printStackTrace();}
	}
	
	class ReceiveMessage implements Runnable{
		Socket s;
		BufferedReader bfr;
		PrintWriter pw;
		String msg;
		TalkingWindow tw;
		public ReceiveMessage(Socket s,TalkingWindow tw) {
			this.s=s;
			this.tw=tw;
			try {
				bfr=new BufferedReader(new InputStreamReader(this.s.getInputStream()));
				pw=new PrintWriter(this.s.getOutputStream(),true);
			}catch(IOException e) {e.printStackTrace();}
		}
		public void run() {
			while(true) {
				try {
					msg=bfr.readLine();
					//pw.println("Receive");
					new Thread(new DealReceiveMessage(msg,tw,id,socket)).start();
				}catch(IOException e) {
					System.out.println("连接已丢失");
					break;
				}
			}
		}
	}
	
	public static void main(String args[]) {
		new GameClient();
	}

}
