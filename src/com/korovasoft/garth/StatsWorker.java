package com.korovasoft.garth;

import com.korovasoft.ga.distributed.KSOrganism;

public class StatsWorker extends AbstractQueueWorker {

	private double lowestFitness = Double.POSITIVE_INFINITY;
	private double highestFitness = Double.NEGATIVE_INFINITY;
	private long numOrgsSeen = 0;
	private long numOrgsSinceImprovement = 0;
	private long numOrgsSinceLastReport = 0;
	private boolean newLowestFitness = false;
	
	@Override
	protected void doOneIterationOfWork() throws InterruptedException {
		KSOrganism o = qdb.take(GarthMain.STATISTICS_Q);
		qdb.put(GarthMain.EVALUATED_Q,o); // That way everyone else can get on with their lives as soon as possible.
		if (o.fitnessScore <= GarthConfig.threshhold) {
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
		if (newLowestFitness || numOrgsSinceLastReport > GarthConfig.reportInterval) {
			System.out.println("Score: " + lowestFitness + "\tOrgs: " + numOrgsSeen);
			numOrgsSinceLastReport = 0;
			newLowestFitness = false;
		}
		if (numOrgsSinceImprovement > GarthConfig.stagnationInterval) {
			interruptBoss();
		}
		numOrgsSeen++;
		numOrgsSinceLastReport++;
		numOrgsSinceImprovement++;
	}

}
