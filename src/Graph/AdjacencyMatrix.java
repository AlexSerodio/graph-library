package Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class AdjacencyMatrix {

	//HashMap<String,Integer> indexMap = new HashMap<>();
	ArrayList<String> indexList = new ArrayList<>();
	
	public byte[][] readInput (String fileName) {
		
		String[] lines = readFile(fileName);
		
		// returns null if the file is null
		if (lines == null)
			return null;
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		
		int index = 0;
		for (int i = 0; i < size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i+1].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			/*if (!indexMap.containsKey(u))
				indexMap.put(u, index++);
			if (!indexMap.containsKey(v))
				indexMap.put(v, index++);*/
			
			if (!indexList.contains(u))
				indexList.add(u);
			if (!indexList.contains(v))
				indexList.add(v);
		}
		
		//System.out.println(indexMap.toString());
		//System.out.println(indexList.toString());
		
		// creates the square matrix
		byte[][] matrix = new byte[size][size];
		
		for (int i = 0; i < size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i+1].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			//matrix[indexMap.get(u)][indexMap.get(v)] = 1;
			//matrix[indexMap.get(v)][indexMap.get(u)] = 1;
			
			matrix[indexList.indexOf(u)][indexList.indexOf(v)] = 1;
			matrix[indexList.indexOf(v)][indexList.indexOf(u)] = 1;
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
		int[] closure = new int[size];
		for (int i = 0; i < size; i++) {
			color[i] = Color.WHITE;
			distance[i] = Integer.MAX_VALUE;
			closure[i] = Integer.MAX_VALUE;
		}

		int time = 0;
		//int s = indexMap.get(start);
		int s = indexList.indexOf(start);
		time = DFS_VISIT(matrix, s, time, distance, closure, color);
		
		for (int u = 0; u < size; u++) {
			if (color[u] == Color.WHITE)
				time = DFS_VISIT(matrix, u, time, distance, closure, color);
		}
		
		System.out.println("DFS - Matrix:");
		for (int i = 0; i < size; i++)
			System.out.println(indexList.get(i) + ": " + distance[i] + "/" + closure[i]);
	}
	
	private int DFS_VISIT (byte[][] matrix, int u, int time, int[] distance, int[] closure, Color[] color) {
		color[u] = Color.GRAY;
		time += 1;
		distance[u] = time;
		for (int v = 0; v < matrix.length; v++) {
			if (matrix[u][v] > 0) {
				if (color[v] == Color.WHITE)
					time = DFS_VISIT(matrix, v, time, distance, closure, color);
			}
		}
		color[u] = Color.BLACK;
		time += 1;
		closure[u] = time;
		return time;
	}
	
	public void search_BFS (byte[][] matrix, String start) {
		int size = matrix.length;
		//int s = indexMap.get(start);
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
						//father[v] = null;
						distance[v] = distance[u] + 1;
					}
				}
			}
			color[u] = Color.BLACK;
		}
		
		System.out.println("BFS - Matrix:");
		for (int i = 0; i < size; i++)
			System.out.println(indexList.get(i) + ": " + distance[i] + " | " + father[i]);
	}
}
