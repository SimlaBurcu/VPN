import java.util.*;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class VPNTree extends UndirectedSparseGraph<Node, Link> {
	private Node median;
	private Map<Node, Set<Node>> childrenMap;
	private Map<Node, Node> parentMap;
	private BFS<Node, Link> bfs;

	public VPNTree() {
		childrenMap = new HashMap<Node, Set<Node>>();
		parentMap = new HashMap<Node, Node>();
	}
	
	public void initialize(){
		reserveEdgeBandwidths();
	}

	public boolean removeVertex(Node arg0) {
		boolean toret = super.removeVertex(arg0);
		if (toret) {
			childrenMap.remove(arg0);
			parentMap.remove(arg0);
		}
		return toret;
	}

	public void addChild(Node parent, Node child) {
		if (!childrenMap.containsKey(parent))
			childrenMap.put(parent, new HashSet<Node>());
		childrenMap.get(parent).add(child);
		parentMap.put(child, parent);
	}

	public Node getParent(Node c) {
		return parentMap.get(c);
	}

	public Set<Node> getChildren(Node p) {
		return childrenMap.get(p);
	}

	public void setMedian(Node median) {
		this.median = median;
	}

	public Node getMedian() {
		return median;
	}

	public void prepareTree() {
		bfs = new BFS<>();
		bfs.labelDistances(this, median, Double.MAX_VALUE);

		setMaps();
	}

	public Map<Node, Set<Node>> getChildrenMap() {
		return childrenMap;
	}

	public double findTotalBandwidth(Collection<Node> vertices) {
		double toret = 0;
		for (Node n : vertices) {
			toret += n.getBandwidthReq();
		}
		return toret;
	}

	public double findBandwidthOfSubtree(Node n) {
		double toret = 0;
		if (childrenMap.get(n).isEmpty())
			return n.getBandwidthReq();
		else
			for (Node c : childrenMap.get(n))
				toret += findBandwidthOfSubtree(c);
		return toret;
	}

	public ArrayList<Node> getPath(Node n1, Node n2) {
		BFS<Node, Link> bfs = new BFS<Node, Link>();
		bfs.labelDistances(this, n1);
		ArrayList<Node> list = new ArrayList<>();
		list.add(n2);
		while (bfs.getPredecessors(n2).size() > 0) {
			list.add(bfs.getPredecessors(n2).iterator().next());
			n2 = bfs.getPredecessors(n2).iterator().next();
		}
		Collections.reverse(list);
		return list;
	}

	private void reserveEdgeBandwidths() {
		ArrayList<Link> list = new ArrayList<>();
		list.addAll(getEdges());
		Set<Node> medianCandidates = new HashSet<Node>();
		medianCandidates.addAll(getVertices());
		for (int i = 0; i < list.size(); i++) {
			Link l = list.get(i);
			if (l.getBandwidth() == 0) {
				Iterator it = this.getIncidentVertices(l).iterator();
				Node n1 = (Node) it.next();
				Node n2 = (Node) it.next();
				this.removeEdge(l);
				i--;
				BFS<Node, Link> bfs = new BFS();
				bfs.labelDistances(this, n1);
				double bandwidth_n2 = findTotalBandwidth(getKeysFromValue(bfs.getDistanceDecorator(), -1));
				double bandwidth_n1 = findTotalBandwidth(getVertices()) - bandwidth_n2;
				if (bandwidth_n1 < bandwidth_n2) {
					medianCandidates.remove(n1);
					l.setBandwidth(bandwidth_n1);
				} else {
					medianCandidates.remove(n2);
					l.setBandwidth(bandwidth_n2);
				}
				this.addEdge(l, n1, n2, EdgeType.UNDIRECTED);
			}
		}
		median = medianCandidates.iterator().next();

	}

	private void setMaps() {
		childrenMap = new HashMap<Node, Set<Node>>();
		parentMap = new HashMap<Node, Node>(); 
		UndirectedSparseGraph<Node, Link> BFStree = bfs.buildBFSTree();
		for (Node n : bfs.getVerticesInOrderVisited()) {
			Set<Node> children = new HashSet<Node>();
			children.addAll(BFStree.getNeighbors(n));
			if (children.size() > 0) {
				for (Node c : children) {
					addChild(n, c);
				}
			}
			else
				childrenMap.put(n, new HashSet<>());
			BFStree.removeVertex(n);
		}
	}

	private <Node, Number> Set<Node> getKeysFromValue(Map<Node, Number> map, Number value) {
		Set<Node> keys = new HashSet<Node>();
		for (Entry<Node, Number> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	public String toString() {
		String toret = "";
		for (Node p : parentMap.keySet()) {
			toret += p + ":" + parentMap.get(p) + "\n";
		}
		return toret;
	}
}
