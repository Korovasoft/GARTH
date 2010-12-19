package com.korovasoft.garth;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import com.korovasoft.ga.distributed.KSAdditiveFitnessFunction;
import com.korovasoft.ga.distributed.KSOrganism;

public class GarthMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int populationSize = 500;
		int threshhold = 0;
		int genomeLength = 10;
		HashMap<String,ArrayBlockingQueue<KSOrganism>> qHash = new HashMap<String,ArrayBlockingQueue<KSOrganism>>();
		
		Thread t = Thread.currentThread();
		qHash.put("unevaluated", new ArrayBlockingQueue<KSOrganism>(populationSize, false));
		qHash.put("evaluated",  new ArrayBlockingQueue<KSOrganism>(populationSize, false));
		qHash.put("statistics",  new ArrayBlockingQueue<KSOrganism>(populationSize, false));
		
		for (int i = 0; i < populationSize; i++) {
			qHash.get("unevaluated").add(new KSOrganism(genomeLength, true));
		}
		
		QueueWorkerCollection workers = new QueueWorkerCollection();
		
		workers.add(new FitnessWorker(t, qHash, new KSAdditiveFitnessFunction()));
		workers.add(new StatsWorker(t, qHash, threshhold));
		workers.add(new MatingWorker(t, qHash));
		
		workers.deploy();
		try {
			while(true) {
				Thread.sleep(60 * 1000);
			}
		} catch (InterruptedException e) {
			
		}
		workers.withdraw();
	}

}
