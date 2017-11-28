import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class Algorithms {

	private BFS<Node, Link> runBFS(VPNTree VPNtree, UndirectedSparseGraph<Node, Link> graph, Node j, double B_j) {
		for (Link l : VPNtree.getEdges()) {
			Iterator it = VPNtree.getIncidentVertices(l).iterator();
			Node n1 = (Node) it.next();
			Node n2 = (Node) it.next();
			graph.removeEdge(graph.findEdge(n1, n2));
		}
		BFS<Node, Link> bfs = new BFS<>();
		bfs.labelDistancesforInsert(VPNtree, graph, j, Double.MAX_VALUE);
		for (Link l : VPNtree.getEdges()) {
			Iterator it = VPNtree.getIncidentVertices(l).iterator();
			Node n1 = (Node) it.next();
			Node n2 = (Node) it.next();
			graph.addEdge(new Link(), n1, n2, EdgeType.UNDIRECTED);
		}
		return bfs;
	}

	private int find_d_aj(UndirectedSparseGraph<Node, Link> graph, BFS<Node, Link> bfs, Node a, Node j) {
		int distance = 0;
		if (bfs.getDistance(graph, a) < 0) {
			BFS<Node, Link> newbfs = new BFS<>();
			newbfs.labelDistances(graph, a);
			distance = newbfs.getDistance(graph, j);
		} else
			distance = bfs.getDistance(graph, a);
		return distance;
	}

	private void markRevolting(VPNTree VPNtree, UndirectedSparseGraph<Node, Link> graph, BFS<Node, Link> bfs,
			double B_j, double total_bandwidth) {
		for (Node r : VPNtree.getVertices()) {
			if (r.getBandwidthReq()>0)
				continue;
			double subtreeBandwidth = VPNtree.findBandwidthOfSubtree(r);
			//System.out.println(r + ": " + subtreeBandwidth + "," + total_bandwidth);
			if ((subtreeBandwidth + B_j) > (total_bandwidth - subtreeBandwidth)){
				r.setRevolting();
			}
				
		}
	}

	private Map<Node, Double> initializeCostMap(VPNTree VPNtree) {
		Map<Node, Double> insertion_costs = new HashMap<Node, Double>();
		for (Node n : VPNtree.getVertices()) {
			if (n.getBandwidthReq() == 0.0)
				insertion_costs.put(n, Double.MAX_VALUE);
			else
				insertion_costs.put(n, 0.0);
		}
		return insertion_costs;
	}

	public VPNTree COMPUTETREESYMMETRIC(UndirectedSparseGraph<Node, Link> graph, Set<Node> P) {
		VPNTree TreeOPT = new VPNTree();
		double C_T_opt = Double.MAX_VALUE;
		for (Node v : graph.getVertices()) {
			VPNTree Tree_v = new VPNTree();
			Tree_v.addChild(null, v);
			double C_T_v = 0;
			Queue<Node> openQ = new LinkedList();
			openQ.add(v);
			while (openQ.size() > 0) {
				Node u = openQ.poll();
				//u.cleanNodeInfo();
				//u.setParent(v);
				for (Link l : graph.getIncidentEdges(u)) {
					Node w = graph.getOpposite(u, l);
					if (!Tree_v.containsVertex(w)) {
						Tree_v.addVertex(w);
						Tree_v.addEdge(new Link(), w, u, EdgeType.UNDIRECTED);
						Tree_v.addChild(u, w);
						//u.addChild(w);
						openQ.add(w);
					}
				}
			}
			// prune & calculate cost//********edge bandwidth ayarla
			VPNTree Tree_v2 = new VPNTree();
			for(Node n: P){
				int depth = 0;
				double B_n = n.getBandwidthReq(); 
				while(n != v){
					depth++;
					Tree_v2.addVertex(n);
					Node p = Tree_v.getParent(n);
					Tree_v2.addEdge(Tree_v.findEdge(n, p), n, p, EdgeType.UNDIRECTED);
					Tree_v2.addChild(p, n);
					n = p;
				}
				C_T_v+= depth*B_n;
			}
			Tree_v2.addChild(null, v);
			Tree_v = Tree_v2;
			
			if (C_T_v < C_T_opt) {
				TreeOPT = Tree_v;
				TreeOPT.setMedian(v);
				C_T_opt = C_T_v;
			}
		}
		System.out.println(C_T_opt);
		System.out.println(TreeOPT.getMedian());
		TreeOPT.prepareTree();
		return TreeOPT;
	}

	public VPNTree INSERT(VPNTree VPNtree, UndirectedSparseGraph<Node, Link> graph, Node j, double B_j) {
		BFS<Node, Link> bfs = runBFS(VPNtree, graph, j, B_j);
		Map<Node, Set<Node>> childrenMap = VPNtree.getChildrenMap();
		double total_bandwidth = VPNtree.findTotalBandwidth(VPNtree.getVertices());
		Node candidate = null;
		double cost_v_min = Double.MAX_VALUE;
		Node v_min = null;
		Map<Node, Double> insertion_costs = initializeCostMap(VPNtree);
		insertion_costs.put(v_min, Double.MAX_VALUE);
		insertion_costs.put(VPNtree.getMedian(), 0.0);
		Queue<Node> openQ = new LinkedList();
		openQ.add(VPNtree.getMedian());

		if (total_bandwidth <= B_j) {
			candidate = j;
			while (openQ.size() > 0) {
				Node u = openQ.poll();
				for (Node r : childrenMap.get(u)) {
					if (r.getBandwidthReq() == 0.0) {
						double toput = insertion_costs.get(u) + total_bandwidth - 2 * VPNtree.findBandwidthOfSubtree(r);
						insertion_costs.put(r, toput);
						openQ.add(r);
					}
				}
				double cost_u = insertion_costs.get(u) + find_d_aj(graph, bfs, u, j) * B_j;
				// System.out.println(u+","+find_d_aj(graph, bfs, u, j));
				if (cost_u < cost_v_min) {
					v_min = u;
					cost_v_min = cost_u;
				}
			}
			// *******boyut kontrol!!!!
			ArrayList<Node> path = VPNtree.getPath(v_min, VPNtree.getMedian());
			//System.out.println(v_min);
			double B_Tr = VPNtree.findBandwidthOfSubtree(path.get(path.size() - 2));
			for (int i = 0; i < path.size() - 1; i++) {
				VPNtree.findEdge(path.get(i), path.get(i + 1)).updateBandwidth(total_bandwidth - 2 * B_Tr);
			}

			Node pointer = v_min;
			while (!pointer.equals(j)) {
				Node n = (Node) bfs.getPredecessors(pointer).iterator().next();
				if (!VPNtree.containsVertex(n))
					VPNtree.addVertex(n);
				if (VPNtree.findEdge(pointer, n) == null)
					VPNtree.addEdge(new Link(total_bandwidth), pointer, n, EdgeType.UNDIRECTED);
				pointer = n;
			}
			for (Link l : VPNtree.getEdges())
				System.out.println(VPNtree.getEndpoints(l) + ", " + l.getBandwidth());
		} else {
			markRevolting(VPNtree, graph, bfs, B_j, total_bandwidth);
			while (openQ.size() > 0) {
				Node u = openQ.poll();
				for (Node v : childrenMap.get(u)) {
					System.out.println(v);
					if (v.getBandwidthReq() == 0.0) {
						//System.out.println(v);
						if (v.isRevolting()
								&& ((total_bandwidth - (2 * VPNtree.findBandwidthOfSubtree(v)) - B_j) < 0)) {
							v.setCandidate();
							;
							double toput = insertion_costs.get(u) + total_bandwidth
									- 2 * VPNtree.findBandwidthOfSubtree(v);
							insertion_costs.put(v, toput);
							//System.out.println(toput);
						} else {
							double toput = insertion_costs.get(u) + B_j;
							insertion_costs.put(v, toput);
						}
						openQ.add(v);
					}
				}
				//System.out.println(insertion_costs.get(u));
				double cost_u = insertion_costs.get(u) + find_d_aj(graph, bfs, u, j) * B_j;
				//System.out.println(cost_u);
				if (cost_u < cost_v_min) {
					v_min = u;
					cost_v_min = cost_u;
				}
				if (cost_u == cost_v_min) {
					int d_u = find_d_aj(graph, bfs, u, j);
					int d_v = find_d_aj(graph, bfs, v_min, j);
					if(d_u < d_v){
						v_min = u;
						cost_v_min = cost_u;
					}
				}
				//System.out.println("deneme "+ u + ", " + cost_u);
			}
			System.out.println(cost_v_min);
			ArrayList<Node> path_vm = VPNtree.getPath(v_min, VPNtree.getMedian());
			candidate = v_min;
			if(path_vm.size()==1)
				candidate.setCandidate();
			int ind = 0;
			while (!candidate.isCandidate()) {
				candidate = path_vm.get(++ind);
			}

			ArrayList<Node> path_cm = VPNtree.getPath(candidate, VPNtree.getMedian());
			if (path_cm.size() != 1) {
				for (int i = 0; i < path_cm.size() - 1; i++) {
					VPNtree.findEdge(path_cm.get(i), path_cm.get(i + 1))
							.updateBandwidth(total_bandwidth - 2 * VPNtree.findBandwidthOfSubtree(path_cm.get(i)));
				}
			}

			ArrayList<Node> path_vc = VPNtree.getPath(v_min, candidate);
			//System.out.println(candidate);
			if (path_vc.size() != 1) {
				for (int i = 0; i < path_vc.size() - 1; i++) {
					VPNtree.findEdge(path_vc.get(i), path_vc.get(i + 1)).updateBandwidth(B_j);
				}
			}

			Node pointer = v_min;
			while (!pointer.equals(j)) {
				//System.out.println(pointer);
				Node n = (Node) bfs.getPredecessors(pointer).iterator().next();
				if (!VPNtree.containsVertex(n))
					VPNtree.addVertex(n);
				if (VPNtree.findEdge(pointer, n) == null){
					VPNtree.addEdge(new Link(B_j), pointer, n, EdgeType.UNDIRECTED);
				}
					
				pointer = n;
			}

		}
		VPNtree.setMedian(candidate);
		/*for (Link l : VPNtree.getEdges())
			System.out.println(l + "," + VPNtree.getEndpoints(l) + ", " + l.getBandwidth());*/
		for(Node n:VPNtree.getVertices())
			n.clearNode();
		System.out.println("median: "+VPNtree.getMedian());
		VPNtree.prepareTree();
		return VPNtree;
	}
}
