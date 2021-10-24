# GuessingGameOnline


## 灵感来源于“数字地雷”游戏



### 游戏玩法

   电脑随机产生答案，并给出答案所在范围。多位玩家依次根据电脑所给范围给出一个符合的数，该数如果不是答案，电脑则会改变范围，并继续游戏；如果该数是答案，则该玩家失败，游戏结束。
   
### 历史版本
###### 目前还没有.......

### 运行方式
###### 运行前提是要安装Java（废话），然后双击“StartServer.bat”启动服务器，“StartClient.bat”启动客户端，再或者你用cmd输指令“java -jar Server.jar”/“java -jar Client.jar”


### 游戏设计思路

   #### 服务器
   
   #### 服务器的类
          Client.java
          Command.java
          DealReceiveMessage.java
          GameServer.java
          Playing.java
          SendMessage.java
   
   
   ### Client.java
   
  ###### 仅仅用作保存各客户端的数据
    public class Client {
      //connected clients' information
      int id;
      String Name/*,IP*/;
      boolean ready;
      Socket s;
      public Client(int id,String Name/*,String IP*/,Socket s,boolean ready) {//IP后来想了想并没有任何用处，所以注释掉了
         this.id=id;
         this.Name=Name;
         //this.IP=IP;
         this.s=s;
         this.ready=ready;
      }
    }
   
------------------------------------------------ 
   
   ### Command.java
   
  ###### emm因为懒所以写的一个处理cmd指令如清屏“cls”的类
   
------------------------------------------------  
   
   ### DealReceiveMessage.java
   
  ###### 用于处理客户端发送过来的消息，其中继承了Thread类，因为在处理一些消息所用时间可能“ 较  长 ”，导致服务器无法接收到客户端在服务器处理过程中发送的消息，所以在每次收到新消息时启动该线程，做到处理接收两不误！
       
   
  ###### 接收到的消息会被转化成char数组，然后通过遍历以及字符串连接把它其中包含的“消息类型”和“数据”提取出来，然后根据“消息类型”来确定处理“数据”的方法。
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
   
------------------------------------------------

   ### GameServer.java
   
   ###### 服务器核心，其中包括了服务器和游戏的设置
   
   #### 连接
   ###### 服务器在开启后会一直监听所有连接请求。在有请求连接时，如果人数已达设置的人数或游戏已开始，则会向客户端发送“/REFUSE”，即拒绝客户端加入，反之则向客户端发送“/AGREE”，客户端在收到“/AGREE”后开始与服务器进行数据交换，其中服务器需要给客户端发送它的id，客户端需要给服务器发送其用户名。互相发送完后服务器新建Client.java类的实例并将收到的客户端的信息储存在其中，最后添加到列表，开启接收客户端消息的监听(一个线程，下面代码没写注释，将就看吧)。
   	public GameServer() {
		new Command().title("猜数游戏服务器 - Alpha-1");
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
   
   #### 游戏开始前准备
   ###### 在人数已满时服务器会要求玩家在弹出的聊天框（客户端部分有讲）中输入“/READY”来进行准备，同时也会有一个线程在重复判断是否全部玩家都已准备，如果全部玩家都已准备，则新建游玩“Playing.java”类实例，游戏开始。
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
					//System.out.println("检测中....");
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
      
------------------------------------------------

   ### Playing.java
   
   ###### 游玩类，仅仅只是处理玩家发送过来的答案以及发送新的“范围”（是不是简单到难以置信）
      
------------------------------------------------

   ### SendMessage.java
   
   ###### 消息发送类，仅仅用于发送已被“打包”好的消息。
    /*
	 * 发送消息专用类
	 * 
	 * 构造函数：message：已打包成string类型的消息  s:要送达的客户端
	 * 
	 * 格式：  //[消息类型] /[数据，其中包括整型，字符串等其他数据类型]\ //[消息类型] /.....
	 * 
	 * 每个消息以两个左斜杠开始，以一个右斜杠结束，有斜杠后可以再加做斜杠做下一消息
	 * 
	 * 消息类型及数据有：
	 *         1：msg  聊天消息  数据数量：1  数据：String msg
	 *         2：ques [仅服务器发送]问题数据  数据数量：4  数据1：int id  数据2：String Name   数据3：int big   数据4：int small
	 *         3：ans  [仅客户端发送]回答数据  数据数量：1  数据：int ans
	 *         4：gameover  [仅服务器发送]指令  数据数量：1  数据：String loser
	 *         
	 */
   

------------------------------------------------

### 客户端

#### 客户端的类
	Command.java
	DealReceiveMessage.java
	GameClient.java
	Playing.java
	SendMessage.java
	TalkingWindow.java

###### 因为客户端这边好多都是和服务器那边相同的，比如“Command.java”，“DealReceiveMessage.java”，所以仅简单介绍一下TalkingWindow.java

##### TalkingWindow.java

###### 聊天窗口，其中运用了Java的Swing功能。在窗口中包括了显示消息的文本域，输入消息的文本框以及发送按钮。作用很简单，显示（不是接收）玩家聊天消息以及服务器的一些通知，比如“游戏开始前准备”
	public class TalkingWindow extends JFrame{
		JTextArea ta=new JTextArea("在该窗口进行聊天!\n");
		JPanel show=new JPanel();
		JPanel input=new JPanel();
		JButton jb=new JButton();
		JScrollPane sp=new JScrollPane(ta);
		JTextField tf=new JTextField(20);
		Container c=this.getContentPane();
		Socket server;
		public TalkingWindow(String name,Socket server)
		{
			ta.setEditable(false);
			ta.setLineWrap(true);
			jb.setText("发送");
			this.setSize(275,300);
			c.setSize(275,300);//设置JFrame容器的大小
		this.setVisible(true);

		input.add(tf);
		input.add(jb);
		this.add(sp);
		this.add(input,BorderLayout.SOUTH);
		this.setTitle("聊天");

		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message=tf.getText().toString();
				if(!message.equals("")) {
					if(message.equals("/READY")||message.equals("/ready"))
						new SendMessage(("//ready "),server);		//这里就是当你输入的时“/READY”时的一个处理，让服务器知道你准备了
					else new SendMessage(("//msg <"+name+">"+message),server);
					tf.setText("");
				}
			}
		});
		}
		public void Put(String message)
		{
			ta.append(message+'\n');
		}
	}
###### 在服务器“DealReceiveMessage.java”类中，有专门的处理准备消息的方法
	else if(msgtype.equals("ready"))ready(i);
