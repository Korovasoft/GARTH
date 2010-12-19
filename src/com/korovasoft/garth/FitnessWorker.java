package com.korovasoft.garth;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import com.korovasoft.ga.distributed.KSFitnessFunction;
import com.korovasoft.ga.distributed.KSOrganism;

public class FitnessWorker extends AbstractQueueWorker {

	private KSFitnessFunction ff;
	private HashMap<String,ArrayBlockingQueue<KSOrganism>> qHash;
	
	public FitnessWorker(Thread bossThread, HashMap<String,ArrayBlockingQueue<KSOrganism>> qHash, KSFitnessFunction F) {
		super(bossThread);
		this.qHash = qHash;
		ff = F;
	}
	
	@Override
	protected void doSomeWork() throws InterruptedException {
		KSOrganism o = qHash.get("unevaluated").take();
		ff.evaluate(o);
		qHash.get("evaluated").put(o);
	}

}
