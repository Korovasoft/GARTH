package com.korovasoft.garth;

import java.util.HashMap;

public class QueueWorkerCollection {

	private Thread bossThread;
	private QueueDatabase queueDatabase;
	private HashMap<AbstractQueueWorker, Thread> workerThreadHash;
	private boolean deployed;
	
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
	
	public void deploy() {
		deployed = true;
		for (Thread t : workerThreadHash.values()) {
			t.start();
		}
	}
	
	public void withdraw() {
		for (AbstractQueueWorker qw : workerThreadHash.keySet()) {
			qw.haltAsSoonAsPossible();
			workerThreadHash.get(qw).interrupt();
		}
	}
	
	
}
