package Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AdjacencyMatrix {

	ArrayList<String> indexList;
	
	public byte[][] readInput (String fileName) {
		
		String[] lines = readFile(fileName);
		
		// returns null if the file is null
		if (lines == null)
			return null;
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		indexList = new ArrayList<>(size);
		// creates the square matrix
		byte[][] matrix = new byte[size][size];
		//int index = 0;
		for (int i = 0; i < size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i+1].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			if (!indexList.contains(u))
				indexList.add(u);
			if (!indexList.contains(v))
				indexList.add(v);
			
			int m = indexList.indexOf(u);
			int n = indexList.indexOf(v);
			matrix[m][n] = 1;
			matrix[n][m] = 1;
		}
		return matrix;
	}
	
	public void createFile (byte[][] matrix, String fileName) {
		int vertices = matrix.length;
		int edges = 0;
		ArrayList<Integer> rateSequence = new ArrayList<>();
		
		for (int i = 0; i < matrix.length; i++) {
			int size = 0;
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 1)
					size++;
			}
			edges += size;
			rateSequence.add(size);
		}
		edges /= 2;
		Collections.sort(rateSequence);
		
		//Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
		    writer.write("|V| = " + vertices);
		    writer.newLine();
		    writer.write("|E| = " + edges);
		    writer.newLine();
		    writer.write("S = " + rateSequence);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void print (byte[][] matrix) {
		System.out.println("Adjacency matrix: ");
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < matrix.length; j++)
				System.out.print(matrix[i][j] + " | ");
			System.out.println();
		}
		System.out.println();
	}
	
	private String[] readFile (String fileName) {
        String[] result = null;
        try {
        	String content = new String(Files.readAllBytes(Paths.get(fileName)));
            result = content.split("\n");
        } catch (IOException e) {
            e.getStackTrace();
        }
        return result;
	}
	
	public void search_DFS (byte[][] matrix, String start) {
		int size = matrix.length;
		
		Color color[] = new Color[size];
		int[] distance = new int[size];
		String[] father = new String[size];
		for (int i = 0; i < size; i++) {
			color[i] = Color.WHITE;
			distance[i] = Integer.MAX_VALUE;
			father[i] = null;
		}

		distance[0] = 0;
		int s = indexList.indexOf(start);
		DFS_VISIT(matrix, s, distance, father, color);
		
		for (int u = 0; u < size; u++) {
			if (color[u] == Color.WHITE)
				DFS_VISIT(matrix, u, distance, father, color);
		}
		saveSearchTree (indexList, distance, father, "matrix-tree-DFS.txt");
	}
	
	private void DFS_VISIT (byte[][] matrix, int u, int[] distance, String[] father, Color[] color) {
		color[u] = Color.GRAY;
		for (int v = 0; v < matrix.length; v++) {
			if (matrix[u][v] > 0) {
				if (color[v] == Color.WHITE) {
					father[v] = indexList.get(u);
					distance[v] = distance[u] + 1;
					DFS_VISIT(matrix, v, distance, father, color);
				}
			}
		}
		color[u] = Color.BLACK;
	}
	
	public void search_BFS (byte[][] matrix, String start) {
		int size = matrix.length;
		int s = indexList.indexOf(start);
		
		Color color[] = new Color[size];
		int[] distance = new int[size];
		String[] father = new String[size];
		for (int i = 0; i < size; i++) {
			color[i] = Color.WHITE;
			distance[i] = Integer.MAX_VALUE;
			father[i] = null;
		}
		
		distance[s] = 0;
		color[s] = Color.GRAY;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(s);
		
		while (!queue.isEmpty()) {
			int u = queue.removeFirst();
			for (int v = 0; v < matrix.length; v++) {
				if (matrix[u][v] > 0) {
					if (color[v] == Color.WHITE) {
						queue.add(v);
						color[v] = Color.GRAY;
						father[v] = indexList.get(u);
						distance[v] = distance[u] + 1;
					}
				}
			}
			color[u] = Color.BLACK;
		}		
		saveSearchTree (indexList, distance, father, "matrix-tree-BFS.txt");
	}
	
	private void saveSearchTree (ArrayList<String> index, int[] distance, String[] father, String fileName) {
		//Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
			for (int i = 0; i < distance.length; i++) {
				writer.write("| vertice: " + index.get(i) + " | nivel: " + distance[i] + " | pai: " + father[i] + " | ");
			    writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
