import java.util.*;

public class Node {
	private int id;
	private double bandwidthReq;
	private boolean isRevolting = false;
	private boolean isCandidate = false;
	//private Node parent = null;
	//private Set<Node> children = new HashSet<>();

	public Node() {
		this.id = -1;
		this.bandwidthReq = 0;
		this.isRevolting = false;
	}

	public Node(int id, double bandwidthReq) {
		this.id = id;
		this.bandwidthReq = bandwidthReq;
		this.isRevolting = false;
	}
	/*
	public Node getParent(){
		return parent;
	}
	
	public Set<Node> getChildren(){
		return children;
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	public void addChild(Node child){
		children.add(child);
	}
	
	public void cleanNodeInfo(){
		parent = null;
		children = new HashSet<>();
	}
	*/
	
	public void clearNode(){
		isCandidate = false;
		isRevolting = false;
	}
	
	public double getBandwidthReq() {
		return this.bandwidthReq;
	}
	
	public void setBandwidthReq(double bandwidthReq){
		this.bandwidthReq = bandwidthReq;
	}
	
	public boolean isRevolting(){
		return isRevolting;
	}
	
	public void setRevolting(){
		this.isRevolting = true;
	}
	
	public boolean isCandidate(){
		return isCandidate;
	}
	
	public void setCandidate(){
		this.isCandidate = true;
	}
	
	public String toString() {
		return "V" + id;
	}
}
