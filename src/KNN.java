import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;

public class KNN {
	private  ArrayList<Instance> trainList = new ArrayList<Instance>();
	private  ArrayList<Instance> testList = new ArrayList<Instance>();
	private  int k;
	private  double pwRange;
	private  double plRange;
	private  double swRange;
	private  double slRange;

	public KNN() {
		// TODO Auto-generated method stub
		File train = new File("iris-training.txt");
		File test = new File("iris-test.txt");
		makeList(train, trainList);
		makeList(test, testList);
		// get k value
		System.out.println("Please enter a k value: ");
		Scanner scan = new Scanner(System.in);
		k = scan.nextInt();
		// check k is not less than 1
		if (k >= 1) {
			knnAlgorithm();
		}
		test();
	}

	public  boolean knnAlgorithm() {


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
			for(int i = 0; i < 5; i++){
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
	public  boolean makeList(File f, ArrayList<Instance> list) {
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
		return true;
	}

	public  double getDistance(Instance a, Instance b) {
		if (setRange()) {
			double pwD = (Math.pow((a.getPW() - b.getPW()), 2)) / (Math.pow(pwRange,2));
			double plD = (Math.pow((a.getPL() - b.getPL()), 2)) / (Math.pow(plRange,2));
			double swD = (Math.pow((a.getSW() - b.getSW()), 2)) / (Math.pow(swRange,2));
			double slD = (Math.pow((a.getSL() - b.getSL()), 2)) / (Math.pow(slRange,2));
			double dsqrd = pwD + plD + swD + slD;
			double dist = Math.sqrt(dsqrd);
			return dist;
		}
		return 0;
	}

	public  boolean setRange() {

		double slMax = 0;
		double swMax = 0;
		double plMax = 0;
		double pwMax = 0;
		double slMin = 99 ^ 9;
		double swMin = 99 ^ 9;
		double plMin = 99 ^ 9;
		double pwMin = 99 ^ 9;
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
	
	public void test(){
		double pos = 0;
		for(Instance i : testList){
			if(i.getName().equals(i.getGuess())) pos++;
		}
		System.out.println("Num of correct " + pos);
		System.out.println("percentage of correct " + pos/75);
	}

}
