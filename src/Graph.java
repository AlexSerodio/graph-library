import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
			String u = edge[0];
			// gets the v (destination) vertex from the edge
			String v = edge[1];
			
			// checks if the u vertex already exist in the list
			int index = searchPosition(u, list);
			
			if (index > -1) {			// if so, add the v vertex in the u list
				list.get(index).add(v);
			} else {					// otherwise, creates a new list and add u and v to it
				list.add(new LinkedList<>());
				list.get(list.size()-1).add(u);
				list.get(list.size()-1).add(v);
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
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).toString());
	}

	public static void main(String[] args) {
		Graph graph = new Graph();
		ArrayList<LinkedList<String>> list = graph.readInputAsList("input.txt");
		graph.printList(list);
	}
}