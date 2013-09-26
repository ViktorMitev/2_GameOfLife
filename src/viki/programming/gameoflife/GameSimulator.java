package viki.programming.gameoflife;


public class GameSimulator {

	private boolean[][] table;
	
	public GameSimulator(int n, int m) {
		table = new boolean[n][m];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				table[i][j] = false;
			}
		}
	}
	
	public boolean[][] getTable() {
		return table;
	}
	
	public void setTable(boolean[][] table) {
		this.table = table;
	}
	
	public boolean getAlive(int i, int j) {
		return table[i][j];
	}
	
	public void setAlive(int i, int j, boolean state) {
		table[i][j] = state;
	}
	
	public int getCols() {
		return table[0].length;
	}
	
	public int getRows() {
		return table.length;
	}
	
	private boolean isInside(int i, int j) {
		if(i >= 0 && i < getRows() && j >= 0 && j < getCols()) {
			return true;
		}
		return false;
	}
	
	private int checkAroundForNeighbours(int i, int j) {
		int counter = 0;
		
		for(int x = i-1; x <= i+1; x++) {
			for(int y = j-1; y <= j+1; y++) {
				if(isInside(x, y) && table[x][y]) {
					counter++;
				}
			}
		}
		return counter;
	}

	void simulate() {
		int counter;
		boolean[][] helpTable = new boolean[getRows()][getCols()];
		
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				counter = checkAroundForNeighbours(i, j);
				if (table[i][j]) {
					counter--;
				}
				helpTable[i][j] = applayRules(i, j, counter);
			}
		}
		exchange(helpTable);
	}
	
	public boolean applayRules(int x, int y, int count) {
		
		if( table[x][y] && count==2 || count==3) {
			return true;
		}
		else {
			return false;
		}
 
	}
	
	public void exchange(boolean[][] tmp) {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				table[i][j] = tmp[i][j];
				tmp[i][j] = false;
			}
		}
	}
	
	public void setAllFalse() {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				table[i][j] = false;
			}
		}
	}
	
}
