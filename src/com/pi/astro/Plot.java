package com.pi.astro;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Plot extends JFrame {
	private static final long serialVersionUID = 1L;
	double minX, maxX;
	double minY, maxY;
	List<double[][]> points = new ArrayList<double[][]>();

	public Plot() {
		super("Plot");
		setSize(500, 500);
		setLocation(0, 0);
		setVisible(true);
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		maxX = Double.MIN_VALUE;
		maxY = Double.MIN_VALUE;
	}

	public void addChart(double[][] data) {
		points.add(data);
		for (double[] p : data) {
			minX = Math.min(minX, p[0]);
			minY = Math.min(minY, p[1]);
			maxX = Math.max(maxX, p[0]);
			maxY = Math.max(maxY, p[1]);
		}
		repaint();
	}

	public void removeChart(double[][] data) {
		points.remove(data);
		repaint();
	}

	Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.GREEN };

	public void paint(Graphics g) {
		int c = 0;
		g.clearRect(0, 0, getWidth(), getHeight());
		for (double[][] data : this.points) {
			g.setColor(colors[c++]);
			for (int i = 1; i < data.length; i++) {
				int x1 = (int) Math.round((data[i - 1][0] - minX)
						/ (maxX - minX) * getWidth());
				int y1 = (int) Math
						.round((1.0 - ((data[i - 1][1] - minY) / (maxY - minY)))
								* getHeight());
				int x2 = (int) Math.round((data[i][0] - minX) / (maxX - minX)
						* getWidth());
				int y2 = (int) Math
						.round((1.0 - ((data[i][1] - minY) / (maxY - minY)))
								* getHeight());
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
}
