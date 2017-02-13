package p2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
	/**
	 * Creates a new customer queue. Make sure to save these variables in the class.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */
	Customer[] queue;
	Gui gui;
	
	int pointerStart = 0;
	int pointerEnd = -1;
	
	
    public CustomerQueue(int queueLength, Gui gui) {
		this.queue = new Customer[queueLength];
		this.gui = gui;
	}

	public synchronized void add(Customer customer) {
		int currentEnd = this.pointerEnd;
		int nextEnd = (this.pointerEnd + 1) % this.queue.length;
		
		while (this.queue[nextEnd] != null) {
			try{wait();} 
			catch (InterruptedException e){}
		}
		
		queue[nextEnd] = customer;
		gui.fillLoungeChair(nextEnd, customer);
		
		this.pointerEnd = nextEnd;
		
		notifyAll();
	}

	public synchronized Customer next() {
		Customer popped;
		while (true) {
			popped = this.queue[pointerStart];
			
			if (popped == null) {
				try{wait();}
				catch(InterruptedException e){}
			} 
			else break;
		}
		
		this.queue[pointerStart] = null;
		gui.emptyLoungeChair(pointerStart);
		this.pointerStart = (this.pointerStart + 1) % this.queue.length;
		notifyAll();
		
		return popped;
	}

	// Add more methods as needed
}
