package Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AdjacencyMatrix implements Graph {

	public byte[][] matrix;
	public ArrayList<String> vertices;
	
	public boolean readInput (String file) {
		
		String[] lines = readFile(file);
		
		// returns null if the file is null
		if (lines == null)
			return false;
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		
		// creates index list
		vertices = new ArrayList<>(size);
		// creates square matrix
		matrix = new byte[size][size];

		for (int i = 0; i < size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i+1].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			if (!vertices.contains(u))
				vertices.add(u);
			if (!vertices.contains(v))
				vertices.add(v);
			
			int m = vertices.indexOf(u);
			int n = vertices.indexOf(v);
			matrix[m][n] = 1;
			matrix[n][m] = 1;
		}
		return true;
	}
	
	public void search_DFS (String start, String output) {
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
		int s = vertices.indexOf(start);
		DFS_VISIT(s, distance, father, color);
		
		for (int u = 0; u < size; u++) {
			if (color[u] == Color.WHITE)
				DFS_VISIT(u, distance, father, color);
		}
		saveSearchTree (distance, father, output);
	}
	
	private void DFS_VISIT (int u, int[] distance, String[] father, Color[] color) {
		color[u] = Color.GRAY;
		for (int v = 0; v < matrix.length; v++) {
			if (matrix[u][v] > 0) {
				if (color[v] == Color.WHITE) {
					father[v] = vertices.get(u);
					distance[v] = distance[u] + 1;
					DFS_VISIT(v, distance, father, color);
				}
			}
		}
		color[u] = Color.BLACK;
	}
	
	public void search_BFS (String start, String output) {
		int size = matrix.length;
		int s = vertices.indexOf(start);
		
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
						father[v] = vertices.get(u);
						distance[v] = distance[u] + 1;
					}
				}
			}
			color[u] = Color.BLACK;
		}		
		saveSearchTree (distance, father, output);
	}
	
	private int search_BFS (String start, int diameter) {
		int size = matrix.length;
		int s = vertices.indexOf(start);
		
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
						father[v] = vertices.get(u);
						distance[v] = distance[u] + 1;
						if (distance[v] > diameter)
							diameter = distance[v];
					}
				}
			}
			color[u] = Color.BLACK;
		}		
		return diameter;
	}
	
	public int diameter () { 
		int diameter = 0;
		for (int i = 0; i < vertices.size(); i++)
			diameter = search_BFS (vertices.get(i), diameter);
		
		return diameter;
	}
	
	private String[] readFile (String file) {
        String[] result = null;
        try {
        	String content = new String(Files.readAllBytes(Paths.get(file)));
            result = content.split("\n");
        } catch (IOException e) {
            e.getStackTrace();
        }
        return result;
	}
	
	public boolean saveGraphInfo (String file) {
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
		
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
		    writer.write("|V| = " + vertices);
		    writer.newLine();
		    writer.write("|E| = " + edges);
		    writer.newLine();
		    writer.write("S = " + rateSequence);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean saveSearchTree (int[] distance, String[] father, String file) {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
			for (int i = 0; i < distance.length; i++) {
				writer.write("| vertice: " + vertices.get(i) + " | nivel: " + distance[i] + " | pai: " + father[i] + " | ");
			    writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("Adjacency matrix:\n   ");
		for (int i = 0; i < matrix.length; i++)
			content.append(vertices.get(i)).append("   ");
		content.append("\n");
		for (int i = 0; i < matrix.length; i++) {
			content.append(vertices.get(i)).append("| ");
			for (int j = 0; j < matrix.length; j++)
				content.append(matrix[i][j]).append(" | ");
			content.append("\n");
		}
		return content.toString();
	}
}
