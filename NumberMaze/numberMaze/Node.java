package numberMaze;

import java.util.Arrays;

public class Node {

	public int number;
	public int[] handle;
	public boolean isSelected;
	public boolean visited;
	
	public Node(int number){
		this.number = number;
		this.handle = new int[2];
		this.isSelected = false;
		this.visited = false;
	}

	@Override
	public String toString() {
		return "Node [number=" + number + ", handle=" + Arrays.toString(handle)
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(handle);
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (!Arrays.equals(handle, other.handle))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

}
