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
		System.out.print("�����������IP��ַ:");
		IP=s.next();
		System.out.print("������������˿�:");
		port=s.nextInt();
		try {
			socket=new Socket(IP,port);
			if(socket.isConnected()) {
				BufferedReader bfr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				String res1=bfr.readLine();
				if(res1.equals("//AGREE")) {
					System.out.print("������\n\n�������������:");
					Name=s.next();
					pw.println(Name);
					String res=bfr.readLine();
					System.out.println("���յ�id"+res);
					
					try{
						id=Integer.parseInt(res);
					}catch(NumberFormatException e) {e.printStackTrace();}
					tw=new TalkingWindow(Name,socket);
					new Thread(new ReceiveMessage(socket,tw)).start();
					
				}
				else if(res1.equals("//REFUSE")) {
					System.out.println("�������ܾ��˼��룬ԭ��:"+bfr.readLine());
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
					System.out.println("�����Ѷ�ʧ");
					break;
				}
			}
		}
	}
	
	public static void main(String args[]) {
		new GameClient();
	}

}
