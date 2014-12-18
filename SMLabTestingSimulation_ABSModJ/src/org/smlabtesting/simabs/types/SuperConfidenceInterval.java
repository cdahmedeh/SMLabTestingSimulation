package org.smlabtesting.simabs.types;

import static java.lang.Math.sqrt;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

//TODO: Change to a newer joke.
public class SuperConfidenceInterval {
	private StatisticalSummary stats;
	private double zeta = 0.0;
	private double confidence = 0.0;

	public SuperConfidenceInterval(double[] values, double confidence) {
		this.confidence = confidence;
		stats = new DescriptiveStatistics(values); 
		
	}
	
	public double getMean() {
		return stats.getMean();
	}
	
	public double getStandardDeviation() {
		return stats.getStandardDeviation();
	}
	
	public double getZeta() {
		TDistribution tDistribution = new TDistribution(
				stats.getN() - 1
		);
		
		double tInv = tDistribution.inverseCumulativeProbability(
				1 - (1 - confidence) / 2
		);
		
		return zeta = tInv * stats.getStandardDeviation() / sqrt(stats.getN());
	}
	
	public double getLowerCI() {
		return stats.getMean() - zeta;
	}

	public double getUpperCI() {
		return stats.getMean() + zeta;
	}
	
	public double getR() {
		return Math.abs(zeta/stats.getMean());
	}
}


