import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;

public class KNN {
	private ArrayList<Instance> trainList;
	private ArrayList<Instance> testList;
	private int k;
	private double pwRange;
	private double plRange;
	private double swRange;
	private double slRange;
	// -----------cluster method
	private boolean stop = false;
	private double slMax;
	private double swMax;
	private double plMax;
	private double pwMax;
	private double slMin;
	private double swMin;
	private double plMin;
	private double pwMin;

	public KNN() {
		File train = new File("iris-training.txt");
		File test = new File("iris-test.txt");
		trainList = makeList(train);
		testList = makeList(test);
		setRange();
		System.out.println("Run KNN [press 1] or KMean Cluster[press 2] ?");
		Scanner scan = new Scanner(System.in);
		int c = scan.nextInt();
		if(c == 1) {
			System.out.println("Please enter a k value: ");
			scan = new Scanner(System.in);
			k = scan.nextInt();
			// check k is not less than 1
			if (k >= 1) {
				knnAlgorithm();
			}
			test(testList);
		}
		else if (c == 2){
			KMean();
		}
	
	}

	/**
	 * Runs the KNN Algorithm - deciding on a class label for each data point based on its nearest neighbours
	 * @return
	 */
	public boolean knnAlgorithm() {

		for (Instance a : testList) {
			Comparator<Instance> comparator = new DistanceComparator();
			PriorityQueue<Instance> queue = new PriorityQueue<Instance>(k, comparator);
			for (Instance b : trainList) {
				b.setDist(getDistance(a, b));
				queue.add(b);
			}
			// check which classes the nearest neighbours fall into
			int s = 0;
			int ve = 0;
			int vi = 0;

			Instance[] temp = new Instance[5];
			for (int i = 0; i < 5; i++) {
				temp[i] = queue.poll();
			}
			for (Instance i : temp) {
				if (i.getName().equals("Iris-setosa")) {
					s++;
				} else if (i.getName().equals("Iris-versicolor")) {
					ve++;
				} else {
					vi++;
				}
			}
			int max = Math.max(s, Math.max(ve, vi));
			if (max == s)
				a.setGuess("Iris-setosa");
			else if (max == ve)
				a.setGuess("Iris-versicolor");
			else if (max == vi)
				a.setGuess("Iris-virginica");

		}
		return true;

	}

	/**
	 * Makes a list of instances given an iris data file
	 * 
	 * @param f
	 * @param list
	 * @return
	 */
	public ArrayList<Instance> makeList(File f) {
		ArrayList<Instance> list = new ArrayList<Instance>();
		double pl;
		double pw;
		double sl;
		double sw;
		String name;
		double nextSl = 0;

		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextDouble()) {
				if (nextSl != 0)
					sl = nextSl;
				else
					sl = sc.nextDouble();
				sw = sc.nextDouble();
				pl = sc.nextDouble();
				pw = sc.nextDouble();
				name = sc.next();

				char[] c = name.toCharArray();
				String s = Character.toString(c[c.length - 1]);
				if (Character.isDigit(c[c.length - 1]) && Character.isDigit(c[c.length - 3]) && s.equals(".")) {
					String nextSlStr = c[c.length - 3] + "." + c[c.length - 1];
					nextSl = Double.parseDouble(nextSlStr);
				} else
					nextSl = 0;
				Instance i = new Instance(name, sl, sw, pl, pw);
				list.add(i);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Calculates euclidean distance
	 * @param a
	 * @param b
	 * @return distance
	 */
	public double getDistance(Instance a, Instance b) {

		double pwD = (Math.pow((a.getPW() - b.getPW()), 2)) / (Math.pow(pwRange, 2));
		double plD = (Math.pow((a.getPL() - b.getPL()), 2)) / (Math.pow(plRange, 2));
		double swD = (Math.pow((a.getSW() - b.getSW()), 2)) / (Math.pow(swRange, 2));
		double slD = (Math.pow((a.getSL() - b.getSL()), 2)) / (Math.pow(slRange, 2));
		double dsqrd = pwD + plD + swD + slD;
		double dist = Math.sqrt(dsqrd);
		return dist;

	}

	/**
	 * Sets the range for each different data classifier
	 * @return true if ranges all set correctly
	 */
	public boolean setRange() {

		slMax = 0;
		swMax = 0;
		plMax = 0;
		pwMax = 0;
		slMin = 99 ^ 9;
		swMin = 99 ^ 9;
		plMin = 99 ^ 9;
		pwMin = 99 ^ 9;
		if (!trainList.isEmpty()) {
			for (Instance i : trainList) {
				if (i.getPL() < plMin)
					plMin = i.getPL();
				if (i.getPL() > plMax)
					plMax = i.getPL();

				if (i.getPW() < pwMin)
					pwMin = i.getPW();
				if (i.getPW() > pwMax)
					pwMax = i.getPW();

				if (i.getSW() < swMin)
					swMin = i.getSW();
				if (i.getSW() > swMax)
					swMax = i.getSW();

				if (i.getSL() < slMin)
					slMin = i.getSL();
				if (i.getSL() > slMax)
					slMax = i.getSL();
			}

			slRange = slMax - slMin;
			swRange = swMax - swMin;
			plRange = plMax = plMin;
			pwRange = pwMax - pwMin;
			return true;
		} else
			return false;
	}

	/**
	 * tests the given arraylist for accuracy
	 */
	public void test(ArrayList<Instance> list) {
		double pos = 0;
		for (Instance i : list) {
			if (i.getName().equals(i.getGuess()))
				pos++;
		}
		System.out.println("Num of correct " + pos);
		System.out.println("percentage of correct " + pos / 75);
	}

	/**
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * 
	 * ----------------------------------KMEAN-CLUSTER METHOD
	 * FUNCTIONS-------------------------------------------
	 *
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 */

	/**
	 * implements the K-Mean cluster Algorithm
	 */
	public void KMean() {
		// generate initial centers
		Instance[] c1Array = new Instance[3];
		for (int i = 0; i < 3; i++) {
			Random r = new Random();
			double pw = pwMin + (pwMax - pwMin) * r.nextDouble();
			double pl = plMin + (plMax - plMin) * r.nextDouble();
			double sl = slMin + (slMax - slMin) * r.nextDouble();
			double sw = swMin + (swMax - swMin) * r.nextDouble();
			// create centroid instance
			Instance c1 = new Instance("c1", sl, sw, pl, pw);
			c1Array[i] = c1;
		}

		// cluster data points based on centroids
		while (!stop) {
			dataAssignmentPhase(c1Array);
			c1Array = updateCentroidPhase(c1Array);
		}
		
	}

	/**
	 * Assigns each given instance to a centroid based on which centroid its closest to
	 * @param array 
	 * 			array of centroids
	 */
	public void dataAssignmentPhase(Instance[] array) {
		for (Instance a : trainList) {
			Comparator<Instance> comparator = new DistanceComparator();
			PriorityQueue<Instance> queue = new PriorityQueue<Instance>(comparator);
			for (Instance b : array) {
				b.setDist(getDistance(a, b));
				queue.add(b);
			}
			a.setCentroid(queue.poll());
		}
	}
	
	/**
	 * Updates the centroids based on the means of each data point assigned to the centroid
	 * @param array
	 * 			array of centroids
	 * @return
	 */
	public Instance[] updateCentroidPhase(Instance[] array) {
		ArrayList<Instance> cluster1 = new ArrayList<Instance>();
		ArrayList<Instance> cluster2 = new ArrayList<Instance>();
		ArrayList<Instance> cluster3 = new ArrayList<Instance>();

		// assign each element in train list to a cluster, based on its centroid
		for (Instance i : trainList) {
			if (i.getCentroid() == array[0])
				cluster1.add(i);
			else if (i.getCentroid() == array[1])
				cluster2.add(i);
			else
				cluster3.add(i);
		}
		Instance[] temp = new Instance[3];
		temp[0] = array[0];
		temp[1] = array[1];
		temp[2] = array[2];
		// update the instances in the array to new centroids
		array[0] = getClusterMean(cluster1);
		array[1] = getClusterMean(cluster2);
		array[2] = getClusterMean(cluster3);
		// check if clusters have changed (STOPPING CONDITION)
		if (array[0].equals(temp[0]) && array[1].equals(temp[1]) && array[2].equals(temp[2])) {
			stop = true;
			dataAssignmentPhase(array);
			testKMean(array);
		}
		
		return array;

	}

	/**
	 * Finds the mean of all the data points in the cluster
	 * @param list
	 * 			all the arrays in the cluster
	 * @return
	 * 			new centroid
	 */
	public Instance getClusterMean(ArrayList<Instance> list) {
		double pw = 0;
		double pl = 0;
		double sw = 0;
		double sl = 0;

		for (Instance i : list) {
			pw += i.getPW();
			pl += i.getPL();
			sw += i.getSW();
			sl += i.getSL();
		}

		pw = pw / list.size();
		pl = pl / list.size();
		sw = sw / list.size();
		sl = sl / list.size();
		

		// create updated centroid based on cluster mean
		Instance c1 = new Instance("c1", sl, sw, pl, pw);
		return c1;
	}
	
	
	/**
	 * Tests the accuracy of the KMean algorithm
	 * @param array
	 * 			array of centroids
	 */
	public void testKMean(Instance[] array){
		ArrayList<Instance> cluster1 = new ArrayList<Instance>();
		ArrayList<Instance> cluster2 = new ArrayList<Instance>();
		ArrayList<Instance> cluster3 = new ArrayList<Instance>();

		// assign each element in train list to a cluster, based on its centroid
		for (Instance i : trainList) {
			if (i.getCentroid() == array[0])
				cluster1.add(i);
			else if (i.getCentroid() == array[1])
				cluster2.add(i);
			else
				cluster3.add(i);
		}
		setKMeanGuess(cluster1);
		setKMeanGuess(cluster2);
		setKMeanGuess(cluster3);
		
		test(trainList);

	}
	
	/**
	 * Sets the name of each data point in the cluster
	 * @param cluster
	 */
	public void setKMeanGuess(ArrayList<Instance> cluster){
		String s = "Iris-setosa";
		String vi = "Iris-virginica";
		String ve = "Iris-versicolor";
		int numS = 0;
		int numVi = 0;
		int numVe = 0;
		
		String guess = "";
		
		for(Instance i : cluster){
			if(i.getName().equals(s)) numS++;
			else if(i.getName().equals(vi)) numVi++;
			else numVe++;
		}
		
		double max = Math.max(numS, Math.max(numVi, numVe));
		
		if(max == numS) guess = s;
		else if (max == numVi) guess = vi;
		else guess = ve;
		
		for(Instance i : cluster){
			i.setGuess(guess);
		}

	}
	
	
}
