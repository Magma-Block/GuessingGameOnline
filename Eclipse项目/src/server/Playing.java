package server;
import java.util.*;
public class Playing {
	int big=10000,small=0,nowplayer=1,maxplayer,ans=new Random().nextInt(9999)+1;
	List<Client> clients;
	public void init(int maxplayer,List<Client> clients) {
		this.clients=clients;
		this.maxplayer=maxplayer;
		for(Client c:clients) {
			new SendMessage("//ques "+nowplayer+" "+clients.get(nowplayer-1).Name+" "+big+" "+small+" ",c.s);
		}
	}
	
	public void gotans(int ans) {
		if(ans==this.ans) {
			for(Client c:clients) {
				new SendMessage("//gameover "+clients.get(nowplayer-1).Name,c.s);
			}
			System.exit(0);
		}
		else {
			if(ans>this.ans)big=ans;
			else if(ans<this.ans)small=ans;
			nowplayer++;
			if(nowplayer>maxplayer)nowplayer=1;
			for(Client c:clients) {
				new SendMessage("//ques "+nowplayer+" "+clients.get(nowplayer-1).Name+" "+big+" "+small+" ",c.s);
			}
		}
	}
}
