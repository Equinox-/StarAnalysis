package com.pi.astro;

public enum StellarClassification {
	O(25000, Double.MAX_VALUE), B(11000, 25000), A(7500, 11000), F(6000, 7500), G(
			5000, 6000), K(3500, 5000), M(0, 3500);
	private final double min, max;

	private StellarClassification(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public static StellarClassification classify(double temp) {
		for (StellarClassification v : values()) {
			if (temp >= v.min && temp < v.max) {
				return v;
			}
		}
		return null;
	}
}
