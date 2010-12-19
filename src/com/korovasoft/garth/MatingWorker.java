package com.korovasoft.garth;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import com.korovasoft.ga.distributed.KSOrganism;

public class MatingWorker extends AbstractQueueWorker {

	private ArrayBlockingQueue<KSOrganism> evaluatedQ;
	private ArrayBlockingQueue<KSOrganism> unevaluatedQ;
	private ArrayBlockingQueue<KSOrganism> statsQ;
	
	public MatingWorker(Thread bossThread, HashMap<String,ArrayBlockingQueue<KSOrganism>> qHash) {
		super(bossThread);
		evaluatedQ = qHash.get("evaluated");
		unevaluatedQ = qHash.get("unevaluated");
		statsQ = qHash.get("statistics");
	}
	
	@Override
	protected void doSomeWork() throws InterruptedException {
		// TODO Auto-generated method stub
		KSOrganism parent1 = evaluatedQ.take();
		KSOrganism parent2 = evaluatedQ.take();
		
		KSOrganism winner = (parent1.fitnessScore < parent2.fitnessScore) ? parent1 : parent2;
		
		KSOrganism child = new KSOrganism(winner);
		
		unevaluatedQ.put(child);
		statsQ.put(winner);
	}

}
