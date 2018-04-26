package Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import sun.misc.Queue;

public class AdjacencyList {

	public ArrayList<LinkedList<String>> readInput (String fileName) {
		
		String[] lines = readFile(fileName);
		
		// return null if the file is null
		if (lines == null)
			return null;
		
		// creates the list of linked lists
		ArrayList<LinkedList<String>> list = new ArrayList<>();
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		
		for (int i = 1; i <= size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			// checks if the u vertex already exist in the list
			int index = searchPosition(u, list);
			
			if (index > -1) {			// if so, add the v vertex in the u list
				list.get(index).add(v);
			} else {					// otherwise, creates a new list and add u and v to it
				list.add(new LinkedList<>());
				list.get(list.size() - 1).add(u);
				list.get(list.size() - 1).add(v);
			}
			
			// checks if the v vertex already exist in the list
			index = searchPosition(v, list);
			
			if (index > -1) {			// if so, add the u vertex in the v list
				list.get(index).add(u);
			} else {					// otherwise, creates a new list and add v and u to it
				list.add(new LinkedList<>());
				list.get(list.size() - 1).add(v);
				list.get(list.size() - 1).add(u);
			}
		}
		list.trimToSize();
		return list;
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
	private int searchPosition (String vertice, ArrayList<LinkedList<String>> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				return -1;
			if (list.get(i).getFirst().equals(vertice))
				return i;
		}
		return -1;
	}
	
	public void createFile (ArrayList<LinkedList<String>> list, String fileName) {
		int vertices = list.size();
		
		int edges = 0;
		ArrayList<Integer> rateSequence = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			int size = list.get(i).size() - 1;
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
	
	/***
	 * Prints list elements
	 * @param list - list to print
	 */
	public void print (ArrayList<LinkedList<String>> list) {	
		System.out.println("Adjacency list: ");
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).toString());
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
	
	public void search_DFS (ArrayList<LinkedList<String>> list, String start) {
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
		int s = searchPosition(start, list);
		DFS_VISIT(list, s, distance, father, color);
		
		for (int u = 0; u < size; u++) {
			if (color[u] == Color.WHITE)
				DFS_VISIT(list, u, distance, father, color);
		}
		saveSearchTree (list, distance, father, "list-tree-DFS.txt");
	}
	
	private void DFS_VISIT (ArrayList<LinkedList<String>> list, int u, int[] distance, String[] father, Color[] color) {
		color[u] = Color.GRAY;
		LinkedList<String> sublist = list.get(u);
		for (int i = 0; i < sublist.size(); i++) {
			int v = searchPosition(sublist.get(i), list);
			if (color[v] == Color.WHITE) {
				father[v] = list.get(u).getFirst();
				distance[v] = distance[u] + 1;
				DFS_VISIT(list, v, distance, father, color);
			}
		}
		color[u] = Color.BLACK;
	}
	
	public void search_BFS (ArrayList<LinkedList<String>> list, String start) {
		int size = list.size();
		int s = searchPosition(start, list);
		
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
				int v = searchPosition(sublist.get(i), list);
				if (color[v] == Color.WHITE) {
					queue.add(v);
					color[v] = Color.GRAY;
					father[v] = list.get(u).getFirst();
					distance[v] = distance[u] + 1;
				}
			}
			color[u] = Color.BLACK;
		}
		saveSearchTree (list, distance, father, "list-tree-BFS.txt");
	}
	
	private void saveSearchTree (ArrayList<LinkedList<String>> list, int[] distance, String[] father, String fileName) {
		//Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
			for (int i = 0; i < distance.length; i++) {
				writer.write("| vertice: " + list.get(i).getFirst() + " | nivel: " + distance[i] + " | pai: " + father[i] + " | ");
			    writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
