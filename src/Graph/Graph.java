package Graph;

public interface Graph {

	boolean readInput (String file);
	void search_DFS (String start, String output);
	void search_BFS (String start, String output);
	boolean saveGraphInfo (String file);
	int diameter();
}
