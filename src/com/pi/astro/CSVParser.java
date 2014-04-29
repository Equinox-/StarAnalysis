package com.pi.astro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	public static double[][] getCSV(File f) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(f));
		List<double[]> data = new ArrayList<double[]>();
		while (true) {
			String s = read.readLine();
			if (s == null) {
				break;
			}
			String[] chunks = s.split(",");
			try {
				data.add(new double[] { Double.valueOf(chunks[0]),
						Double.valueOf(chunks[1]) });
			} catch (Exception e) {
			}
		}
		read.close();
		return data.toArray(new double[0][0]);
	}
}
