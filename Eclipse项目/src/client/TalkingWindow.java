package client;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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
        				new SendMessage(("//ready "),server);
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
