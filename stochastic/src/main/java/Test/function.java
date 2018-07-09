package Test;

import java.util.ArrayList;
import java.util.List;

public class function {
	static int mid = 40;
	static int max = 60;
	static int min = 20;
	static int midMut = 20;
	static int maxMut = 30;
	static int minMut = 10;
	static int midChrom = 55;
	static int maxChrom = 100;
	static int minChrom = 10;

	public static void main(String[] args) {
		double[] numGene = new double[13];
		for (int i = 0; i < numGene.length; i++) {
			numGene[i] = ((i + 10.0) - 16) * (2.0 / (22 - 10));
		}
		int[] numChild = new int[60 / 2];
		int add = 2;
		for (int i = 0; i < numChild.length; i++) {
			numChild[i] = add;
			add += 2;
		}
		int[] numMut = new int[30];
		for (int i = 0; i < numMut.length; i++) {
			numMut[i] = 1 + i;
		}
		int[] numChrom = new int[91];
		for (int i = 10; i < numChrom.length + 10; i++) {
			numChrom[i - 10] = i;
		}
		List<Double> percentagesChild = new ArrayList<Double>();
		List<Double> percentagesMut = new ArrayList<Double>();
		double smallestOfAll = Integer.MAX_VALUE;
		for (int i = 0; i < numChrom.length; i++) {
			for (int j = 0; j < numChild.length; j++) {
				double percentageChild = numChild[j] * 1.0 / numChrom[i] * 100.0;
				if (percentageChild <= 60 && percentageChild >= 20) {
					percentagesChild.add(percentageChild);
				}
			}
			for (int j = 0; j < numMut.length; j++) {
				double percentageMut = numMut[j] * 1.0 / numChrom[i] * 100.0;
				if (percentageMut <= 30 && percentageMut >= 10) {
					percentagesMut.add(percentageMut);
				}
			}
			for (int j = 0; j < percentagesChild.size(); j++) {
				double coded = (percentagesChild.get(j) * 1.0 - mid) * (2.0 / (max - min));
				percentagesChild.set(j, coded);
			}
			for (int j = 0; j < percentagesMut.size(); j++) {
				double coded = (percentagesMut.get(j) * 1.0 - midMut) * (2.0 / (maxMut - minMut));
				percentagesMut.set(j, coded);
			}
			double codedChrom = (numChrom[i] * 1.0 - midChrom) * (2.0 / (maxChrom - minChrom));
			double smallest = Integer.MAX_VALUE;
			double tmp = 0;
			String[] values = new String[4];
			for (int j = 0; j < percentagesChild.size(); j++) {
				for (int k = 0; k < percentagesMut.size(); k++) {
					for (int l = 0; l < numGene.length; l++) {
						tmp = 979.0 - 137.9 * percentagesChild.get(j) + 12.3 * percentagesMut.get(k)
								+ 269.3 * codedChrom - 196.4 * numGene[l]
								- 202 * percentagesMut.get(k) * percentagesMut.get(k) - 347.5 * codedChrom * codedChrom
								+ 422.3 * numGene[l] * numGene[l]
								+ 236.9 * percentagesChild.get(j) * percentagesMut.get(k)
								- 188.4 * percentagesChild.get(j) * codedChrom
								- 151.9 * percentagesMut.get(k) * codedChrom;
						if (tmp < smallest && tmp > 0) {
							smallest = tmp;
							values[0] = "x1 = " + percentagesChild.get(j);
							values[1] = "x2 = " + percentagesMut.get(k);
							values[2] = "x3 = " + codedChrom;
							values[3] = "x4 = " + numGene[l];
						}
					}
				}
			}
			percentagesChild.clear();
			percentagesMut.clear();
			if (smallest < smallestOfAll) {
				smallestOfAll = smallest;
			}
			System.out.println(smallest);
			System.out.println(values[0] + "\n" + values[1] + "\n" + values[2] + "\n" + values[3]);
		}
		System.out.println("Hi, the smallest value was: " + smallestOfAll);
	}

}
