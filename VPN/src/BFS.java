
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Labels each node in the graph according to the BFS distance from the start
 * node(s). If nodes are unreachable, then they are assigned a distance of -1.
 * All nodes traversed at step k are marked as predecessors of their successors
 * traversed at step k+1.
 * <p>
 * Running time is: O(m)
 * 
 * @author Scott White
 */
public class BFS<V, E> {

	private Map<V, Number> distanceDecorator = new HashMap<V, Number>();
	private List<V> mCurrentList;
	private Set<V> mUnvisitedVertices;
	private List<V> mVerticesInOrderVisited;
	private Map<V, HashSet<V>> mPredecessorMap;

	/**
	 * Creates a new BFS labeler for the specified graph and root set The
	 * distances are stored in the corresponding Vertex objects and are of type
	 * MutableInteger
	 */
	public BFS() {
		mPredecessorMap = new HashMap<V, HashSet<V>>();
	}

	/**
	 * Returns the list of vertices visited in order of traversal
	 * 
	 * @return the list of vertices
	 */
	public List<V> getVerticesInOrderVisited() {
		return mVerticesInOrderVisited;
	}

	/**
	 * Returns the set of all vertices that were not visited
	 * 
	 * @return the list of unvisited vertices
	 */
	public Set<V> getUnvisitedVertices() {
		return mUnvisitedVertices;
	}

	/**
	 * Given a vertex, returns the shortest distance from any node in the root
	 * set to v
	 * 
	 * @param g
	 *            the graph in which the distances are to be measured
	 * @param v
	 *            the vertex whose distance is to be retrieved
	 * @return the shortest distance from any node in the root set to v
	 */
	public int getDistance(Hypergraph<V, E> g, V v) {
		if (!g.getVertices().contains(v)) {
			throw new IllegalArgumentException("Vertex is not contained in the graph.");
		}

		return distanceDecorator.get(v).intValue();
	}

	/**
	 * Returns set of predecessors of the given vertex
	 * 
	 * @param v
	 *            the vertex whose predecessors are to be retrieved
	 * @return the set of predecessors
	 */
	public Set<V> getPredecessors(V v) {
		return mPredecessorMap.get(v);
	}

	protected void initialize(Hypergraph<V, E> g, Set<V> rootSet) {
		mVerticesInOrderVisited = new ArrayList<V>();
		mUnvisitedVertices = new HashSet<V>();
		for (V currentVertex : g.getVertices()) {
			mUnvisitedVertices.add(currentVertex);
			mPredecessorMap.put(currentVertex, new HashSet<V>());
		}

		mCurrentList = new ArrayList<V>();
		for (V v : rootSet) {
			distanceDecorator.put(v, new Integer(0));
			mCurrentList.add(v);
			mUnvisitedVertices.remove(v);
			mVerticesInOrderVisited.add(v);
		}
	}

	private void addPredecessor(V predecessor, V sucessor) {
		HashSet<V> predecessors = mPredecessorMap.get(sucessor);
		predecessors.add(predecessor);
	}

	public void labelDistances(Hypergraph<V, E> graph, Set<V> rootSet) {

		initialize(graph, rootSet);

		int distance = 1;
		while (true) {
			List<V> newList = new ArrayList<V>();
			for (V currentVertex : mCurrentList) {
				if (graph.containsVertex(currentVertex)) {
					for (V next : graph.getSuccessors(currentVertex)) {
						visitNewVertex(currentVertex, next, distance, newList);
					}
				}
			}
			if (newList.size() == 0)
				break;
			mCurrentList = newList;
			distance++;
		}

		for (V v : mUnvisitedVertices) {
			distanceDecorator.put(v, new Integer(-1));
		}
	}

	/**
	 * Computes the distances of all the node from the specified root node. Also
	 * keeps track of the predecessors of each node traversed as well as the
	 * order of nodes traversed.
	 * 
	 * @param graph
	 *            the graph to label
	 * @param root
	 *            the single starting vertex to traverse from
	 */
	public void labelDistances(Hypergraph<V, E> graph, V root) {
		labelDistances(graph, Collections.singleton(root));
	}

	/**
	 * Computes the distances of all the node from the starting root nodes. If
	 * there is more than one root node the minimum distance from each root node
	 * is used as the designated distance to a given node. Also keeps track of
	 * the predecessors of each node traversed as well as the order of nodes
	 * traversed.
	 * 
	 * @param graph
	 *            the graph to label
	 * @param rootSet
	 *            the set of starting vertices to traverse from
	 */
	public void labelDistances(Hypergraph<V, E> graph, Set<V> rootSet, double max_cap) {

		initialize(graph, rootSet);

		int distance = 1;
		while (true) {
			List<V> newList = new ArrayList<V>();
			for (V currentVertex : mCurrentList) {
				if (graph.containsVertex(currentVertex)) {
					for (V next : graph.getSuccessors(currentVertex)) {
						if (((Link) graph.findEdge(currentVertex, next)).getCapacity() <= max_cap)
							visitNewVertex(currentVertex, next, distance, newList);
					}
				}
			}
			if (newList.size() == 0)
				break;
			mCurrentList = newList;
			distance++;
		}

		for (V v : mUnvisitedVertices) {
			distanceDecorator.put(v, new Integer(-1));
		}
	}

	public void labelDistances(Hypergraph<V, E> graph, V root, double max_cap) {
		labelDistances(graph, Collections.singleton(root), max_cap);
	}
	
	
	
	public void labelDistancesforInsert(VPNTree tree, Hypergraph<V, E> graph, Set<V> rootSet, double max_cap) {

		initialize(graph, rootSet);

		int distance = 1;
		while (true) {
			List<V> newList = new ArrayList<V>();
			for (V currentVertex : mCurrentList) {
				if (graph.containsVertex(currentVertex)) {
					for (V next : graph.getSuccessors(currentVertex)) {
						if ((((Link) graph.findEdge(currentVertex, next)).getCapacity() <= max_cap) && !(tree.containsVertex((Node)currentVertex)))
							visitNewVertex(currentVertex, next, distance, newList);
					}
				}
			}
			if (newList.size() == 0)
				break;
			mCurrentList = newList;
			distance++;
		}

		for (V v : mUnvisitedVertices) {
			distanceDecorator.put(v, new Integer(-1));
		}
	}

	public void labelDistancesforInsert(VPNTree tree, Hypergraph<V, E> graph, V root, double max_cap) {
		labelDistancesforInsert(tree, graph, Collections.singleton(root), max_cap);
	}

/*	public void labelDistances(Hypergraph<V, E> graph, VPNTree vpnTree, Set<V> rootSet) {

		initialize(graph, rootSet);

		int distance = 1;
		boolean containTree = false;
		while (true) {
			List<V> newList = new ArrayList<V>();
			for (V currentVertex : mCurrentList) {
				if (graph.containsVertex(currentVertex)) {
					
						for (V next : graph.getSuccessors(currentVertex)) {
							if (vpnTree.containsVertex((Node) currentVertex))
								containTree=true;
							if((!vpnTree.containsVertex((Node) currentVertex)) && vpnTree.containsVertex((Node) next) && containTree)	
								continue;
							visitNewVertex(currentVertex, next, distance, newList);
						}
					
				}
			}
			if (newList.size() == 0)
				break;
			mCurrentList = newList;
			distance++;
		}

		for (V v : mUnvisitedVertices) {
			distanceDecorator.put(v, new Integer(-1));
		}
	}

	public void labelDistances(Hypergraph<V, E> graph, VPNTree vpnTree, V root) {
		labelDistances(graph, vpnTree, Collections.singleton(root));
	}
*/
	private void visitNewVertex(V predecessor, V neighbor, int distance, List<V> newList) {
		if (mUnvisitedVertices.contains(neighbor)) {
			distanceDecorator.put(neighbor, new Integer(distance));
			newList.add(neighbor);
			mVerticesInOrderVisited.add(neighbor);
			mUnvisitedVertices.remove(neighbor);
		}
		int predecessorDistance = distanceDecorator.get(predecessor).intValue();
		int successorDistance = distanceDecorator.get(neighbor).intValue();
		if (predecessorDistance < successorDistance && getPredecessors(neighbor).size() == 0) {
			addPredecessor(predecessor, neighbor);
		}
	}

	/**
	 * Must be called after {@code labelDistances} in order to contain valid
	 * data.
	 * 
	 * @return a map from vertices to minimum distances from the original
	 *         source(s)
	 */
	public Map<V, Number> getDistanceDecorator() {
		return distanceDecorator;
	}

/*	public VPNTree buildVPNTree(BFS<Node, Link> bfs) {
		VPNTree tree = new VPNTree();
		// System.out.println("build tree "+tree + ", " + tree.getMedian());
		Map<Node, java.lang.Number> distanceDecorator = new HashMap<Node, Number>();
		distanceDecorator.putAll(bfs.getDistanceDecorator());
		Node root = getKeysFromValue(distanceDecorator, 0).iterator().next();
		tree.addVertex(root);
		// System.out.println("build tree "+tree + ", " + tree.getMedian());
		distanceDecorator.remove(root);
		int counter = 1;
		while (distanceDecorator.size() > 0) {
			for (Node n : getKeysFromValue(distanceDecorator, counter)) {
				tree.addVertex(n);
				distanceDecorator.remove(n);
			}
			counter++;
		}
		// System.out.println("build tree "+tree + ", " + tree.getMedian());
		for (Node n1 : tree.getVertices()) {
			for (Node n2 : bfs.getPredecessors(n1)) {
				Link l = new Link();
				tree.addEdge(l, n1, n2, EdgeType.UNDIRECTED);
			}
		}
		// System.out.println("build tree "+tree + ", " + tree.getMedian());
		return tree;
	}*/

	public UndirectedSparseGraph<V, E> buildBFSTree() {
		UndirectedSparseGraph<V, E> tree = new UndirectedSparseGraph<>();
		Map<V, Number> distanceDecorator = new HashMap<V, Number>();
		distanceDecorator.putAll(getDistanceDecorator());
		V root = getKeysFromValue(distanceDecorator, 0).iterator().next();
		tree.addVertex(root);
		distanceDecorator.remove(root);
		int counter = 1;
		for (V n : getKeysFromValue(distanceDecorator, -1)) {
			distanceDecorator.remove(n);
		}
		while (distanceDecorator.size() > 0) {
			for (V n : getKeysFromValue(distanceDecorator, counter)) {
				tree.addVertex(n);
				distanceDecorator.remove(n);
			}
			counter++;
		}
		for (V n1 : tree.getVertices()) {
			for (V n2 : getPredecessors(n1)) {
				E l = (E) new Link();
				tree.addEdge(l, n1, n2, EdgeType.UNDIRECTED);
			}
		}
		return tree;
	}

	public <Node, Number> Set<Node> getKeysFromValue(Map<Node, Number> map, Number value) {
		Set<Node> keys = new HashSet<Node>();
		for (Entry<Node, Number> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
}
