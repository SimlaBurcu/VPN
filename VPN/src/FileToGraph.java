
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class FileToGraph {
	static int nodeSize;
	static int edgeSize;
	
	static ArrayList<Integer> nodes=new ArrayList<>();
	static ArrayList<Integer> xCoordinates=new ArrayList<>();
	static ArrayList<Integer> yCoordinates=new ArrayList<>();
	
	static ArrayList<Integer> edges=new ArrayList<>();
	static ArrayList<Integer> from=new ArrayList<>();
	static ArrayList<Integer> to=new ArrayList<>();
	
	static UndirectedSparseGraph<Node, Link> g = new UndirectedSparseGraph<Node, Link>();
	static ArrayList <Node> mynodes = new ArrayList<>();
	
	public static void fileToGraph(String file) throws FileNotFoundException, IOException{
		//"C:\\Users\\simla\\Desktop\\JUNG Samples\\20RT.brite"
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    int counter = 1;
		    String line = br.readLine();

		    while (line != null) {
		    	if(counter==1){
		    		nodeSize=Integer.parseInt(line.substring(line.indexOf("(")+2, line.indexOf("Nodes")-1));
		    		edgeSize=Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf("Edges")-1));
		    	}
		    	if(counter>=5 && counter<5+nodeSize){
		    		nodes.add(Integer.parseInt(line.split("\t")[0]));
		    		xCoordinates.add(Integer.parseInt(line.split("\t")[1]));
		    		yCoordinates.add(Integer.parseInt(line.split("\t")[2]));
		    	}
		    	if(counter>7+nodeSize && counter<=7+nodeSize+edgeSize){
		    		edges.add(Integer.parseInt(line.split("\t")[0]));
		    		from.add(Integer.parseInt(line.split("\t")[1]));
		    		to.add(Integer.parseInt(line.split("\t")[2]));
		    	}
		        line = br.readLine();
		        counter++;
		    }
		    System.out.println("okudu");
		}
	}
	
	public static UndirectedSparseGraph<Node, Link> createGraph(String file) throws FileNotFoundException, IOException {
		fileToGraph(file);

		

		for (int i = 0; i < nodeSize; i++) {
			mynodes.add(new Node(nodes.get(i), 0.0));// ******************bandwidthreqs
		}

		for (int i = 0; i < edgeSize; i++) {
			g.addEdge(new Link(), mynodes.get(from.get(i)), mynodes.get(to.get(i)), EdgeType.UNDIRECTED);
		}
		
		return g;
	}
	
	public static Layout graphLayout(UndirectedSparseGraph<Node, Link> graph){
		Layout<Node, String> layout = new StaticLayout(graph);
		layout.setSize(new Dimension(1000, 1000));
		for (int i = 0; i < nodeSize; i++) {
			layout.setLocation(mynodes.get(i), new Point(xCoordinates.get(i)/2,yCoordinates.get(i)/2));
			//System.out.println(i);
			//mynodes.add(new MyNode(fg.nodes.get(i), 0.0));
		}
		return layout;
	}
}
