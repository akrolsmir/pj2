package ai;

import list.DList;
import list.InvalidNodeException;

import java.util.Arrays;

import list.List;
import player.Move;

public class Board {
	
	public final static int BLACK = -1, EMPTY = 0, WHITE = 1;
	enum Direction {
		N, E, S, W, NE, SE, SW, NW, NONE
	}
	
	private List blackChips = new DList(), whiteChips = new DList();
	
	int[][] grid;
	
	public Board(){
		grid = new int[8][8];
	}
	
	public Board(int[][] init){
		grid = init;
	}
	
	/**
	 * makeMove() tries to make the given move on this board. Returns if the
	 * move was made (i.e. valid)
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param move the proposed move
	 * @return
	 */
	public boolean makeMove(int color, Move move){
	    if(!isValid(color, move)){
	    	return false;
	    }
	    List chipList = color == BLACK ? blackChips : whiteChips;
	    switch(move.moveKind){
	    case Move.ADD:
	    	grid[move.x1][move.y1] = color;
	    	chipList.insertBack(new int[]{move.x1, move.y1});
	    	break;
	    case Move.STEP:
	    	grid[move.x1][move.y1] = color;
	    	grid[move.x2][move.y2] = EMPTY; 
	    	chipList.insertBack(new int[]{move.x1, move.y1});
	    	break;
	    }
	    return true;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				switch (grid[i][j]) {
				case BLACK:
					result.append(" B");
					break;
				case WHITE:
					result.append(" W");
					break;
				default:
					result.append(" .");
					break;
				}
			}
			result.append('\n');
		}
		return result.toString();
	}

    /**
     * isValid() determines if a given move for a given player on this board
     * is valid.
     * 
     * @param color the turn of the current player (determined by color)
     * @param move the proposed move
     * @return if the proposed move is valid
     * 
     * @author Alec Mouri
     */
    public boolean isValid(int color, Move move) {
        //No chip may be placed in any of the four corners of the board
    	//So x1, y1 cannot simultaneously be a multiple of 7
    	int x = move.x1;
    	int y = move.y1;
    	int len = grid.length - 1;
    	int adjacent = 0;
    	if(x % len  == 0 && y % len == 0){
    		return false;
    	}
    	
    	//No chip may be placed in a goal of the opposite color.
    	//Note that Black cannot be placed in the left/right goal spaces,
    	//and White cannot be placed in the top/down goal spaces
    	if((color == BLACK && x % len == 0) || 
    	   (color == WHITE && y % len == 0)){
    		return false;
    	}
    	
    	//No chip may be placed in a square that is already occupied.
    	if(grid[x][y] != EMPTY){
    		return false;
    	}
    	
    	//Groups of more than two chips of the same color are not allowed.
    	//Naive implementation since depth of search is at most 2
    	//Note: can probably be optimized.
    	for (int i = x - 1; i <= x + 1; i++){
    		for (int j = y - 1; j <= y + 1; j++){

    			//Do not consider case of nonexistent adjacent square and the square we want to make sure that is valid
    			if(i < 0 || i > len || j < 0 || j > len || (i == x && j == y)){
    				continue;
    			}
    			//If an adjacent chip of the same color is found then check chips adjacent to those
    			if(!(i == x && j == y) && grid[i][j] == color){
    				adjacent += 1;
    				//If two adjacent pieces, then move is not valid
    				if(adjacent == 2){
    					return false;
    				}
    				for(int k = i - 1; k <= i + 1; k++){
    					for(int l = j - 1; l <= j + 1; l++){
    						//Do not consider case of nonexistent adjacent square and coordinates of
    						//proposed move.
    		    			if(k < 0 || k > len || l < 0 || l > len || (k == i && l == j)){
    		    				continue;
    		    			}
    		    			  			
    		    			//If a chip of the same color is found then move is illegal
    		    			if(grid[k][l] == color){
    		    				return false;
    		    			}
    					}
    				}
    			}
    		}
    	}
    	
    	//All rules are satisfied so return true
    	return true;
    }
    
    private static void testValidMove(){
    	
    	System.out.println("Initializing board...");
    	int[][] grid = new int[8][8];
    	grid[5][5] = WHITE;
    	grid[2][2] = BLACK;
    	grid[5][4] = WHITE;
    	grid[3][1] = BLACK;
    	
    	Board board = new Board(grid);
    
    	
    	System.out.println("There is a WHITE piece at (5, 5) and at (0, 4). There is a BLACK piece at (2, 2) and at (3, 1)");
    	System.out.println("Testing for invalid moves...");
    	
    	System.out.println("Invalid moves for WHITE should be (0, 0), (1, 0), (2, 0), (3, 0), (4, 0), (5, 0), (6, 0), (7, 0), " +
    			"(3, 1), (2, 2), (4, 3), (5, 3), (6, 3), (4, 4), (5, 4), (6, 4), (4, 5), (5, 5), (6, 5), (4, 6), (5, 6), (6, 6) " + 
    			"(0, 7), (1, 7), (2, 7), (3, 7), (4, 7), (5, 7), (6, 7), (7, 7)");
    	
    	System.out.println("Invalid moves for BLACK should be (0, 0), (0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (0, 6), (0, 7), " +
    			"(2, 0), (3, 0), (4, 0), (1, 1), (2, 1), (3, 1), (4, 1), (1, 2), (2, 2), (3, 2), (4, 2), (1, 3), (2, 3), (3, 3), (5, 4), (5, 5), " +
    			"(7, 0), (7, 1), (7, 2), (7, 3), (7, 4), (7, 5) (7, 6), (7, 7)");
    	
    	//Test every board position for each color:
    	for(int c = -1; c < 2; c = c + 2){
    		for(int i = 0; i < 8; i++){
    			for(int j = 0; j < 8; j++){
    				if(!board.isValid(c, new Move(i, j))){
    					System.out.print("(" + i + ", " + j + ") is an invalid move for ");
    					if (c == BLACK){
    						System.out.println("BLACK");
    					} else {
    						System.out.println("WHITE");
    					}
    				}
    			}
    		}
    	}
    	
    	Board boardTwo = new Board();
    	boardTwo.makeMove(WHITE, new Move (3, 1));
    	boardTwo.makeMove(WHITE, new Move(2, 3));
    	System.out.println(boardTwo.isValid(WHITE, new Move(3, 2)));
    	
    }

    public List locationOfPieces(int color) {
    	DList listed = new DList();
    	for (int x = 0; x < grid.length; x++) {
    		for (int y = 0; y < grid[0].length; y++) {
    			if (grid[x][y] == color) {
    				listed.insertFront(new int[] {x, y});
    			}
    		}
    	}
    	return listed;
    	
    	//return color == BLACK ? blackChips : whiteChips;
    }
    
    /**
     * allValidMoves() creates a list of all valid Moves for a given board and
     * player.
     * 
     * @param color the turn of the current player (determined by color)
     * @return
     * 
     * @author Michael Liu
     */
    public List allValidMoves(int color) {
    	DList validList = new DList();
    	//differentiates adding and moving pieces
    	if (locationOfPieces(color).length() <= 9) {
    		//iterating through both dimensions of the board
    		for(int i = 0; i < grid.length; i++) {
        		for(int j = 0; j < grid[0].length; j++) {
        			Move m = new Move(i, j);
        			if (isValid(color, m)) {
        				validList.insertBack(m);
        			}
        		}
        	}
    	} else {
    		for(Object o : locationOfPieces(color)) {
    			for(int i = 0; i < grid.length; i++) {
            		for(int j = 0; j < grid[0].length; j++) {
            			Move m = new Move(i, j, ((int[])o)[0], ((int[])o)[1]);
            			if (isValid(color, m)) {
            				validList.insertBack(m);
            			}
            		}
    			}
    		}
    	}
    	return validList;
    }
    

    /**
     * connectedChips() creates a list of all chips which are connected to a
     * given chip on the board. Chips are represented by an int array of length
     * 2, in the form of {x, y}. If there is no chip at that location, return
     * an empty List.
     * 
     * @param chip the position of the chip to check for connections
     * @return a List of int arrays of chip positions
     * 
     * @author Austin Chen
     */
	public List connectedChips(int[] chip) {
		int x0 = chip[0], y0 = chip[1];
		if (!inBounds(x0, y0) || grid[x0][y0] == EMPTY) {
			return new DList();
		}

		List connectedChips = new DList();
		// i and j form offsets for each of the 8 directions
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!(i == 0 && j == 0)){
					// Keep incrementing x and y by i and j, until out of
					// bounds or chip found
					int x = x0 + i, y = y0 + j;
					while (inBounds(x, y) && grid[x][y] == EMPTY) {
						x += i;
						y += j;
					}
					// Add to the list if it's the same color as the original
					if (inBounds(x, y) && grid[x][y] == grid[x0][y0]) {
						connectedChips.insertBack(new int[] { x, y });
					}
				}
			}
		}
		return connectedChips;
	}

	private boolean inBounds(int x, int y) {
		return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
	}
	
	private static void testConnectedChips(){
        Board board = new Board(new int[][]{{-1, 0, -1},{1, 1, 0},{1, 0, -1}});
        List list = board.connectedChips(new int[]{1, 1});
        for(Object o: list){
        	System.out.println(Arrays.toString((int[])o));
        }
        System.out.println("^ should have been: (1, 0), (2, 0)");
        list = board.connectedChips(new int[]{0, 2});
        for(Object o: list){
        	System.out.println(Arrays.toString((int[])o));
        }
        System.out.println("^ should have been: (0, 0), (2, 2)");
	}

    /**
     * hasNetwork() checks to see if a board contains a winning network for a
     * given player.
     * 
     * @param color the turn of the current player (determined by color)
     * @param board the current state of the board
     * @return if there is a valid network
     * 
     * @author Alec Mouri
     */
	public boolean hasNetwork(int color) {
    	List pieces = locationOfPieces(color);
    	for(Object curr : pieces){
    		int[] c = (int[]) curr;
    		//disregard initial path that is not in goal area
    		if((color == WHITE && c[0] % grid.length == 0) || (color == BLACK && c[1] % grid.length == 0)){
    			//memoized list
    			List network = new DList();
    			network.insertBack(c);
	    		if (hasNetworkHelper(network, c, color, Direction.NONE)){
	    			return true;
	    		}
    		}
    	}
    	return false;
    }
    
    /**
     * hasNetworkHelper() is a helper method for hasNetwork() that checks for all rules of a valid network and performs a tree
     * search to check all possible candidate networks until a valid one is found.
     * 
     * @param memo the memoized list of previous chips in the network
     * @param pos the position of the latest chip in the network
     * @param color the color of the network we are detecting the network for
     * @param board the current state of the board
     * @param dir the previous direction that was searched for
     * @return
     */
    private boolean hasNetworkHelper(List memo, int[] pos, int color, Direction dir){
    	int len = grid.length - 1;
    	
    	List chips = connectedChips(pos);
    	//If network length is at least 6, return
    	if(memo.length() >= 6 && ((color == WHITE && pos[0] % len == 0) || (color == BLACK && pos[1] % len == 0))){
    		return true;
    	}
    	

    	//iterate over all chips connected to current chip
    	for(Object curr: chips){
    		int[] c = (int[]) curr;
    		Direction newDir = getDirection(pos, c);
    		if (dir != newDir && !inNetwork(memo, c)){
    			memo.insertBack(c);
    			if(hasNetworkHelper(memo, c, color, newDir)){
    				return true;
    			} else {
    				try {
    				memo.back().remove();
    				} catch (InvalidNodeException e){
    					
    				}
    			}
    		}
    	}
    	
    	//No possible connections found, return false;
    	return false;
    }
    
    private static Direction getDirection(int[] pos, int[] newPos){
    	if(pos[0] > newPos[0] && pos[1] < newPos[1]){
    		return Direction.SW;
    	} else if (pos[0] < newPos[0] && pos[1] < newPos[1]){
    		return Direction.SE;
    	} else if (pos[0] < newPos[0] && pos[1] > newPos[1]){
    		return Direction.NE;
    	} else if (pos[0] > newPos[0] && pos[1] > newPos[1]){
    		return Direction.NW;
    	} else if (pos[0] > newPos[0]){
    		return Direction.W;
    	} else if (pos[0] < newPos[0]){
    		return Direction.E;
    	} else if (pos[1] < newPos[1]){
    		return Direction.S;
    	} else {
    		return Direction.N;
    	}
    }
    
    private static boolean inNetwork(List memo, int[] pos){
    	for(Object o: memo){
    		if(pos[0] == ((int[])o)[0] && pos[1] == ((int[])o)[1]){
    			return true;
    		}
    	}
    	return false;
    }
    
    private static void testHasNetwork() {
    	Board board = new Board();
    	System.out.println("Initializing test board...");
    	board.grid[2][0] = BLACK;
    	board.grid[2][3] = BLACK;
    	board.grid[3][2] = BLACK;
    	board.grid[6][5] = BLACK;
    	board.grid[3][5] = BLACK;
    	board.grid[3][7] = BLACK;
    	System.out.println("A network should be detected.");
    	System.out.println("Network detected: " + board.hasNetwork(BLACK));
    }

    private static void testAllValidMoves() {
//    	Board board = new Board();
//    	System.out.println("With an empty board, these are the valid moves for WHITE:" + board.allValidMoves(WHITE));
//    	board.grid[4][4] = WHITE;
//    	System.out.println("WHITE goes on (4,4), BLACK can go on:" + board.allValidMoves(BLACK));
//    	board.grid[4][3] = BLACK;
//    	System.out.println("BLACK goes on (4,3), WHITE can go on:" + board.allValidMoves(WHITE));
//    	board.grid[4][5] = WHITE;
//    	System.out.println("WHITE goes on (4,5), BLACK can go on:" + board.allValidMoves(BLACK));
//    	for(int x = 0; x < board.grid.length; x++) {
//    		board.grid[x][2] = WHITE;
//    	}
//    	System.out.println("WHITE has 10 pieces, WHITE can now:" + board.allValidMoves(WHITE));
//    	for(int x = 0; x < board.grid.length; x++) {
//    		for(int y = 0; y < board.grid[0].length; y++) {
//    			board.grid[y][x] = WHITE;
//    		}
//    	}
//    	System.out.println("WHITE has all the spaces, BLACK can go on:" + board.allValidMoves(BLACK));
    }
    
    public static void main(String[] args) {
//    	testValidMove();
    	testAllValidMoves();
//    	testConnectedChips();
//    	testHasNetwork();
    }

}


