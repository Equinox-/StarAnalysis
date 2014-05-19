package com.pi.astro;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.pi.astro.util.SineLSQO;

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

	public static double[][] stripZeros(double[][] data) {
		double[][] stripped = new double[data.length][2];
		int j = 0;
		for (int i = 0; i < stripped.length; i++) {
			if (data[i][1] != 0) {
				stripped[j][0] = data[i][0];
				stripped[j][1] = data[i][1];
				j++;
			}
		}
		double[][] res = new double[j][2];
		System.arraycopy(stripped, 0, res, 0, j);
		return res;
	}

	public static double[][] matchOrbit(double[][] data) {
		double[][] stripped = stripZeros(data);
		double[] coeff = SineLSQO.match(stripped, .025, .1, new double[] { 100,
				1E6, 100, 1E6 });

		double[][] curve = new double[data.length * 10][2];
		for (int i = 0; i < data.length * 10; i++) {
			// dxoff, dyoff, dperiod, damp
			curve[i][0] = data[0][0]
					+ (float) (data[data.length - 1][0] - data[0][0])
					* ((float) i / (float) (data.length * 10));
			curve[i][1] = coeff[3]
					* Math.sin(coeff[2] * (curve[i][0] - coeff[0])) + coeff[1];
		}
		return curve;
	}

	public static void main(String[] args) throws IOException {
		final double[][] data = CSVParser.getCSV(new File("binary2orbit.csv"));
		final double[][] relVelocity = redshiftToVelocity(data, 656.255);
		final Plot p = new Plot();
		p.addChart(relVelocity);
		final double[][] stripped = stripZeros(relVelocity);
		final double[] coeff =new double[] {
				-7.423, 4.26E2, 63.4305, 173500 };/* SineLSQO.match(stripped, .025, .1, new double[] {
				-7.4, 4.26E2, Math.PI * 2.0 / 63.4305, 173500 });*/

		final double[][] curve = new double[data.length * 10][2];
		for (int i = 0; i < data.length * 10; i++) {
			// dxoff, dyoff, dperiod, damp
			curve[i][0] = data[0][0]
					+ (float) (data[data.length - 1][0] - data[0][0])
					* ((float) i / (float) (data.length * 10));
			curve[i][1] = SineLSQO.evalSine(curve[i][0], coeff);
		}
		p.addChart(curve);
		p.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				double[] old = Arrays.copyOf(coeff, coeff.length);
				SineLSQO.stepMatch(stripped, .05, .1, new double[] { 100, 1E6,
						100, 1E6 }, coeff,
						e.isControlDown() ? 2
								: (e.getButton() == MouseEvent.BUTTON1 ? 1 : 0));

				System.out.println((coeff[0] - old[0]) + ","
						+ (coeff[1] - old[1]) + "," + (coeff[2] - old[2]) + ","
						+ (coeff[3] - old[3]));

				for (int i = 0; i < data.length * 10; i++) {
					// dxoff, dyoff, dperiod, damp
					curve[i][0] = data[0][0]
							+ (float) (data[data.length - 1][0] - data[0][0])
							* ((float) i / (float) (data.length * 10));
					curve[i][1] = coeff[3]
							* Math.sin(coeff[2] * (curve[i][0] - coeff[0]))
							+ coeff[1];
				}
				p.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
}
