package p2;

/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 * One doorman instance corresponds to one producer in
 * the producer/consumer problem.
 */
public class Doorman implements Runnable {
	/**
	 * Creates a new doorman. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		A reference to the GUI interface.
	 */
	
	CustomerQueue queue;
	Gui gui;
	Thread thread;
	boolean isStopped = false;

	public Doorman(CustomerQueue queue, Gui gui) { 
		this.queue = queue;
		this.gui = gui;
		this.thread = new Thread(this, "Doorman");
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run(){
		Customer newCustomer;
		
		while(!this.isStopped){
			gui.println("New customer has arrived");
			newCustomer = new Customer();
			this.queue.add(newCustomer);
			gui.println("Customer added at seat " + this.queue.pointerEnd);
			
			try{
				Thread.sleep(Globals.doormanSleep);
			}
			catch (InterruptedException e){}
		}
	}


	/**
	 * Starts the doorman running as a separate thread. Make
	 * sure to create the thread and start it.
	 */
	public void startThread() {
		thread.start();
	}

	/**
	 * Stops the doorman thread. Use Thread.join() for stopping
	 * a thread.
	 */
	public void stopThread() {
		this.isStopped = true;
	}

	// Add more methods as needed
}
