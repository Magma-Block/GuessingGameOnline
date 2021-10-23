package server;
import java.net.*;
public class Client {
	//connected clients' information
	int id;
	String Name/*,IP*/;
	boolean ready;
	Socket s;
	public Client(int id,String Name/*,String IP*/,Socket s,boolean ready) {
		this.id=id;
		this.Name=Name;
		//this.IP=IP;
		this.s=s;
		this.ready=ready;
	}

}
