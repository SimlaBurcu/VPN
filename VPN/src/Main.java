import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import edu.uci.ics.jung.algorithms.*;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.decorators.*;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;

public class Main {
	private static VPNTree tree;
	static UndirectedSparseGraph<Node, Link> graph;
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		FileToGraph fg = new FileToGraph();
		graph = fg.createGraph("C:\\Users\\simla\\Desktop\\Lab\\VPN\\JUNG Samples\\1000RT.brite");
		fg.mynodes.get(16).setBandwidthReq(6);
		if(graph.degree(fg.mynodes.get(16))>1){
			Node v1 = new Node(fg.mynodes.size(), 0);
			fg.mynodes.add(v1);
			graph.addEdge(new Link(), v1, fg.mynodes.get(16), EdgeType.UNDIRECTED);
			fg.mynodes.set(fg.mynodes.size()-1, fg.mynodes.get(16));
			fg.mynodes.set(16, v1);
		}
		fg.mynodes.get(9).setBandwidthReq(6);
		if(graph.degree(fg.mynodes.get(9))>1){
			Node v1 = new Node(fg.mynodes.size(), 0);
			fg.mynodes.add(v1);
			graph.addEdge(new Link(), v1, fg.mynodes.get(9), EdgeType.UNDIRECTED);
			fg.mynodes.set(fg.mynodes.size()-1, fg.mynodes.get(9));
			fg.mynodes.set(9, v1);
		}
		fg.mynodes.get(18).setBandwidthReq(6);
		if(graph.degree(fg.mynodes.get(18))>1){
			Node v1 = new Node(fg.mynodes.size(), 0);
			fg.mynodes.add(v1);
			graph.addEdge(new Link(), v1, fg.mynodes.get(18), EdgeType.UNDIRECTED);
			fg.mynodes.set(fg.mynodes.size()-1, fg.mynodes.get(18));
			fg.mynodes.set(18, v1);
		}
		fg.mynodes.get(11).setBandwidthReq(6);
		if(graph.degree(fg.mynodes.get(11))>1){
			Node v1 = new Node(fg.mynodes.size(), 0);
			fg.mynodes.add(v1);
			graph.addEdge(new Link(), v1, fg.mynodes.get(11), EdgeType.UNDIRECTED);
			fg.mynodes.set(fg.mynodes.size()-1, fg.mynodes.get(11));
			fg.mynodes.set(11, v1);
		}
		Set<Node> P = new HashSet<>();
		P.add(fg.mynodes.get(16));
		P.add(fg.mynodes.get(9));
		P.add(fg.mynodes.get(18));
		Algorithms a = new Algorithms();
		tree = a.COMPUTETREESYMMETRIC(graph, P);
		System.out.println(tree);
		tree.initialize();
		tree.prepareTree();
		//tree =  a.INSERT(tree, graph, fg.mynodes.get(5), 6);
		
		//Layout<Node, String> layout = fg.graphLayout(tree);
		for(Link l: tree.getEdges()){
			System.out.println(tree.getEndpoints(l) + ", " + l.getBandwidth());
		}

		BasicVisualizationServer<Node, String> vv = new BasicVisualizationServer<Node, String>(layout);
		vv.setPreferredSize(new Dimension(1050, 1050));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		// vv.getRenderContext().setEdgeLabelTransformer(new
		// ToStringLabeller());
		JFrame frame = new JFrame("Simple Graph View");
		JPanel container = new JPanel();
		JScrollPane scrPane = new JScrollPane(container);
		frame.add(scrPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.add(vv);
		frame.pack();
		frame.setVisible(true);
	}

//INSERT Demo
/*	public static void main(String[] args){
		tree = new VPNTree();
		graph = new UndirectedSparseGraph<>();
		
		  double B_j=10;
		  Node v1 = new Node(1, 0);
		  Node v2 = new Node(2, 0);
		  Node v3 = new Node(3, 0);
		  Node v4 = new Node(4, 0);
		  Node v5 = new Node(5, 0);
		  Node v6 = new Node(6, 0);
		  Node v7 = new Node(7, 0);
		  Node v8 = new Node(8, 0);
		  Node v9 = new Node(9, 0);
		  Node v10 = new Node(10, 0);
		  Node p1 = new Node(11, 1);
		  Node p2 = new Node(12, 2);
		  Node p3 = new Node(13, 2);
		  Node j1 = new Node(14, 4);
		  Node j2 = new Node(15, 4);
		  Node j3 = new Node(16, 4);
		  
		  
		  graph.addEdge(new Link(), v1, v2, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v2, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v5, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v6, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v7, v8, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v8, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v9, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v1, v6, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v2, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v3, v8, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v5, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p1, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p2, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p3, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j1, v6, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j2, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j3, v5, EdgeType.UNDIRECTED);
		  
		  tree.addEdge(new Link(), p1, v3, EdgeType.UNDIRECTED);
		  tree.addEdge(new Link(), v3, v8, EdgeType.UNDIRECTED);
		  tree.addEdge(new Link(), v7, v8, EdgeType.UNDIRECTED);
		  tree.addEdge(new Link(), v9, v8, EdgeType.UNDIRECTED);
		  tree.addEdge(new Link(), p2, v7, EdgeType.UNDIRECTED);
		  tree.addEdge(new Link(), p3, v9, EdgeType.UNDIRECTED);

		  BFS bfs=new BFS<Node,Link>();
		  bfs.labelDistances(graph, p1);
		  tree.initialize();
		  tree.prepareTree();
		  Algorithms a = new Algorithms();
		  
		  tree=a.INSERT(tree, graph, j1, 4);
		  tree=a.INSERT(tree, graph, j2, 4);
		  tree=a.INSERT(tree, graph, j3, 4);
		
		Layout<Node, String> layout = new StaticLayout(tree);
		layout.setSize(new Dimension(400, 400));
		layout.setLocation(v1, new Point(50, 100));
		layout.setLocation(v2, new Point(100, 100));
		layout.setLocation(v3, new Point(150, 100));
		layout.setLocation(v4, new Point(200, 100));
		layout.setLocation(v5, new Point(250, 100));
		layout.setLocation(v6, new Point(50, 150));
		layout.setLocation(v7, new Point(100, 150));
		layout.setLocation(v8, new Point(150, 150));
		layout.setLocation(v9, new Point(200, 150));
		layout.setLocation(v10, new Point(250, 150));
		layout.setLocation(p1, new Point(150, 50));
		layout.setLocation(p2, new Point(100, 200));
		layout.setLocation(p3, new Point(200, 200));
		layout.setLocation(j1, new Point(50, 200));
		layout.setLocation(j2, new Point(250, 200));
		layout.setLocation(j3, new Point(250, 50));
		
		BasicVisualizationServer<Node, String> vv = new BasicVisualizationServer<Node, String>(layout);
		vv.setPreferredSize(new Dimension(400, 400));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		JFrame frame = new JFrame("Simple Graph View");
		JPanel container = new JPanel();
		JScrollPane scrPane = new JScrollPane(container);
		frame.add(scrPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.add(vv);
		frame.pack();
		frame.setVisible(true);
		
	}
*/	
//COMPUTETREESYMMETRIC Demo	
/*	public static void main(String[] args){
		tree = new VPNTree();
		graph = new UndirectedSparseGraph<>();
		
		  double B_j=10;
		  Node v1 = new Node(1, 0);
		  Node v2 = new Node(2, 0);
		  Node v3 = new Node(3, 0);
		  Node v4 = new Node(4, 0);
		  Node v5 = new Node(5, 0);
		  Node v6 = new Node(6, 0);
		  Node v7 = new Node(7, 0);
		  Node v8 = new Node(8, 0);
		  Node v9 = new Node(9, 0);
		  Node v10 = new Node(10, 0);
		  Node p1 = new Node(11, 1);
		  Node p2 = new Node(12, 2);
		  Node p3 = new Node(13, 2);
		  Node j1 = new Node(14, 4);
		  Node j2 = new Node(15, 4);
		  Node j3 = new Node(16, 4);
		  
		  
		  graph.addEdge(new Link(), v1, v2, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v2, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v5, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v6, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v7, v8, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v8, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v9, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v1, v6, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v2, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v3, v8, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v4, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), v5, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p1, v3, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p2, v7, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), p3, v9, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j1, v6, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j2, v10, EdgeType.UNDIRECTED);
		  graph.addEdge(new Link(), j3, v5, EdgeType.UNDIRECTED);
		  

		  Algorithms a = new Algorithms();
		  
		  
		  Set<Node> P= new HashSet<>();
		  P.add(p1);
		  P.add(p2);
		  P.add(p3);
		 // P.add(j1);
		  //P.add(j2);
		  //P.add(j3);
		  tree = a.COMPUTETREESYMMETRIC(graph, P);
		
		Layout<Node, String> layout = new StaticLayout(tree);
		layout.setSize(new Dimension(400, 400));
		layout.setLocation(v1, new Point(50, 100));
		layout.setLocation(v2, new Point(100, 100));
		layout.setLocation(v3, new Point(150, 100));
		layout.setLocation(v4, new Point(200, 100));
		layout.setLocation(v5, new Point(250, 100));
		layout.setLocation(v6, new Point(50, 150));
		layout.setLocation(v7, new Point(100, 150));
		layout.setLocation(v8, new Point(150, 150));
		layout.setLocation(v9, new Point(200, 150));
		layout.setLocation(v10, new Point(250, 150));
		layout.setLocation(p1, new Point(150, 50));
		layout.setLocation(p2, new Point(100, 200));
		layout.setLocation(p3, new Point(200, 200));
		layout.setLocation(j1, new Point(50, 200));
		layout.setLocation(j2, new Point(250, 200));
		layout.setLocation(j3, new Point(250, 50));
		
		BasicVisualizationServer<Node, String> vv = new BasicVisualizationServer<Node, String>(layout);
		vv.setPreferredSize(new Dimension(400, 400));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		JFrame frame = new JFrame("Simple Graph View");
		JPanel container = new JPanel();
		JScrollPane scrPane = new JScrollPane(container);
		frame.add(scrPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.add(vv);
		frame.pack();
		frame.setVisible(true);
		
	}
*/	
}
