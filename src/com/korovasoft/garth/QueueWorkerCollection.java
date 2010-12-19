package com.korovasoft.garth;

import java.util.HashMap;

public class QueueWorkerCollection {

	private HashMap<AbstractQueueWorker, Thread> workerThreadHash;
	private boolean deployed;
	
	public QueueWorkerCollection() {
		workerThreadHash = new HashMap<AbstractQueueWorker,Thread>();
		deployed = false;
	}

	/**
	 * Returns false if the collection is already deployed.
	 * @param qw
	 * @return
	 */
	public boolean add(AbstractQueueWorker qw) {
		if (deployed) return false;
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
