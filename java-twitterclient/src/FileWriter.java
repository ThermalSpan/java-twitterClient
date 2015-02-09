/* FileWriter.java 
 * Authored by Kimberly Sayre
 * This class implements the FileWriter interface.
 * It is used to write the contents of BlockingQueue<CharSequence> to a specified file
 * 
 * Changes made by Russell Bentley
 * Changed the constructor to include the external BlockingQueu
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;

public interface FileWriter extends Runnable {
   void open(File file);
   void run();
   void close();
}

class AsyncFilePrinter implements FileWriter {
	public boolean isStreaming;
	private  BlockingQueue<String> queue;
	//StatsTracker stats;
	
	public AsyncFilePrinter( BlockingQueue<String> queue){
		System.out.println("Initialized File Printer:");
		this.queue = queue;
		//this.stats = stats;
		isStreaming = true;
	}
	
	public void open(File file) {
		
	}
	
	public void run() {
		System.out.println("Now running File Printer:");
		while(isStreaming){
			System.out.println("Retrieving next msg:");
			String msg = "INIT";
			try {
				msg = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(msg);
			//System.out.println("Num of Messages: " + stats.getNumMessages());
		}
		System.out.println("Ending FileWriter Thread");
	}

	
	public void close() {
		this.isStreaming = false;
	}
}

class AsyncFileWriter implements FileWriter {
    private File file;
    private Writer out;
    private final BlockingQueue<String> queue;
    private volatile boolean streaming = false;
    public AsyncFileWriter(BlockingQueue<String> queue){
        this.queue = queue;
    }

	public void open(File file) {
    	try {
			this.out = new BufferedWriter(new java.io.FileWriter(file));
			this.streaming = true;
	        new Thread(this).start();
		} catch (IOException e) {System.out.println("File Writer won't start");}    
    }

    public void run() {
        while (streaming && !queue.isEmpty()) {
            try {
                String seq = queue.take();
                if (seq != null) {
                    try {
                       out.append(seq);
                       System.out.println("saved tweet");
                    } catch (IOException logme) {System.out.println("Failed to Write");
                    }
                }
            } catch (InterruptedException e) {System.out.println("Failed to write");}
        }
        try {
            out.close();
        } catch (IOException ignore) {System.out.println("Failed to close writer");}
    }


    public void close() {
        this.streaming = false;
    }

}
