import Graph.AdjacencyList;
import Graph.AdjacencyMatrix;

public class Main {

	public static void main(String[] args) {
		
		String directory = "files//";
		long start, estimateTime;
		long beforeUsedMem, afterUsedMem, actualMemUsed;
		String[] inputs = {"teste1.txt", "teste2.txt", "teste3.txt"};
		String[] vertices = {"1764", "995", "2", "2177", "11616", "11", "899", "88", "931", "21"};
		
		AdjacencyList list = new AdjacencyList();
		
		start = System.currentTimeMillis();
		beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		list.readInput(directory + inputs[0]);
		afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		estimateTime = System.currentTimeMillis() - start;
		actualMemUsed = afterUsedMem - beforeUsedMem;
		System.out.println("Input file: " + inputs[0]);
		System.out.println("list readInput time: " + (estimateTime/1000.0) + " seconds");
		System.out.println("list memory usage: " + actualMemUsed + " bytes");
				
		System.out.println("Vertice: " + vertices[0]);
		start = System.currentTimeMillis();
		list.search_DFS(vertices[0], directory + "list-tree-DFS.txt");
		estimateTime = System.currentTimeMillis() - start;
		System.out.println("list search_DFS time: " + (estimateTime/1000.0) + " seconds");

		start = System.currentTimeMillis();
		list.search_BFS(vertices[0], directory + "list-tree-BFS.txt");
		estimateTime = System.currentTimeMillis() - start;
		System.out.println("list search_BFS time: " + (estimateTime/1000.0) + " seconds");
		
		
		AdjacencyMatrix matrix = new AdjacencyMatrix();
		
		start = System.currentTimeMillis();
		beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		matrix.readInput(directory + inputs[0]);
		afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		estimateTime = System.currentTimeMillis() - start;
		actualMemUsed = afterUsedMem - beforeUsedMem;
		System.out.println("Input file: " + inputs[0]);
		System.out.println("matrix readInput time: " + (estimateTime/1000.0) + " seconds");
		System.out.println("matrix memory usage: " + actualMemUsed + " bytes");
				
		System.out.println("Vertice: " + vertices[0]);
		start = System.currentTimeMillis();
		matrix.search_DFS(vertices[0], directory + "matrix-tree-DFS.txt");
		estimateTime = System.currentTimeMillis() - start;
		System.out.println("matrix search_DFS time: " + (estimateTime/1000.0) + " seconds");

		start = System.currentTimeMillis();
		matrix.search_BFS(vertices[0], directory + "matrix-tree-BFS.txt");
		estimateTime = System.currentTimeMillis() - start;
		System.out.println("matrix search_BFS time: " + (estimateTime/1000.0) + " seconds");

		System.out.println("done");
	}
}