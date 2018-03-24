
public class Instance {
	
	private double sepL;
	private double sepW;
	private double petL;
	private double petW;
	private String name;
	private double distance;
	private String guess;
	
	//-----for KMEAM CLUSTER
	private Instance centroid;
	
	public Instance(String name, double sepL, double sepW, double petL, double petW){
		this.sepL = sepL;
		this.sepW = sepW;
		this.petL = petL;
		this.petW = petW;
		this.name = name;
	}
	
	
	public double getPL(){
		return petL;
	}
	
	
	public double getPW(){
		return petW;
	}
	
	
	public double getSL(){
		return sepL;
	}
	
	
	public double getSW(){
		return sepW;
	}
	
	public String getName(){
		return name;
	}
	
	public double getDist(){
		return distance;
	}
	
	public void setDist(Double d){
		this.distance= d;
	}
	
	public void setGuess(String guess){
		this.guess = guess;
	}
	
	public String getGuess(){
		return guess;
	}
	
	public void setCentroid(Instance c){
		this.centroid = c;
	}
	
	public Instance getCentroid(){
		return centroid;
	}

	public boolean equals(Object o){
		if(o instanceof Instance){
			Instance i = (Instance) o;
			return getPW() == i.getPW() && getPL() == i.getPL() && getSW() == i.getSW() && getSL() == i.getSL();
		}
		return false;
	}
}
