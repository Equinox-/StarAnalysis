package com.pi.astro;

import java.io.File;
import java.io.IOException;

public class BinaryRedshiftAnalysis {
	public static class OrbitResults {
		public double orbitalPeriod;
		public double orbitalVelocity;
	}

	public static double redshiftToVelocity(double observed, double emit) {
		return ((observed / emit) - 1.0) * Constants.SPEED_OF_LIGHT;
	}

	public static double[][] redshiftToVelocity(double[][] data, double emit) {
		double[][] j = new double[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			j[i][0] = data[i][0];
			j[i][1] = data[i][1] > 0 ? redshiftToVelocity(data[i][1], emit) : 0;
		}
		return j;
	}

	public static OrbitResults matchOrbit(double[][] data) {
		// Grab derivs
		double root = 0;
		for (int i = 0; i < data.length - 1; i++) {
			if (data[i][1] != 0 && data[i + 1][1] != 0) {
				if (Math.signum(data[i + 1][1]) != Math.signum(data[i][1])) {
					double total = Math.abs(data[i + 1][1])
							+ Math.abs(data[i][1]);
					double nextW = Math.abs(data[i + 1][1]) / total;
					double prevW = Math.abs(data[i][1]) / total;
					root = (data[i + 1][0] * nextW) + (data[i][0] * prevW);
				}
			}
		}
		double[][] curve = new double[data.length][2];
		for (int i = 0; i < data.length; i++) {
			// Math.sin(data[i][0] - root);
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		double[][] data = CSVParser.getCSV(new File("binary2orbit.csv"));
		double[][] relVelocity = redshiftToVelocity(data, 656.255);
		OrbitResults results = matchOrbit(relVelocity);
		Plot p = new Plot();
		p.addChart(relVelocity);

	}
}
