
public class Link {
	private int id;
	private double reservedBandwidth;
	private double capacity;
	private static int edgeCount = 1;

	public Link() {
		this.id = edgeCount++; 
		this.reservedBandwidth = 0;
		this.capacity = Double.MAX_VALUE;
	}
	
	public Link(double reservedBandwidth) {
		this.id = edgeCount++; 
		this.reservedBandwidth = reservedBandwidth;
		this.capacity = Double.MAX_VALUE;
	}

	public void setBandwidth(double bandwidth) {
		this.reservedBandwidth = bandwidth;
	}

	public double getBandwidth() {
		return this.reservedBandwidth;
	}

	public void updateBandwidth(double delta) {
		this.reservedBandwidth = this.reservedBandwidth + delta;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getCapacity() {
		return this.capacity;
	}

	public String toString() {
		return "E" + id;
	}
}
