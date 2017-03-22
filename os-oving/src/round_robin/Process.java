package round_robin;

/**
 * This class contains data associated with processes,
 * and methods for manipulating this data as well as
 * methods for displaying a process in the GUI.
 *
 * You will probably want to add more methods to this class.
 */
public class Process {
	/** The ID of the next process to be created */
	private static long nextProcessId = 1;
	/** The ID of this process */
	private long processId;
	/** The amount of memory needed by this process */
    private long memoryNeeded;
	/** The amount of cpu time still needed by this process */
    private long cpuTimeNeeded;
	/** The average time between the need for I/O operations for this process */
    private long avgIoInterval;
	/** The time left until the next time this process needs I/O */
    private long timeToNextIoOperation = 0;

	/** The time that this process has spent waiting in the memory queue */
	private long timeSpentWaitingForMemory = 0;
	/** The time that this process has spent waiting in the CPU queue */
	private long timeSpentInReadyQueue = 0;
	/** The time that this process has spent processing */
    private long timeSpentInCpu = 0;
	/** The time that this process has spent waiting in the I/O queue */
    private long timeSpentWaitingForIo = 0;
	/** The time that this process has spent performing I/O */
	private long timeSpentInIo = 0;

	/** The number of times that this process has been placed in the CPU queue */
	private long nofTimesInReadyQueue = 0;
	/** The number of times that this process has been placed in the I/O queue */
	private long nofTimesInIoQueue = 0;
	private long nofProcessedIoTasks = 0;
	/** The global time of the last event involving this process */
	private long timeOfLastEvent;

	/**
	 * Creates a new process with given parameters. Other parameters are randomly
	 * determined.
	 * @param memorySize	The size of the memory unit.
	 * @param creationTime	The global time when this process is created.
	 */
	public Process(long memorySize, long creationTime) {
		// Memory need varies from 100 kB to 25% of memory size
		memoryNeeded = 100 + (long)(Math.random()*(memorySize/4-100));
		// CPU time needed varies from 100 to 10000 milliseconds
		cpuTimeNeeded = 100 + (long)(Math.random()*9900);
		// Average interval between I/O requests varies from 1% to 25% of CPU time needed
		avgIoInterval = (1 + (long)(Math.random()*25))*cpuTimeNeeded/100;
		generateNextIOTime();
		// The first and latest event involving this process is its creation
		timeOfLastEvent = creationTime;
		// Assign a process ID
		processId = nextProcessId++;
	}
	
    public long generateNextIOTime(){
        long time = (long) ((Math.random() * (1.2*avgIoInterval - 0.8*avgIoInterval)) + 0.8*avgIoInterval);
        this.timeToNextIoOperation = time;
        return time;

    }

	public long getMemoryNeeded() {
		return memoryNeeded;
	}
	
	public long getRemainingCpuTime(){
		return this.cpuTimeNeeded;
	}
	
	public long getTimeToNextIoOperation(){
		return this.timeToNextIoOperation;
	}
	
	public long getProcessId() {
		return processId;
	}

	public void updateStatistics(Statistics statistics, long clock) {
		
		statistics.totalTimeSpentWaitingForMemory += timeSpentWaitingForMemory;
        statistics.totalTimeSpentInReadyQueue += timeSpentInReadyQueue;
        statistics.totalTimeSpentInCpu += timeSpentInCpu;
        statistics.totalBusyCpuTime += timeSpentInCpu;
        statistics.totalTimeSpentWaitingForIo += timeSpentWaitingForIo;
        statistics.totalTimeSpentInIo += timeSpentInIo;

        statistics.totalNofTimesInReadyQueue += nofTimesInReadyQueue;
        statistics.totalNofTimesInIoQueue += nofTimesInIoQueue;

        statistics.nofCompletedProcesses++;
        statistics.nofProcessedIoOperations += nofProcessedIoTasks;
	}
	
	public void leftMemoryQueue(long clock) {
		timeSpentWaitingForMemory += clock - this.timeOfLastEvent;
		timeOfLastEvent = clock;
	}
	
	public void enterIoQueue(long clock){
		this.nofTimesInIoQueue ++;
		this.timeOfLastEvent = clock;
	}
	
	public void enterIo(long clock){
		this.timeSpentWaitingForIo += clock - this.timeOfLastEvent;
		this.timeOfLastEvent = clock;
	}
	
	public void leftIo(long clock){
		generateNextIOTime();
		this.timeSpentInIo += clock - this.timeOfLastEvent;
		this.nofProcessedIoTasks ++;
		clock = this.timeOfLastEvent;
	}

	public void enterCpuQueue(long clock){
		this.nofTimesInReadyQueue ++;
		this.timeOfLastEvent = clock;
	}
	
	public void enterCpu(long clock){
		this.timeSpentInReadyQueue += clock - this.timeOfLastEvent;
		this.timeOfLastEvent = clock;
	}
	
	public void leftCpu(long clock){
		long timeSpent = clock - this.timeOfLastEvent;
		this.cpuTimeNeeded -= timeSpent;
		this.timeToNextIoOperation -= timeSpent;
		this.timeSpentInCpu += timeSpent;
		this.timeOfLastEvent = clock;
	}
}