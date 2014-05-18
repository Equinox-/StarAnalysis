package com.pi.astro;

public class BlackBody {
	/**
	 * Computes the flux per steradian of the planck function at the given
	 * temperature and wavelength.
	 * 
	 * @param temp
	 *            Kelvin
	 * @param wavelength
	 *            Meters
	 * @return W/(sr m^2)/m
	 */
	public static double planckLaw(double temp, double wavelength) {
		double chunkA = (2.0 * Constants.PLANCKS_CONSTANT * Math.pow(
				Constants.SPEED_OF_LIGHT, 2));
		double waveA = Math.pow(wavelength, 5);
		double exp = Math
				.exp((Constants.PLANCKS_CONSTANT * Constants.SPEED_OF_LIGHT)
						/ (wavelength * Constants.BOLTZMANN_CONSTANT * temp));
		return (chunkA / waveA) / (exp - 1.0);
	}

	/**
	 * Evaluates how well a linearly scaled black body matches the given data
	 * set.
	 * 
	 * @param points
	 *            the data set. Each element is {wavelength in meters, flux in
	 *            W/m^2}
	 * @param temp
	 *            the temperature to evaluate the black body curve at
	 * @return {match metric, black body curve multiplier}
	 */
	private static double[] evalCurve(double[][] points, double temp,
			double[][] variance) {
		double metric = 0;
		double avgMultiplier = 0;
		double count = 0;

		double lastGood = 0;

		for (int i = 2; i < points.length; i++) {
			double[] pt = points[i];
			double diff = pt[1] - lastGood;
			if (Math.abs(diff) < 2.3E-7 || pt[1] > 0) {
				if (pt[1] > 0) {
					avgMultiplier += (pt[1] / planckLaw(temp, pt[0]));
					count++;
				}
				lastGood = pt[1];
			}
		}
		avgMultiplier /= count;
		for (int i = 0; i < points.length; i++) {
			variance[i][0] = points[i][0];
			if (points[i][1] > 0) {
				variance[i][1] = (points[i][1] - (avgMultiplier * planckLaw(
						temp, points[i][0])));
				metric += (points[i][1] - (avgMultiplier * planckLaw(temp,
						points[i][0])));
			} else {
				variance[i][1] = 0;
			}
		}
		return new double[] { metric, avgMultiplier };
	}

	/**
	 * Matches the planck curve with the given temperature to the data set
	 * provided using a linear scaling factor.
	 * 
	 * @param data
	 *            the data set. Each element is {wavelength in meters, flux in
	 *            W/m^2}
	 * @param temp
	 *            the temperature in Kelvin
	 * @param scale
	 *            the scaling factor
	 * @return The planck curve data set. Each element is {wavelength in meters,
	 *         flux in W/m^2/sr}
	 */
	private static double[][] matchedPlanckCurve(double[][] data, double temp,
			double scale) {
		double[][] curve = new double[data.length][2];
		for (int i = 0; i < curve.length; i++) {
			curve[i][0] = data[i][0];
			curve[i][1] = scale * planckLaw(temp, data[i][0]);
		}
		return curve;
	}

	/**
	 * Utility class for storing match results. *
	 */
	public static class PlanckMatchResults {
		/**
		 * The planck curve data set. Each element is {wavelength in meters,
		 * flux in W/m^2/sr}
		 */
		public double[][] planckData;
		/**
		 * The matched temperature of the black body.
		 */
		public double temperature;
		/**
		 * The match metric between the planck curve and the data set.
		 */
		public double matchMetric;
		/**
		 * The scaling factor. The data set is equal to the planck curve
		 * evaluated at the temperature multiplied by the scaling factor.
		 */
		public double scalingFactor;
		/**
		 * The variance between data and the planck curve. Each element is
		 * {wavelength in meters, flux in W/m^2}
		 */
		public double[][] curveVariance;
	}

	/**
	 * Matches the given information to a planck curve multiplied by a constant.
	 * This algorithm tries to minimize the error between the planck curve and
	 * the data set, discarding any zero-flux values.
	 * 
	 * @param data
	 *            the data set. Each element is {wavelength in meters, flux in
	 *            W/m^2}
	 * @param steps
	 *            the number of processing iterations to perform
	 * @return the match results
	 */
	public static PlanckMatchResults matchCurve(double[][] data, int steps) {
		double temp = 6000;
		double[] res = { 0, 1 };
		double[][] variance = new double[data.length][2];
		double resLast = -1;
		for (int step = 0; step < steps; step++) {
			res = BlackBody.evalCurve(data, temp, variance);
			if (resLast > 0) {
				// Maybe some adaptive thing
			}
			temp += (res[0] * 1E8);
			resLast = res[0];
		}
		PlanckMatchResults results = new PlanckMatchResults();
		results.curveVariance = variance;
		results.matchMetric = res[0];
		results.scalingFactor = res[1];
		results.planckData = matchedPlanckCurve(data, temp,
				results.scalingFactor);
		results.temperature = temp;
		return results;
	}

	/**
	 * Computes the surface flux of a black body of the provided temperature.
	 * 
	 * This is the Stefann-Boltzmann Law.
	 * 
	 * @param temp
	 *            the data set. Each element is {wavelength in meters, spectral flux in
	 *            W/m^2/m}
	 * @return total flux in W/m^2
	 */
	public static double totalFlux(double temp) {
		return 5.67037321E-8 * Math.pow(temp, 4);
	}

	/**
	 * Computes the surface flux of a black body with the given spectral data.
	 * 
	 * This uses a Riemann Sum
	 * 
	 * @param data
	 *            temperature in Kelvin
	 * @return total flux in W/m^2
	 */
	public static double totalFlux(double[][] data) {
		double flux = 0;
		for (int i = 1; i < data.length; i++) {
			flux += (data[i][0] - data[i - 1][0])
					* (data[i][1] + data[i - 1][1]) * 0.5;
		}
		return flux;
	}

	/**
	 * Computes the maximum wavelength in meters for the black body radiation
	 * curve at the given temperature.
	 * 
	 * This is Wien's displacement law.
	 * 
	 * @param temp
	 *            the temperature in kelvin
	 * @return the wavelength in meters
	 */
	public static double lambdaMax(double temp) {
		return 2.89777221E-3 / temp;
	}

	/**
	 * Computes the maximum wavelength in meters for the given spectrum.
	 * 
	 * @param data
	 *            the data set. Each element is {wavelength in meters, flux in
	 *            W/m^2}
	 * @return the maximum wavelength in meters
	 */
	public static double lambdaMax(double[][] data) {
		double[] mostPower = { 0, 0 };
		for (double[] d : data) {
			if (d[1] * d[0] > mostPower[1] * mostPower[0]) {
				mostPower = d;
			}
		}
		return mostPower[0];
	}

}
