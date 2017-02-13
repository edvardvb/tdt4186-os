package p2;

/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 * One barber instance corresponds to one consumer in
 * the producer/consumer problem.
 */
public class Barber implements Runnable {
	/**
	 * Creates a new barber. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		The GUI.
	 * @param pos		The position of this barber's chair
	 */
	CustomerQueue queue;
	Gui gui;
	int pos;
	Thread thread;
	
	public Barber(CustomerQueue queue, Gui gui, int pos) { 
		this.queue = queue;
		this.gui = gui;
		this.pos = pos;
		
		this.thread = new Thread(this, "Barber #" + pos);
		
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run(){		
		while(true){
			Customer nextCustomer = this.queue.next();
			gui.fillBarberChair(this.pos, nextCustomer);
			gui.println("Barber #" + pos + " got a new customer from " + this.queue.pointerStart);
			
			try{
				Thread.sleep(Globals.barberWork);
			}
			catch (InterruptedException e) {}
			gui.emptyBarberChair(this.pos);
			gui.println("Barber #" + pos + " finished his customer.");
			
			try{
				gui.barberIsSleeping(this.pos);
				gui.println("Barber #" + pos + " is daydreaming.");
				Thread.sleep(Globals.barberSleep);
				gui.barberIsAwake(this.pos);
			}
			catch (InterruptedException e){}
		
		}
	}

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() {
		thread.start();
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread() {
	}

	// Add more methods as needed
}

