package com.korovasoft.garth;

import com.korovasoft.ga.distributed.KSOrganism;

public class MatingWorker extends AbstractQueueWorker {

	public MatingWorker() {

	}
	
	@Override
	protected void doOneIterationOfWork() throws InterruptedException {
		// TODO Auto-generated method stub
		KSOrganism current = null;
		KSOrganism winner = qdb.take(GarthMain.EVALUATED_Q);
		for (int i = 1; i < GarthConfig.tournamentSize; i++) {
			current = qdb.take(GarthMain.EVALUATED_Q);
			if (current.fitnessScore < winner.fitnessScore) {
				winner = current;
			}
		}
		qdb.put(GarthMain.STATISTICS_Q,winner);

		// insert the same number of children
		for (int i = 1; i < GarthConfig.tournamentSize; i++) {
			qdb.put(GarthMain.UNEVALUATED_Q, new KSOrganism(winner));
		}
	}

}
