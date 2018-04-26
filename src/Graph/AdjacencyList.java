package Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AdjacencyList implements Graph {

	public ArrayList<LinkedList<String>> list;
	
	public boolean readInput (String file) {
		
		String[] lines = readFile(file);
		
		// return null if the file is null
		if (lines == null)
			return false;
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		
		// creates the list of linked lists
		list = new ArrayList<>(size);
		
		for (int i = 1; i <= size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			// checks if the u vertex already exist in the list
			int index = searchPosition(u);
			
			if (index > -1) {			// if so, add the v vertex in the u list
				list.get(index).add(v);
			} else {					// otherwise, creates a new list and add u and v to it
				list.add(new LinkedList<>());
				list.get(list.size() - 1).add(u);
				list.get(list.size() - 1).add(v);
			}
			
			// checks if the v vertex already exist in the list
			index = searchPosition(v);
			
			if (index > -1) {			// if so, add the u vertex in the v list
				list.get(index).add(u);
			} else {					// otherwise, creates a new list and add v and u to it
				list.add(new LinkedList<>());
				list.get(list.size() - 1).add(v);
				list.get(list.size() - 1).add(u);
			}
		}
		return true;
	}
	
	public void search_DFS (String start, String output) {
		int size = list.size();
		
		Color color[] = new Color[size];
		int[] distance = new int[size];
		String[] father = new String[size];
		for (int i = 0; i < size; i++) {
			color[i] = Color.WHITE;
			distance[i] = Integer.MAX_VALUE;
			father[i] = null;
		}

		distance[0] = 0;
		int s = searchPosition(start);
		DFS_VISIT(s, distance, father, color);
		
		for (int u = 0; u < size; u++) {
			if (color[u] == Color.WHITE)
				DFS_VISIT(u, distance, father, color);
		}
		saveSearchTree (distance, father, output);
	}
	
	private void DFS_VISIT (int u, int[] distance, String[] father, Color[] color) {
		color[u] = Color.GRAY;
		LinkedList<String> sublist = list.get(u);
		for (int i = 0; i < sublist.size(); i++) {
			int v = searchPosition(sublist.get(i));
			if (color[v] == Color.WHITE) {
				father[v] = list.get(u).getFirst();
				distance[v] = distance[u] + 1;
				DFS_VISIT(v, distance, father, color);
			}
		}
		color[u] = Color.BLACK;
	}
	
	public void search_BFS (String start, String output) {
		int size = list.size();
		int s = searchPosition(start);
		
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
			LinkedList<String> sublist = list.get(u);
			for (int i = 0; i < sublist.size(); i++) {
				int v = searchPosition(sublist.get(i));
				if (color[v] == Color.WHITE) {
					queue.add(v);
					color[v] = Color.GRAY;
					father[v] = list.get(u).getFirst();
					distance[v] = distance[u] + 1;
				}
			}
			color[u] = Color.BLACK;
		}
		saveSearchTree (distance, father, output);
	}
	
	private int search_BFS (String start, int diameter) {
		int size = list.size();
		int s = searchPosition(start);
		
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
			LinkedList<String> sublist = list.get(u);
			for (int i = 0; i < sublist.size(); i++) {
				int v = searchPosition(sublist.get(i));
				if (color[v] == Color.WHITE) {
					queue.add(v);
					color[v] = Color.GRAY;
					father[v] = list.get(u).getFirst();
					distance[v] = distance[u] + 1;
					if (distance[v] > diameter)
						diameter = distance[v];
				}
			}
			color[u] = Color.BLACK;
		}
		return diameter;
	}
	
	public int diameter () {
		int diameter = 0;
		for (int i = 0; i < list.size(); i++)
			diameter = search_BFS (list.get(i).getFirst(), diameter);
		
		return diameter;
	}
	
	/**
	 * Checks if a specific vertex already exist in the main list.
	 * Returns the index of the first occurrence of the specified element in the main list, 
	 * or -1 if the main list does not contain the element.
	 * @param vertice - element to search for
	 * @param list - list where the element will be search
	 * @return The index of the first occurrence of the specified element in the main list, 
	 * or -1 if the main list does not contain the element.
	 */
	private int searchPosition (String vertice) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				return -1;
			if (list.get(i).getFirst().equals(vertice))
				return i;
		}
		return -1;
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
		
		if (list == null)
			return false;
		
		int vertices = list.size();
		
		int edges = 0;
		ArrayList<Integer> rateSequence = new ArrayList<>(vertices);
		for (int i = 0; i < list.size(); i++) {
			int size = list.get(i).size() - 1;
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
				writer.write("| vertice: " + list.get(i).getFirst() + " | nivel: " + distance[i] + " | pai: " + father[i] + " | ");
			    writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public String toString () {
		StringBuilder content = new StringBuilder();
		content.append("Adjacency list:\n");
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++)
				content.append(list.get(i).get(j).toString()).append(" -> ");
			content.append("null\n");
		}
		return content.toString();
	}
}
