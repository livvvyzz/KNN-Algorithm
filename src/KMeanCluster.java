import java.util.ArrayList;

public class KMeanCluster {
	private ArrayList<Instance> list;
	private KNN knn;
	
	public KMeanCluster(ArrayList<Instance> list, KNN knn){
		this.list = list;
		this.knn = knn;
		//start algorithm
		//locate centroids
		generateInitCenters();
	}
	
	public void generateInitCenters(){
		
	}
}
