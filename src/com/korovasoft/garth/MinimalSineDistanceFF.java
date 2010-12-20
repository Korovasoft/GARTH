package com.korovasoft.garth;

import com.korovasoft.ga.distributed.KSFitnessFunction;
import com.korovasoft.ga.distributed.KSOrganism;

public class MinimalSineDistanceFF extends KSFitnessFunction {

	@Override
	protected double calculateFitness(KSOrganism organism) {
		// TODO Auto-generated method stub
		return sineNorm(organism.genome)*norm(organism.genome) + norm(organism.genome);
	}

	
	private double norm(double[] coordinates) {
		double sum = 0;
		for (int i = 0; i < coordinates.length; i++) {
			sum += Math.pow(coordinates[i],2);
		}
		return Math.sqrt(sum);
	}
	
	private double sineNorm(double[] coordinates) {
		double sum = 0;
		for (int i = 0; i < coordinates.length; i++) {
			sum += Math.pow(Math.sin(coordinates[i]),2);
		}
		return Math.sqrt(sum);
	}
}
