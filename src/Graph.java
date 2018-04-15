import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.sun.javafx.collections.MappingChange.Map;

public class Graph {

	private String[] readFile(String fileName) {

        String[] result = null;
        try {
        	String content = new String(Files.readAllBytes(Paths.get(fileName)));
            result = content.split("\n");
        } catch (IOException e) {
            e.getStackTrace();
        }

        return result;
	}
	
	public byte[][] readInputAsMatrix (String fileName) {
		
		String[] lines = readFile(fileName);
		
		// returns null if the file is null
		if (lines == null)
			return null;
		
		// gets the amount of vertex
		int size = Integer.parseInt(lines[0].trim());
		
		//HashMap<String,Integer> map = new HashMap<>();
		ArrayList<String> vertices = new ArrayList<>();
		int index = 0;
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
		}
		
		// this line is not required
		Collections.sort(vertices);
		
		//System.out.println(vertices.toString());
		
		// creates the square matrix
		byte[][] matrix = new byte[size][size];
		
		for (int i = 0; i < size; i++) {
			// separates the u and v vertex of the edge
			String[] edge = lines[i+1].split(" ");
			// gets the u (origin) vertex from the edge
			String u = edge[0].trim();
			// gets the v (destination) vertex from the edge
			String v = edge[1].trim();
			
			//System.out.println(map.get(u) + " " + map.get(v));
			matrix[vertices.indexOf(u)][vertices.indexOf(v)] = 1;
			matrix[vertices.indexOf(v)][vertices.indexOf(u)] = 1;
		}
		
		return matrix;
	}
	
	public ArrayList<LinkedList<String>> readInputAsList (String fileName) {
		
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
				list.get(list.size()-1).add(u);
				list.get(list.size()-1).add(v);
			}
			
			// checks if the v vertex already exist in the list
			index = searchPosition(v, list);
			
			if (index > -1) {			// if so, add the u vertex in the v list
				list.get(index).add(u);
			} else {					// otherwise, creates a new list and add v and u to it
				list.add(new LinkedList<>());
				list.get(list.size()-1).add(v);
				list.get(list.size()-1).add(u);
			}
		}
		list.trimToSize();
		return list;
	}
	
	/**
	 * Checks if a specific vertex already exist in the main list.
	 * Returns the index of the first occurrence of the specified element in the main list, 
	 * or -1 if the main list does not contain the element.
	 * @param vertex - element to search for
	 * @param list - list where the element will be search
	 * @return The index of the first occurrence of the specified element in the main list, 
	 * or -1 if the main list does not contain the element.
	 */
	private int searchPosition (String vertex, ArrayList<LinkedList<String>> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				return -1;
			if (list.get(i).getFirst().equals(vertex))
				return i;
		}
		return -1;
	}
	
	/***
	 * Prints list elements
	 * @param list - list to print
	 */
	private void printList (ArrayList<LinkedList<String>> list) {	
		System.out.println("Adjacency list: ");
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).toString());
		System.out.println();
	}
	
	private void printMatrix (byte[][] matrix) {
		System.out.println("Adjacency matrix: ");
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " | ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Graph graph = new Graph();
		
		ArrayList<LinkedList<String>> list = graph.readInputAsList("input.txt");
		graph.printList(list);
	
		byte[][] matrix = graph.readInputAsMatrix("input.txt");
		graph.printMatrix(matrix);
	}
}