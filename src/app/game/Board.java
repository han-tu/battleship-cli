package app.game;

import java.util.Hashtable;

public class Board {
	private Hashtable <String, String> boardMap;
	
	public Board() {
		this.boardMap = new Hashtable<String, String>();
		initBoardMap();
	}
	
	private void initBoardMap() {
		for (int i = 65 ; i <= 70 ; i++) {
			char vertical = (char) i;
			for (int j = 0 ; j < 10 ; j++) {
				String pos = vertical + Integer.toString(j);
				this.boardMap.put(pos, "U");
			}
		}
	}
	
	public void setBoardConditionAt(String pos, String cond) {
		this.boardMap.replace(pos, cond);
	}
	
	public String toString() {
		String board = "- 0 1 2 3 4 5 6 7 8 9\n";
		for (int i = 65 ; i <= 70 ; i++) {
			char vertical = (char) i;
			for (int j = 0 ; j < 10 ; j++) {
				String pos = vertical + Integer.toString(j);
				if (j == 0)
					board += vertical;
				board += " ";
				board += this.boardMap.get(pos);
				
			}
			board += "\n";
		}
		return board;
	}
}
