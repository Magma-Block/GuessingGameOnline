package server;
import java.io.*;
import java.net.*;
import java.util.*;
public class GameServer {
	List<Client> clients =new ArrayList<>();
	int maxplayer=0,gamemode=0,nowplayer=0,status=0;//status 0:waiting 1:playing
	Command command=new Command();
	Scanner s=new Scanner(System.in);
	ServerSocket server;
	Playing playing=null;
	public GameServer() {
		Init();
		try {
			server=new ServerSocket(25321);
			System.out.println("�������ѿ������˿�:25321");
			while(true) {
				Socket socket=server.accept();
				System.out.println("�пͻ�������");
				BufferedReader bfr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				if(nowplayer<maxplayer&&status==0) {
					pw.println("//AGREE");
					String name=bfr.readLine();
					System.out.println("���յ������û����û���:"+name);
					pw.println(++nowplayer);
					System.out.println("�ѷ���id");
					Client client=new Client(nowplayer,name,socket,false);
					clients.add(client);
					System.out.println(name+" �����˷���  "+nowplayer+"/"+maxplayer);
					for(Client c:clients) {
						new SendMessage(("//msg "+name+" �����˷���  "+nowplayer+"/"+maxplayer),c.s);
					}
					new Thread(new ReceiveMesasge(client)).start();
					if(nowplayer==maxplayer)
					{
						for(Client c:clients) {
							new SendMessage(("//msg �����������������������/READY����׼��"),c.s);
							new SendMessage(("//msg ��Ϸ����������׼����ʼ"),c.s);
						}
						new Thread(new BeforePlay(clients,maxplayer)).start();
					}
				}
				else if(nowplayer>=maxplayer) {
					pw.println("//REFUSE\n����������");
				}
				else if(status==1) {
					pw.println("//REFUSE\n��Ϸ�ѿ�ʼ");
				}
				
			}
		}catch(IOException e) {e.printStackTrace();}
	}
	
	class BeforePlay implements Runnable{
		boolean begin=false;
		List<Client> clients;
		int maxplayer;
		public BeforePlay(List<Client> clients,int maxplayer) {
			this.clients=clients;
			this.maxplayer=maxplayer;
		}
		public void run() {
			while(true) {
				System.out.println("�����....");
				begin=true;
				for(Client c:clients)
					if(c.ready==false) {
						begin=false;
						break;
					}
				if(begin)break;
			}
			System.out.println("�ѿ�ʼ��Ϸ");
			playing =new Playing();
			playing.init(maxplayer, clients);
		}
	}
	
	class ReceiveMesasge implements Runnable{
		Client c;
		BufferedReader bfr;
		PrintWriter pw;
		String msg;
		public ReceiveMesasge(Client c) {
			this.c=c;
			try {
				bfr=new BufferedReader(new InputStreamReader(this.c.s.getInputStream()));
				pw=new PrintWriter(this.c.s.getOutputStream(),true);
			}catch(IOException e) {e.printStackTrace();}
		}
		public void run()
		{
			while(true) {
				try {
					msg=bfr.readLine();
					System.out.println(msg);
					//pw.println("Receive");
					new Thread(new DealReceiveMessage(msg,clients,c,playing)).start();
				}
				catch(IOException e) {
					System.out.println(c.Name+" �Ͽ�����");
					nowplayer--;
					break;
				}
			}
		}
	}
	
	public void Init()
	{

		while(true)
		{
			command.cls();
			System.out.print("��������Ϸ����:");
			maxplayer=s.nextInt();
			System.out.println("��ȷ����Ϸ����:"+maxplayer+"\n1:����������     2:������������\n");
			int n=s.nextInt();
			if(n==1)
				break;
		}
	}
	
	public static void main(String[] args) {
		new GameServer();
	}

}
