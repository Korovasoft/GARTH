package com.korovasoft.garth;

public abstract class AbstractQueueWorker implements Runnable{

	/**
	 * Reference to the thread deploying these workers
	 */
	protected Thread bossThread;
	
	/**
	 * Reference to the database of queues with which
	 * these workers will work
	 */
	protected QueueDatabase qdb;
		
	/**
	 * Determines whether a worker should perform another iteration or not
	 */
	private boolean continueRunning;
	
	
	/**
	 * Plain old constructor
	 */
	public AbstractQueueWorker() {
		continueRunning = true;
	}
	
	/**
	 * Halts workers before they start the next iteration of work
	 */
	public void haltAsSoonAsPossible() {
		continueRunning = false;
	}
	
	/**
	 * Workers do one iteration at a time, and can 
	 * be halted before the next iteration by calling
	 * haltAsSoonAsPossible()
	 */
	public void run() {
		while (continueRunning) {
			try {	
				doOneIterationOfWork();
			} catch (InterruptedException e) {
				continueRunning = false;
			}
		}
	}
	
	/**
	 * Interrupts the boss thread. It may then decide to discontinue the experiment
	 * or maybe even restart it.
	 */
	protected void interruptBoss() {
		bossThread.interrupt();
	}
	
	/**
	 * Does one iteration of work
	 * @throws InterruptedException
	 */
	protected abstract void doOneIterationOfWork() throws InterruptedException;
}
