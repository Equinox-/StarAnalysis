package com.pi.astro;

import java.util.ArrayList;
import java.util.List;

public class MainSequence {
	private static List<double[]> data = new ArrayList<double[]>();

	static {
		// http://www.atnf.csiro.au/outreach/education/senior/astrophysics/stellarevolution_mainsequence.html
		data.add(new double[] { 3.00E-003, 2900 });
		data.add(new double[] { 0.03, 3800 });
		data.add(new double[] { 0.3, 5000 });
		data.add(new double[] { 1, 6000 });
		data.add(new double[] { 5, 7000 });
		data.add(new double[] { 60, 11000 });
		data.add(new double[] { 600, 17000 });
		data.add(new double[] { 10000, 22000 });
		data.add(new double[] { 17000, 28000 });
		data.add(new double[] { 80000, 35000 });
		data.add(new double[] { 790000, 44500 });
		
		// http://www.essex1.com/people/speer/main.html
		data.add(new double[] { 30000, 33000 });
		data.add(new double[] { 16000, 30000 });
		data.add(new double[] { 8300, 22000 });
		data.add(new double[] { 750, 15000 });
		data.add(new double[] { 130, 12500 });
		data.add(new double[] { 63, 9500 });
		data.add(new double[] { 40, 9000 });
		data.add(new double[] { 24, 8700 });
		data.add(new double[] { 9, 7400 });
		data.add(new double[] { 6.3, 7100 });
		data.add(new double[] { 4, 6400 });
		data.add(new double[] { 1.45, 5900 });
		data.add(new double[] { 1, 5800 });
		data.add(new double[] { 0.7, 5600 });
		data.add(new double[] { 0.44, 5300 });
		data.add(new double[] { 0.36, 5100 });
		data.add(new double[] { 0.28, 4830 });
		data.add(new double[] { 0.18, 4370 });
		data.add(new double[] { 0.075, 3670 });
		data.add(new double[] { 0.03, 3400 });
		data.add(new double[] { 0.0005, 3200 });
		data.add(new double[] { 0.0002, 3000 });
	}

	public static double msqTempToLuminosity(double temp) {
		double total = 0;
		double weight = 0;
		for (double[] d : data) {
			double j = Math.pow(Math.abs(temp - d[1]), -20.0);
			total += j * d[0];
			weight += j;
		}
		return total / weight * 3.846E26;
	}
}
