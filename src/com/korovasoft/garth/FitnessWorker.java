package com.korovasoft.garth;

import com.korovasoft.ga.distributed.KSFitnessFunction;
import com.korovasoft.ga.distributed.KSOrganism;

public class FitnessWorker extends AbstractQueueWorker {

	private KSFitnessFunction ff;
	
	public FitnessWorker(KSFitnessFunction F) {
		ff = F;
	}
	
	@Override
	protected void doOneIterationOfWork() throws InterruptedException {
		KSOrganism o = qdb.take(GarthMain.UNEVALUATED_Q);
		ff.evaluate(o);
		qdb.put(GarthMain.EVALUATED_Q,o);
	}

}
