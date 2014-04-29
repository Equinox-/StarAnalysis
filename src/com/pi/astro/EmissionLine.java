package com.pi.astro;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum EmissionLine {
	OVI(1.0333E-007), Lya(1.21524E-007), NV(1.23942E-007), OI(1.30553E-007), CIIA(
			1.33552E-007), SiIV(1.39761E-007), SiIVOIV(0.00000014), CIV(
			1.54586E-007), HeII(1.63785E-007), OIII(1.66585E-007), AlII(
			1.8574E-007), CIII(1.90827E-007), CIIB(2.326E-007), NeIV(
			0.000000244), MgII(0.00000028), NeVA(3.34679E-007), NeVB(
			3.42685E-007), OII(3.7283E-007), HeI(3.889E-007), He(3.97119E-007), SII(
			4.0723E-007), Hd(4.10289E-007), Hg(4.34168E-007), OIIID(
			4.364436E-007), Hb(4.86268E-007), OIIIA(4.932603E-007), OIIIB(
			0.000000496), OIIIC(5.00824E-007), OIA(6.302046E-007), OIB(
			6.365536E-007), NI(6.52903E-007), NIIA(0.000000655), Ha(
			6.56461E-007), NIIB(6.58527E-007), SIIA(6.71829E-007), SIIB(
			6.73267E-007), K(3.934777E-007), H(0.000000397), G(4.30561E-007), Mg(
			5.1767E-007), Na(5.8956E-007);
	public final double lambda;

	private EmissionLine(double d) {
		this.lambda = d;
	}

	public static EmissionLine match(double d) {
		double best = Double.MAX_VALUE;
		EmissionLine bestL = null;
		for (EmissionLine l : values()) {
			if (Math.abs(l.lambda - d) < best) {
				best = Math.abs(l.lambda - d);
				bestL = l;
			}
		}
		return bestL;
	}

	/**
	 * Computes the emission lines, and width of each line for the variance
	 * between a black body curve and an observed spectrum.
	 * 
	 * @param variance
	 *            The variance between the observed data and a black body curve.
	 *            Each element is {wavelength in meters, flux in W/m^2}
	 * @param spectrumTolerance
	 *            The minimum variance level to be considered as an emission
	 *            line
	 * @param lineTolerance
	 *            The maximum error between the actual line wavelength and the
	 *            variance wavelength to be considered an emission.
	 * @return the emission lines
	 */
	public static Set<Entry<EmissionLine, Integer>> matchEmissionLines(
			double[][] variance, double spectrumTolerance, double lineTolerance) {
		Map<EmissionLine, Integer> setLines = new HashMap<EmissionLine, Integer>();
		for (double[] vary : variance) {
			if (Math.abs(vary[1]) > spectrumTolerance) {
				EmissionLine line = EmissionLine.match(vary[0]);
				if (line != null
						&& Math.abs(line.lambda - vary[0]) < lineTolerance) {
					Integer count = setLines.get(line);
					if (count == null) {
						count = 0;
					}
					count++;
					setLines.put(line, count);
				}
			}
		}
		return setLines.entrySet();
	}

	/**
	 * Computes the emission lines, and width of each line for the variance
	 * between a black body curve and an observed spectrum.
	 * 
	 * This uses 1E-7 meters spectrum tolerance, and 1E-8 meters line tolerance.
	 * 
	 * @see EmissionLine#matchEmissionLines(double[][], double, double)
	 */
	public static Set<Entry<EmissionLine, Integer>> matchEmissionLines(
			double[][] variance) {
		return matchEmissionLines(variance, 1E-7, 1E-8);
	}
}
