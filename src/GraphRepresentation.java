import java.util.ArrayList;
import java.util.LinkedList;

import Graph.AdjacencyList;
import Graph.AdjacencyMatrix;

public class GraphRepresentation {

	public static void main(String[] args) {
		AdjacencyList al = new AdjacencyList();
		ArrayList<LinkedList<String>> list = al.readInput("input.txt");
		al.print(list);
		al.createFile(list, "output-list.txt");
		
		AdjacencyMatrix am = new AdjacencyMatrix();
		byte[][] matrix = am.readInput("input.txt");
		am.print(matrix);
		am.createFile(matrix, "output-matrix.txt");
		
		al.search_DFS(list, "1");
		am.search_DFS(matrix, "1");
		
		al.search_BFS(list, "1");
		am.search_BFS(matrix, "1");
		
	}
}