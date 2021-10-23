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
			System.out.println("服务器已开启，端口:25321");
			while(true) {
				Socket socket=server.accept();
				System.out.println("有客户端连接");
				BufferedReader bfr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				if(nowplayer<maxplayer&&status==0) {
					pw.println("//AGREE");
					String name=bfr.readLine();
					System.out.println("已收到连接用户的用户名:"+name);
					pw.println(++nowplayer);
					System.out.println("已发送id");
					Client client=new Client(nowplayer,name,socket,false);
					clients.add(client);
					System.out.println(name+" 加入了房间  "+nowplayer+"/"+maxplayer);
					for(Client c:clients) {
						new SendMessage(("//msg "+name+" 加入了房间  "+nowplayer+"/"+maxplayer),c.s);
					}
					new Thread(new ReceiveMesasge(client)).start();
					if(nowplayer==maxplayer)
					{
						for(Client c:clients) {
							new SendMessage(("//msg 房间已满，在聊天框内输入/READY进行准备"),c.s);
							new SendMessage(("//msg 游戏将在所有人准备后开始"),c.s);
						}
						new Thread(new BeforePlay(clients,maxplayer)).start();
					}
				}
				else if(nowplayer>=maxplayer) {
					pw.println("//REFUSE\n服务器已满");
				}
				else if(status==1) {
					pw.println("//REFUSE\n游戏已开始");
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
				System.out.println("检测中....");
				begin=true;
				for(Client c:clients)
					if(c.ready==false) {
						begin=false;
						break;
					}
				if(begin)break;
			}
			System.out.println("已开始游戏");
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
					System.out.println(c.Name+" 断开链接");
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
			System.out.print("请输入游戏人数:");
			maxplayer=s.nextInt();
			System.out.println("请确认游戏人数:"+maxplayer+"\n1:开启服务器     2:重新设置人数\n");
			int n=s.nextInt();
			if(n==1)
				break;
		}
	}
	
	public static void main(String[] args) {
		new GameServer();
	}

}
