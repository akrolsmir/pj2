package ai;

import list.List;
import player.Move;

public class Helper {
	
	private final static int BLACK = -1;
	private final static int EMPTY = 0;
	private final static int WHITE = 1;

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
    	if(x % 7 == 0 && y % 7 == 0){
    		return false;
    	}
    	
    	//No chip may be placed in a goal of the opposite color.
    	//Note that Black cannot be placed in the left/right goal spaces,
    	//and White cannot be placed in the top/down goal spaces
    	if((color == BLACK && x % 7 == 0) || 
    	   (color == WHITE && y % 7 == 0)){
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
    			if(i < 0 || i > 7 || j < 0 || j > 7){
    				continue;
    			}
    			//If an adjacent chip of the same color is found then check chips adjacent to those
    			if((i != x && j != y) && board[i][j] == color){
    				for(int k = i - 1; k <= i + 1; k++){
    					for(int l = j - 1; l <= j + 1; l++){
    						//Do not consider case of nonexistent adjacent square and coordinates of
    						//proposed move.
    		    			if(k < 0 || k > 7 || l < 0 || l > 7 || (k == x && l == y)){
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
    	
    	System.out.println("There is a white piece at (5, 5) and at (0, 4). There is a black piece at (2, 2) and at (3, 1)");
    	System.out.println("Testing for invalid moves...");
    	
    	System.out.println("Invalid moves for white should be (0, 0), (1, 0), (2, 0), (3, 0), (4, 0), (5, 0), (6, 0), (7, 0), " +
    			"(3, 1), (2, 2), (4, 3), (5, 3), (6, 3), (4, 4), (5, 4), (6, 4), (4, 5), (5, 5), (6, 5), (4, 6), (5, 6), (6, 6) " + 
    			"(0, 7), (1, 7), (2, 7), (3, 7), (4, 7), (5, 7), (6, 7), (7, 7)");
    	
    	System.out.println("Invalid moves for black should be (0, 0), (0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (0, 6), (0, 7), " +
    			"(2, 0), (3, 0), (4, 0), (1, 1), (2, 1), (3, 1), (4, 1), (1, 2), (2, 2), (3, 2), (4, 2), (1, 3), (2, 3), (3, 3), (5, 4), (5, 5), " +
    			"(7, 0), (7, 1), (7, 2), (7, 3), (7, 4), (7, 5) (7, 6), (7, 7)");
    	
    	//Test every board position for each color:
    	for(int c = -1; c < 2; c = c + 2){
    		for(int i = 0; i < 8; i++){
    			for(int j = 0; j < 8; j++){
    				System.out.println("Validating (" + i + ", " + j + ")");
    				if(!isValid(c, new Move(i, j), board)){
    					System.out.print("(" + i + ", " + j + ") is an invalid move for ");
    					if (c == BLACK){
    						System.out.println("Black");
    					} else {
    						System.out.println("White");
    					}
    				}
    			}
    		}
    	}
    }

    /**
     * allValidMoves() creates a list of all valid Moves for a given board and
     * player.
     * 
     * @param color the turn of the current player (determined by color)
     * @param board the current state of the board
     * @return
     */
    public List allValidMoves(int color, int[][] board) {
    	return null;
    }

    /**
     * connectedChips() creates a list of all chips which are connected to a
     * given chip on the board. Chips are represented by an int array of length
     * 2, in the form of {x, y}.
     * 
     * @param chip the position of the chip to check for connections
     * @param board the current state of the board
     * @return a List of int arrays of chip positions 
     */
    public List connectedChips(int[] chip, int[][] board) {
    	return null;
    }

    /**
     * hasNetwork() checks to see if a board contains a winning network for a
     * given player.
     * 
     * @param color the turn of the current player (determined by color)
     * @param board the current state of the board
     * @return if there is a valid network
     */
    public boolean hasNetwork(int color, int[][] board) {
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	testValidMove();
    }

}
