import java.util.concurrent.BlockingQueue;

import com.twitter.hbc.core.StatsReporter.StatsTracker;


public class messagePrinter implements Runnable{
	BlockingQueue<String> messageQueue;
	
	public messagePrinter(BlockingQueue<String> messageQueue) {
		this.messageQueue = messageQueue;
	}
	
	public void run() {
		while(true)
			try {
				System.out.println(messageQueue.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
