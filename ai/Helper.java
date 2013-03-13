package ai;

import list.DList;
import list.InvalidNodeException;
import java.util.Arrays;

import list.List;
import player.Move;

public class Helper {
	
	public final static int BLACK = -1, EMPTY = 0, WHITE = 1;

    /**
     * isValid() determines if a given move for a given player on a given board
     * is valid.
     * 
     * @param color the turn of the current player (determined by color)
     * @param move the proposed move
     * @param board the current state of the board
     * @return if the proposed move is valid
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
     * @throws InvalidNodeException 
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
        list = connectedChips(new int[]{0, 0}, board);
        for(Object o: list){
        	System.out.println(Arrays.toString((int[])o));
        }
	}

    /**
     * hasNetwork() checks to see if a board contains a winning network for a
     * given player.
     * 
     * @param color the turn of the current player (determined by color)
     * @param board the current state of the board
     * @return if there is a valid network
     */
    public static boolean hasNetwork(int color, int[][] board) {
        return false;
    }

    /**
     * @param args
     * @throws InvalidNodeException 
     */
    
    
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
    }

}


