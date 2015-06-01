package numberMaze;

import java.util.ArrayList;

public class Maze {

	public Node[][] nodeArray;
	public int targetNum;
	
	public Maze(int row, int col, int maxNumber){
		
		// Generate the array
		this.nodeArray = genArray(row, col, maxNumber);
		
		// Find path
		boolean hasCycle = false;
		int sum = 0;
		int cycleCount = 0;
		do {
			hasCycle = false;
			System.out.println(" Cycle count is: " + cycleCount);
			Node node = this.nodeArray[0][0];
			node.visited = true;
			sum = node.number;
			// Hasn't reached the end
			while (!node.equals(this.nodeArray[row-1][col-1])){
				int[] nextNodeCoord = findNextNode(node.handle[0], node.handle[1], this.nodeArray); // step
				System.out.print(nextNodeCoord[0] + ", " + nextNodeCoord[1]);
				if (nextNodeCoord[0] == -1){	// no where to go
					hasCycle = true;
					break;	// try again
				} else {
					node = this.nodeArray[nextNodeCoord[0]][nextNodeCoord[1]];
					sum = sum + node.number;
					System.out.println(" ..." + node.number);
				}
			}
			cycleCount++;
			resetVisited();
		} while (hasCycle); // Keep trying to find path if there is a cycle
		System.out.println("Sum is " + sum);
		this.targetNum = sum;
		
	}
	
	// Generates an array of nodes with an inputed size of row and column. Gives each node a randomized number
	// from 0 to the specified maximum. 
	public static Node[][] genArray(int row, int col, int maxNumber){
		Node[][] testArray = new Node[row][col];
		for (int i = 0; i < testArray.length; i++){
			for (int j = 0; j < testArray[i].length; j++){
				testArray[i][j] = new Node((int)(Math.random()*(maxNumber+1)));
				testArray[i][j].handle[0] = i;
				testArray[i][j].handle[1] = j;
				System.out.println("Position " + i + ", " + j + " has the number " + testArray[i][j].number);
			}
		}
		return testArray;
	}
	
	// Method to randomize the direction that the path takes. When random direction is 0,
	// the path turns up; 1, right; 2, down; 3, left. Does not revisited Nodes that already have 
	// been visited. Returns the coordinates of the next node (aka the handle), or -1's if all 
	// surrounding nodes have been visited.
	public static int[] findNextNode(int x, int y, Node[][] array){
		ArrayList<Node> toBeVisited = new ArrayList<Node>();
		// Check right
		try {
			if (!array[x][y+1].visited)
				toBeVisited.add(array[x][y+1]);
		} catch (Exception e) {
		}
		// Check down
		try {
			if (!array[x+1][y].visited)
				toBeVisited.add(array[x+1][y]);
		} catch (Exception e) {
		}
		// Check left
		try {
			if (!array[x][y-1].visited)
				toBeVisited.add(array[x][y-1]);
		} catch (Exception e) {
		}
		// Check up
		try {
			if (!array[x-1][y].visited)
				toBeVisited.add(array[x-1][y]);
		} catch (Exception e) {
		}
		if (toBeVisited.size() == 0){
			int[] returnArray = {-1, -1};
			return returnArray;
		} else {
			Node randomNode = toBeVisited.get((int)(Math.random()*toBeVisited.size()));
			randomNode.visited = true;
			return randomNode.handle;
		}
	}
	
	// Sets all the nodes in the array to unvisited.
	public void resetVisited(){
		for (int i = 0; i < this.nodeArray.length; i++){
			for (int j = 0; j < this.nodeArray[i].length; j++){
				this.nodeArray[i][j].visited = false;
			}
		}
	}

}
