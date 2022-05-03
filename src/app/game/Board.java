package app.game;

import java.util.Hashtable;

public class Board {
	private Hashtable <String, String> boardMap;
	
	/*
	 * Ship Count:
	 * Small: 1
	 * Medium-Small : 2
	 * Medium-Large : 1
	 * Large : 1
	 */
	
	public Board() {
		this.boardMap = new Hashtable<String, String>();
		initBoardMap();
	}
	
	private void initBoardMap() {
		for (int i = 65 ; i <= 70 ; i++) {
			char vertical = (char) i;
			for (int j = 1 ; j <= 10 ; j++) {
				String pos = vertical + Integer.toString(j);
				this.boardMap.put(pos, "U");
			}
		}
	}
	
	public void setBoardConditionAt(String pos, String cond) {
		this.boardMap.replace(pos, cond);
	}
	
	public String toString() {
		String board = "- 1 2 3 4 5 6 7 8 9 10\n";
		for (int i = 65 ; i <= 70 ; i++) {
			char vertical = (char) i;
			for (int j = 1 ; j <= 10 ; j++) {
				String pos = vertical + Integer.toString(j);
				if (j == 1)
					board += vertical;
				board += " ";
				board += this.boardMap.get(pos);
				
			}
			board += "\n";
		}
		return board;
	}
}
