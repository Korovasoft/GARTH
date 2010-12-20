package com.korovasoft.garth;

import com.korovasoft.ga.distributed.KSOrganism;

public class GarthMain {

	public static final String EVALUATED_Q = "evaluated";
	public static final String UNEVALUATED_Q = "unevaluated";
	public static final String STATISTICS_Q = "statistics";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		
		Thread t = Thread.currentThread();
		QueueDatabase qdb = new QueueDatabase();
		
		qdb.createQueue(EVALUATED_Q, GarthConfig.populationSize);
		qdb.createQueue(UNEVALUATED_Q, GarthConfig.populationSize);
		qdb.createQueue(STATISTICS_Q, GarthConfig.populationSize);
		
		
		for (int i = 0; i < GarthConfig.populationSize; i++) {
			qdb.offer(UNEVALUATED_Q, new KSOrganism(GarthConfig.genomeLength, true));
		}
		
		QueueWorkerCollection workers = new QueueWorkerCollection(t,qdb);
		
		workers.add(new FitnessWorker(new MinimalSineDistanceFF()));
		workers.add(new FitnessWorker(new MinimalSineDistanceFF()));
		workers.add(new StatsWorker());
		workers.add(new MatingWorker());
		
		workers.deploy();
		try {
			while(true) {
				Thread.sleep(GarthConfig.masterThreadSleepInterval);
			}
		} catch (InterruptedException e) {
			
		}
		workers.withdraw();
		long finishTime = System.currentTimeMillis();
		long totalTime = finishTime - startTime;
		long numSeconds = totalTime / 1000;
		System.out.println("Experiment completed in " + numSeconds + " seconds.");
	}

}
