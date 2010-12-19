/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.ga.distributed;

public class KSAdditiveFitnessFunction extends KSFitnessFunction {

	protected double calculateFitness(KSOrganism organism) {
		double fitness = 0.0;
		for (int i = 0; i < organism.genomeLength; i++) {
			fitness += organism.genome[i];
		}
		return fitness;
	}
}
