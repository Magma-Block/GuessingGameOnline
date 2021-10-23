package client;
import java.net.*;
import java.io.*;
public class SendMessage {
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
	 *         
	 */
	
	
	public SendMessage(String message,Socket s) {
		try {
			BufferedReader bfr=new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
			pw.println(message);
			/*String res=bfr.readLine();
			if(res.equals("Received"))
				System.out.println(res);*/
		}catch(IOException e) {e.printStackTrace();}
	}

}
