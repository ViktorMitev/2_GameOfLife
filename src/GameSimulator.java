
public class GameSimulator {

	public Cell[][] table;
	public Cell[][] helpTable;
	private int rows;
	private int cols;
	
	public GameSimulator(int n, int m) {
		table = new Cell[n][m];
		helpTable = new Cell[n][m];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				table[i][j] = new Cell(false);
			}
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				helpTable[i][j] = new Cell(false);
			}
		}
		
		rows = n;
		cols = m;
	}
	
	public int getCols() {
		return cols;
	}
	
	public void setCols(int n) {
		cols = n;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int m) {
		rows = m;
	}
	
	public void Simulate() {
		int counter;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				counter = 0;
				for(int x = Math.max(i-1,0); x < Math.min(rows,i+2); x++) {
					for(int y = Math.max(j-1,0); y < Math.min(cols,j+2); y++) {
						if(table[x][y].getAlive()==true) {
							counter++;
						}
					}
				}
				if(table[i][j].getAlive()) {
					counter--;
				}
				applayRules(i, j, counter);	
			}
		}
	}
	
	public void applayRules(int x, int y, int count) {
		
		if(table[x][y].getAlive() == true && count<2) {
			helpTable[x][y].setAlive(false);
		}
		if(table[x][y].getAlive() == true && count>=2 && count<=3) {
			helpTable[x][y].setAlive(true);
		}
		if(table[x][y].getAlive() == true && count>3) {
			helpTable[x][y].setAlive(false);
		}
		if(table[x][y].getAlive() == false && count==3) {
			helpTable[x][y].setAlive(true);
		}

	}
	
	public void turn() {
		boolean ex;
		for(int i=0; i < this.table.length; i++) {
			for(int j=0; j < this.table[i].length; j++) {
				ex = this.helpTable[i][j].getAlive();
				this.table[i][j].setAlive(ex);
				helpTable[i][j].setAlive(false);
			}
		}
	}
	
}
