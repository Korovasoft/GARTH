package com.korovasoft.garth;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import com.korovasoft.ga.distributed.KSOrganism;

public class StatsWorker extends AbstractQueueWorker {

	private ArrayBlockingQueue<KSOrganism> evaluatedQ;
	private ArrayBlockingQueue<KSOrganism> statsQ;
	private double threshhold;
	private double lowestFitness;
	private double highestFitness;
	private long numOrgsSeen;
	private long numOrgsSinceImprovement;
	private long numOrgsSinceLastReport;
	private boolean newLowestFitness;
	
	public StatsWorker(Thread bossThread, HashMap<String,ArrayBlockingQueue<KSOrganism>> qHash, double threshhold) {
		super(bossThread);
		evaluatedQ = qHash.get("evaluated");
		statsQ = qHash.get("statistics");
		this.threshhold = threshhold;
		lowestFitness = Double.POSITIVE_INFINITY;
		highestFitness = Double.NEGATIVE_INFINITY;
		numOrgsSeen = 0;
		numOrgsSinceLastReport = 0;
		newLowestFitness = false;
	}
	
	@Override
	protected void doSomeWork() throws InterruptedException {
		KSOrganism o = statsQ.take();
		evaluatedQ.put(o); // That way everyone else can get on with their lives as soon as possible.
		if (o.fitnessScore <= threshhold) {
			interruptBoss();
		}
		if (o.fitnessScore < lowestFitness) {
			lowestFitness = o.fitnessScore;
			newLowestFitness = true;
			numOrgsSinceImprovement = 0;
		}
		if (o.fitnessScore > highestFitness) {
			highestFitness = o.fitnessScore;
			KSOrganism.setFitnessNormalizer(highestFitness);
		}
		if (newLowestFitness || numOrgsSinceLastReport > 100000) {
			System.out.println("Score: " + lowestFitness + "\tOrgs: " + numOrgsSeen);
			numOrgsSinceLastReport = 0;
			newLowestFitness = false;
		}
		if (numOrgsSinceImprovement > 10 * 1000 * 1000) {
			interruptBoss();
		}
		numOrgsSeen++;
		numOrgsSinceLastReport++;
		numOrgsSinceImprovement++;
	}

}
