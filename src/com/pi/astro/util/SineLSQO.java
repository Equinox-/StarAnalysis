package com.pi.astro.util;

import java.util.Arrays;

public class SineLSQO {
	/**
	 * {dxoff, dyoff, dperiod, damp}
	 */
//	private static double[] deriv(double x, double yOrig, double xoff,
//			double yoff, double period, double amp) {
//		// coeff[0] -> coeff[3] * Math.cos(coeff[2] * (curve[i][0] - coeff[0]))
//		// * coeff[2] * -1
//		// coeff[1] -> 1
//		// coeff[2] -> coeff[3] * Math.cos(coeff[2] * (curve[i][0] - coeff[0]))
//		// * (curve[i][0] - coeff[0])
//		// coeff[3] = Math.sin(coeff[2] * (curve[i][0] - coeff[0]))
//		double eAmp = Math.sin(period * (x - xoff));
//		double ePeriod = amp * Math.cos(period * (x - xoff)) * (x - xoff);
//		double eXOFF = amp * Math.cos(period * (x - xoff)) * period * -1;
//		double eYOFF = 1;
//		return new double[] { eXOFF, eYOFF, ePeriod, eAmp };
//	}
//
//	private static double[] changes(double[][] dataSet, double xoff,
//			double yoff, double period, double amp) {
//		double[] res = new double[4];
//		for (double[] pt : dataSet) {
//			double[] here = deriv(pt[0], pt[1], xoff, yoff, period, amp);
//			double error = (evalSine(pt[0], xoff, yoff, period, amp) - pt[1]);
//			for (int i = 0; i < here.length; i++) {
//				res[i] -= here[i] != 0 ? error / here[i] : 0;
//			}
//		}
//		return res;
//	}

	public static double evalSine(double x, double... coeff) {
		return coeff[3] * Math.sin(2.0 * Math.PI * (x - coeff[0]) / coeff[2])
				+ coeff[1];
	}

	private static double errorSquare(double[][] dataSet, double xoff,
			double yoff, double period, double amp, boolean abs) {
		double error = 0;
		for (double[] pt : dataSet) {
			double ts = evalSine(pt[0], xoff, yoff, period, amp) - pt[1];
			if (abs) {
				error += Math.abs(ts * ts);
			} else {
				error += ts * ts;
			}
		}
		return error;
	}

	public static double[] stepMatch(double[][] set, double rateMax,
			double rateMult, double[] limits, double[] coeff, int idStep) {
		double[] coeff2 = Arrays.copyOf(coeff, coeff.length);
		int coeffID = idStep + (idStep > 0 ? 1 : 0);
		// double[] changes = changes(set, coeff[0], coeff[1], coeff[2],
		// coeff[3]);
		double error = errorSquare(set, coeff[0], coeff[1], coeff[2], coeff[3],
				true);
		double errorSigned = errorSquare(set, coeff[0], coeff[1], coeff[2],
				coeff[3], false);
		double stepGeom = 0.95;
		double step = Math.min(coeff[coeffID] * Math.sqrt(error / set.length)
				* stepGeom, 0.1);
		coeff2[coeffID] += step;
		double errorPlus = errorSquare(set, coeff2[0], coeff2[1], coeff2[2],
				coeff2[3], true);
		if (errorPlus < error) {
			coeff[coeffID] += step;
		} else {
			coeff[coeffID] -= step + step;
		}
		return coeff;
	}

	/**
	 * {dxoff, dyoff, dperiod, damp}
	 */
	public static double[] match(double[][] set, double rateMax,
			double rateMult, double[] limits) {
		// double xoff = 0, yoff = 0, period = 1, amp = 1;
		double[] coeff = { 0, 0, 10, 1 };
		// for (int a = 0; a < 100; a++) {
		// for (int idStep = 0; idStep <= 2; idStep++) {
		// for (int step = 0; step < 10; step++) {
		// coeff = stepMatch(set, rateMax, rateMult, limits, coeff,
		// idStep);
		// }
		// }
		// }
		return coeff;
	}
}
