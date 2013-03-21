package ai;

import list.DList;
import list.InvalidNodeException;
import java.util.Arrays;

import list.List;
import player.Move;

public class Helper {
	
	public final static int BLACK = -1, EMPTY = 0, WHITE = 1;
	private enum Direction {
		N, E, S, W, NE, SE, SW, NW, NONE
	}

    /**
     * isValid() determines if a given move for a given player on a given board
     * is valid.
     * 
     * @param color the turn of the current player (determined by color)
     * @param move the proposed move
     * @param board the current state of the board
     * @return if the proposed move is valid
     * 
     * @author Alec Mouri
     */
    public static boolean isValid(int color, Move move, int[][] board) {
        //No chip may be placed in any of the four corners of the board
    	//So x1, y1 cannot simultaneously be a multiple of 7
    	int x = move.x1;
    	int y = move.y1;
    	int len = board.length - 1;
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
    	if(board[x][y] != EMPTY){
    		return false;
    	}
    	
    	//Groups of more than two chips of the same color are not allowed.
    	//Naive implementation since depth of search is at most 2
    	//Note: can probably be optimized.
    	for (int i = x - 1; i <= x + 1; i++){
    		for (int j = y - 1; j <= y + 1; j++){
    			//Do not consider case of nonexistent adjacent square
    			if(i < 0 || i > len || j < 0 || j > len){
    				continue;
    			}
    			//If an adjacent chip of the same color is found then check chips adjacent to those
    			if(!(i == x && j == y) && board[i][j] == color){
    				for(int k = i - 1; k <= i + 1; k++){
    					for(int l = j - 1; l <= j + 1; l++){
    						//Do not consider case of nonexistent adjacent square and coordinates of
    						//proposed move.
    		    			if(k < 0 || k > len || l < 0 || l > len || (k == i && l == j)){
    		    				continue;
    		    			}
    		    			  			
    		    			//If a chip of the same color is found then move is illegal
    		    			if(board[k][l] == color){
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
    	int[][] board = new int[8][8];
    	board[5][5] = WHITE;
    	board[2][2] = BLACK;
    	board[5][4] = WHITE;
    	board[3][1] = BLACK;
    	
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
    				if(!isValid(c, new Move(i, j), board)){
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
    }

    private static int numberOfPieces(int color, int[][] board) {
    	int number = 0;
    	for (int x = 0; x < board.length; x++) {
    		for (int y = 0; y < board[0].length; y++) {
    			if (board[x][y] == color) {
    				number++;
    			}
    		}
    	}
    	return number;
    }
    
    private static List locationOfPieces(int color, int[][] board) {
    	DList listed = new DList();
    	for (int x = 0; x < board.length; x++) {
    		for (int y = 0; y < board[0].length; y++) {
    			if (board[x][y] == color) {
    				listed.insertFront(new int[] {y, x});
    			}
    		}
    	}
    	return listed;
    }
    
    /**
     * allValidMoves() creates a list of all valid Moves for a given board and
     * player.
     * 
     * @param color the turn of the current player (determined by color)
     * @param board the current state of the board
     * @return
     * 
     * @author Michael Liu
     */
    public static List allValidMoves(int color, int[][] board) {
    	DList validList = new DList();
    	//differentiates adding and moving pieces
    	if (Helper.numberOfPieces(color, board) <= 9) {
    		//iterating through both dimensions of the board
    		for(int i = 0; i < board.length; i++) {
        		for(int j = 0; j < board[0].length; j++) {
        			if (isValid(color, new Move(j, i), board)) {
        				validList.insertBack(new Move(j, i));
        			}
        		}
        	}
    	} else {
    		for(Object o : locationOfPieces(color,board)) {
    			for(int i = 0; i < board.length; i++) {
            		for(int j = 0; j < board[0].length; j++) {
            			Move m = new Move(((int[])o)[0], ((int[])o)[1], j, i);
            			if (isValid(color, m, board)) {
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
     * @param board the current state of the board
     * @return a List of int arrays of chip positions
     * 
     * @author Austin Chen
     */
	public static List connectedChips(int[] chip, int[][] board) {
		int x0 = chip[0], y0 = chip[1];
		if (!inBounds(board, x0, y0) || board[x0][y0] == EMPTY) {
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
					while (inBounds(board, x, y) && board[x][y] == EMPTY) {
						x += i;
						y += j;
					}
					// Add to the list if it's the same color as the original
					if (inBounds(board, x, y) && board[x][y] == board[x0][y0]) {
						connectedChips.insertBack(new int[] { x, y });
					}
				}
			}
		}
		return connectedChips;
	}

	private static boolean inBounds(int[][] board, int x, int y) {
		return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
	}
	
	private static void testConnectedChips(){
        int[][] board = new int[][]{{-1, 0, -1},{1, 1, 0},{1, 0, -1}};
        List list = connectedChips(new int[]{1, 1}, board);
        for(Object o: list){
        	System.out.println(Arrays.toString((int[])o));
        }
        System.out.println("^ should have been: (1, 0), (2, 0)");
        list = connectedChips(new int[]{0, 2}, board);
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
    public static boolean hasNetwork(int color, int[][] board) {
    	List pieces = locationOfPieces(color, board);
    	for(Object curr : pieces){
    		//disregard initial path that is not in goal area
    		if((color == WHITE && ((int[]) curr)[1] % (board.length) == 0) || (color == BLACK && ((int[]) curr)[0] % (board.length) == 0)){
    			List first = new DList();
    			first.insertBack(new int[] {((int[])curr)[1], ((int[])curr)[0]});
	    		if (hasNetworkHelper(first, new int[] {((int[])curr)[1], ((int[])curr)[0]}, color, board, Direction.NONE)){
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
    private static boolean hasNetworkHelper(List memo, int[] pos, int color, int[][] board, Direction dir){
    	int len = board.length - 1;
    	
    	List chips = connectedChips(pos, board);
    	//If network length is at least 6, return
    	if(memo.length() >= 6 && ((color == WHITE && pos[0] % len == 0) || (color == BLACK && pos[1] % len == 0))){
    		return true;
    	}
    	
    	for(Object o: chips){
    		Direction newDir = getDirection(pos, (int []) o);
    		if (dir != newDir && !inNetwork(memo, (int []) o)){
    			memo.insertBack(o);
    			if(hasNetworkHelper(memo, (int[]) o, color, board, newDir)){
    				return true;
    			} else {
    				try {
    				memo.back().remove();
    				} catch (InvalidNodeException e){
    					
    				}
    			}
    		}
    	}
    	
    	//Begin search across all 8 directions:
    	
    	//Note that the same direction cannot be searched in a row.
    	//E.g. if a chip was arrived to in the Eastward direction, the next
    	//chips cannot be found in the Eastward direction
    	
    	//Chips cannot be jumped over. So path must branch at first chip
    	//it encounters in a given direction, and opposing chips will block the path
    	
    	//All chips in a network cannot appear twice. So the network should be treated
    	//as a simple graph.
   
    	//Search North
    	/*
    	if (dir != Direction.N){
    		//Searched Y strictly decreases
	    	for(int i = posY - 1; i >= 0; i--){
	    		tempPos = board[posX][i];
	    		if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if (hasNetworkHelper(memo, new int[] {posX, i}, color, board, Direction.N)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//search South
    	if (dir != Direction.S){
	    	//Searched Y strictly increase
	    	for(int i = posY + 1; i <= len; i++){
	    		tempPos = board[posX][i];
	    		if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if(hasNetworkHelper(memo, new int[] {posX, i}, color, board, Direction.S)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//Search West
    	if (dir != Direction.W){
    		//Searched X strictly decreases
	    	for(int i = posX - 1; i >= 0; i--){
	    		tempPos = board[i][posY];
	    		if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if(hasNetworkHelper(memo, new int[] {i, posY}, color, board, Direction.W)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//Search East
    	if (dir != Direction.E){
	    	//Searched X strictly increases
	    	for(int i = posX + 1; i <= len; i++){
	    		tempPos = board[i][posY];
	    		if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if (hasNetworkHelper(memo, new int[] {i, posY}, color, board, Direction.E)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//search Northeast
    	if (dir != Direction.NE){
	    	//Searched X strictly increases, searched Y strictly decreases	
	    	for(int i = 1; i <= Math.min(len - posX, posY); i++){
	    		tempPos = board[posX + i][posY - i];
	    		if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if (hasNetworkHelper(memo, new int[] {posX + i, posY - i}, color, board, Direction.NE)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//search Southeast
    	if (dir != Direction.SE){
    		//Searched X strictly increases, searched Y strictly increases
    		for(int i = 1; i <= Math.min(len - posX, len - posY); i++){
    			tempPos = board[posX + i][posY + i];
    			if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if (hasNetworkHelper(memo, new int[] {posX + i, posY + i}, color, board, Direction.SE)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//Search Southwest
    	if (dir != Direction.SW){
    		//Searched X strictly decreases, searched Y strictly increases
    		for(int i = 1; i <= Math.min(posX, len - posY); i++){
    			tempPos = board[posX - i][posY + i];
    			if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if(hasNetworkHelper(memo, new int[] {posX - i, posY + i}, color, board, Direction.SW)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	//Search Northwest
    	if (dir != Direction.NW){
    		//Searched X strictly decreases, searched Y strictly decreases
    		for(int i = 1; i <= Math.min(posX, posY); i++){
    			tempPos = board[posX - i][posY - i];
    			if(tempPos != EMPTY){
	    			inNet = inNetwork(memo, pos);
	    		}
	    		if(tempPos != color && tempPos != EMPTY || tempPos == color && inNet){
	    			break;
	    		}
	    		if(tempPos == color && !inNet){
	    			memo.insertBack(pos);
	    			if (hasNetworkHelper(memo, new int[] {posX - i, posY - i}, color, board, Direction.NW)){
	    				return true;
	    			}
	    		}
	    	}
    	}
    	*/
    	
    	//No possible connections found, return false;
    	return false;
    }
    
    private static Direction getDirection(int[] pos, int[] newPos){
    	if(pos[0] < newPos[0] && pos[1] < newPos[1]){
    		return Direction.SW;
    	} else if (pos[0] > newPos[0] && pos[1] > newPos[1]){
    		return Direction.SE;
    	} else if (pos[0] > newPos[0] && pos[1] < newPos[1]){
    		return Direction.NE;
    	} else if (pos[0] < newPos[0] && pos[1] > newPos[1]){
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
    	int[][] board = new int[8][8];
    	System.out.println("Initializing test board...");
    	board[2][0] = BLACK;
    	board[2][3] = BLACK;
    	board[3][2] = BLACK;
    	board[6][5] = BLACK;
    	board[3][5] = BLACK;
    	board[3][7] = BLACK;
    	System.out.println("A network should be detected.");
    	System.out.println("Network detected: " + hasNetwork(BLACK, board));
    }

    private static void testAllValidMoves() {
    	int[][] board = new int[8][8];
    	System.out.println("With an empty board, these are the valid moves for WHITE:" + allValidMoves(WHITE, board));
    	board[4][4] = WHITE;
    	System.out.println("WHITE goes on (4,4), BLACK can go on:" + allValidMoves(BLACK, board));
    	board[4][3] = BLACK;
    	System.out.println("BLACK goes on (4,3), WHITE can go on:" + allValidMoves(WHITE, board));
    	board[4][5] = WHITE;
    	System.out.println("WHITE goes on (4,5), BLACK can go on:" + allValidMoves(BLACK, board));
    	for(int x = 0; x < board.length; x++) {
    		board[x][2] = WHITE;
    	}
    	System.out.println("WHITE has 10 pieces, WHITE can now:" + allValidMoves(WHITE, board));
    	for(int x = 0; x < board.length; x++) {
    		for(int y = 0; y < board[0].length; y++) {
    			board[y][x] = WHITE;
    		}
    	}
    	System.out.println("WHITE has all the spaces, BLACK can go on:" + allValidMoves(BLACK, board));
    }
    
    public static void main(String[] args) {
    	testValidMove();
    	testAllValidMoves();
    	testConnectedChips();
    	testHasNetwork();
    }

}


