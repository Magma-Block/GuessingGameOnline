package client;
import java.net.*;
import java.io.*;
public class SendMessage {
	/*
	 * ������Ϣר����
	 * 
	 * ���캯����message���Ѵ����string���͵���Ϣ  s:Ҫ�ʹ�Ŀͻ���
	 * 
	 * ��ʽ��  //[��Ϣ����] /[���ݣ����а������ͣ��ַ�����������������]\ //[��Ϣ����] /.....
	 * 
	 * ÿ����Ϣ��������б�ܿ�ʼ����һ����б�ܽ�������б�ܺ�����ټ���б������һ��Ϣ
	 * 
	 * ��Ϣ���ͼ������У�
	 *         1��msg  ������Ϣ  ����������1  ���ݣ�String msg
	 *         2��ques [������������]��������  ����������4  ����1��int id  ����2��String Name   ����3��int big   ����4��int small
	 *         3��ans  [���ͻ��˷���]�ش�����  ����������1  ���ݣ�int ans
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
