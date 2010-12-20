package com.korovasoft.garth;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.korovasoft.ga.distributed.KSOrganism;

public class QueueDatabase {
	
	/**
	 * Queues are stored here and identified by unique names
	 */
	private ConcurrentHashMap<String, ArrayBlockingQueue<KSOrganism>> qHash;
	
	public QueueDatabase() {
		qHash = new ConcurrentHashMap<String, ArrayBlockingQueue<KSOrganism>>();
	}
	
	/**
	 * 
	 * @param name
	 * @param capacity
	 * @return false if name is already taken
	 */
	public boolean createQueue(String name, int capacity) {
		if (qHash.containsKey(name)) return false;
		qHash.put(name, new ArrayBlockingQueue<KSOrganism>(capacity));
		return true;
	}

	/**
	 * 
	 * @param name
	 * @return the next organism in the queue
	 * @throws InterruptedException
	 */
	public KSOrganism take(String name) throws InterruptedException {
		return qHash.get(name).take();
	}
	
	/**
	 * Puts the organism at the back of said queue
	 * @param name
	 * @param o
	 * @throws InterruptedException
	 */
	public void put(String name, KSOrganism o) throws InterruptedException {
		qHash.get(name).put(o);
	}
	
	/**
	 * It's like put() but without the all back-talk. This does not
	 * guarantee organisms will be placed in the queue.
	 * @param name
	 * @param o
	 */
	public boolean offer(String name, KSOrganism o) {
		return qHash.get(name).offer(o);
	}
}
