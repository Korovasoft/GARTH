package com.korovasoft.garth;

public abstract class AbstractQueueWorker implements Runnable{

	private boolean continueRunning;
	private Thread bossThread;
	
	public AbstractQueueWorker(Thread t) {
		bossThread = t;
		continueRunning = true;
	}
	
	public void haltAsSoonAsPossible() {
		continueRunning = false;
	}
	
	public void run() {
		while (continueRunning) {
			try {	
				doSomeWork();
			} catch (InterruptedException e) {
				continueRunning = false;
			}
		}
	}
	
	protected void interruptBoss() {
		bossThread.interrupt();
	}
	
	protected abstract void doSomeWork() throws InterruptedException;
}
