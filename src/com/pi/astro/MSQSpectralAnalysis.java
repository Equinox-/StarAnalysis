package com.pi.astro;

import java.io.File;
import java.io.IOException;

import com.pi.astro.BlackBody.PlanckMatchResults;

public class MSQSpectralAnalysis {

	public static void main(String[] args) throws IOException {
		double[][] data = CSVParser.getCSV(new File("binary1data.csv"));
		PlanckMatchResults results = BlackBody.matchCurve(data, 100);
		double luminosity = MainSequence.msqTempToLuminosity(
				results.temperature, false);
		System.out.println("Temperature: " + results.temperature);
		System.out.println("Lambda Max (Observed): "
				+ BlackBody.lambdaMax(data) + " m");
		System.out.println("Lambda Max (Computed): "
				+ BlackBody.lambdaMax(results.temperature) + " m");
		System.out.println("Luminosity: " + luminosity + " W");
		double totalFlux = BlackBody.totalFlux(results.temperature);
		double observedFlux = totalFlux * results.scalingFactor;
		System.out.println("Total flux: " + totalFlux + " W/m^2");
		System.out.println("Observed flux (Calculated): " + observedFlux
				+ " W/m^2");
		System.out.println("Observed flux (Raw): " + BlackBody.totalFlux(data)
				+ " W/m^2");
		double computeDistance = Math.sqrt(luminosity / observedFlux / Math.PI
				/ 4.0);
		System.out.println("Computed distance: " + computeDistance + " m ("
				+ (computeDistance / 9460730472580800.0) + " ly)");
		double solarRadius = Math.sqrt(luminosity / totalFlux / Math.PI / 4.0);
		System.out.println("Solar radius: " + solarRadius + " m");
		System.out.println("Spectral type: "
				+ StellarClassification.classify(results.temperature));

		Object[] lines = EmissionLine.matchEmissionLines(results.curveVariance)
				.toArray();
		System.out.println("Emission Lines:  (Line=Width)");
		for (Object o : lines) {
			System.out.println(o);
		}
	}
}
