package com.korovasoft.garth;

import java.util.HashMap;

/**
 * Collection of related queue workers, each of which will run in a separate thread, and can be deployed simultaneously
 * @author robert
 *
 */
public class QueueWorkerCollection {

	/**
	 * thread to alert when an individual worker would like to stop the entire collection
	 */
	private Thread bossThread;
	
	/**
	 * database of related queues that will be shared amongst the 
	 */
	private QueueDatabase queueDatabase;
	
	/**
	 * associates worker objects with threads in which they run
	 */
	private HashMap<AbstractQueueWorker, Thread> workerThreadHash;
	
	/**
	 * has the worker collection been deployed yet? If so, we can't add workers to the collection
	 */
	private boolean deployed;
	
	/**
	 * Create new worker collection with bossthread t and QueueDatabase qdb
	 * @param t
	 * @param qdb
	 */
	public QueueWorkerCollection(Thread t, QueueDatabase qdb) {
		workerThreadHash = new HashMap<AbstractQueueWorker,Thread>();
		deployed = false;
		bossThread = t;
		queueDatabase = qdb;
	}

	/**
	 * Returns false if the collection is already deployed.
	 * Configures the workers with references to their bossThread
	 * an shared QueueDatabase
	 * @param qw
	 * @return
	 */
	public boolean add(AbstractQueueWorker qw) {
		if (deployed) return false;
		qw.bossThread = bossThread;
		qw.qdb = queueDatabase;
		workerThreadHash.put(qw, new Thread(qw));
		return true;
	}
	
	/**
	 * Starts each thread in the collection
	 */
	public void deploy() {
		deployed = true;
		for (Thread t : workerThreadHash.values()) {
			t.start();
		}
	}
	
	/**
	 * <b>Asks</b> each thread to exit cleanly
	 */
	public void withdraw() {
		for (AbstractQueueWorker qw : workerThreadHash.keySet()) {
			qw.haltAsSoonAsPossible();
			workerThreadHash.get(qw).interrupt();
		}
	}
	
	
}
