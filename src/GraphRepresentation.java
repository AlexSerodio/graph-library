import Graph.Graph;
import Graph.AdjacencyList;
import Graph.AdjacencyMatrix;

public class GraphRepresentation {

	public static void main(String[] args) {
		
		String directory = "files//";
		
		//Graph graph;
		AdjacencyMatrix matrix = new AdjacencyMatrix();
		AdjacencyList list = new AdjacencyList();

		list.readInput(directory + "input.txt");
		list.saveGraphInfo(directory + "output-list.txt");
		
		matrix.readInput(directory + "input.txt");
		matrix.saveGraphInfo(directory + "output-matrix.txt");
		
		list.search_DFS("1", directory + "list-tree-DFS.txt");
		list.search_BFS("1", directory + "list-tree-BFS.txt");		
		
		matrix.search_DFS("1", directory + "matrix-tree-DFS.txt");
		matrix.search_BFS("1", directory + "matrix-tree-DFS.txt");
		
		//System.out.println(list.toString());
		//System.out.println(matrix.toString());
		
		// DIAMETER TEST
		
		int diameter;
		long start, estimateTime;
		
		//start = System.currentTimeMillis();
		start = System.nanoTime();
		diameter = list.diameter();
		//estimateTime = System.currentTimeMillis() - start;
		estimateTime = System.nanoTime() - start;
		System.out.print("list:\ndiameter: " + diameter);
		System.out.println(" | time: " + estimateTime);
		
		
		//start = System.currentTimeMillis();
		start = System.nanoTime();
		diameter = matrix.diameter();
		//estimateTime = System.currentTimeMillis() - start;
		estimateTime = System.nanoTime() - start;
		System.out.print("matrix:\ndiameter: " + diameter);
		System.out.println(" | time: " + estimateTime);
		
		System.out.println("done");
	}
}