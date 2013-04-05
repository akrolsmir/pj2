package tests;

import java.util.Arrays;

import list.List;
import player.Move;
import ai.Board;

public class BoardTest {

    
    private static void testHasNetwork() {
    	Board board = new Board();
    	System.out.println("Initializing test board...");
    	board.grid[2][0] = Board.BLACK;
    	board.grid[2][3] = Board.BLACK;
    	board.grid[3][2] = Board.BLACK;
    	board.grid[6][5] = Board.BLACK;
    	board.grid[3][5] = Board.BLACK;
    	board.grid[3][7] = Board.BLACK;
    	System.out.println("A network should be detected.");
    	System.out.println("Network detected: " + board.hasNetwork(Board.BLACK));
    }
    
    private static void testHasNetwork2(){
        Board board = new Board();
        board.grid[6][3] = Board.BLACK;
        board.grid[5][5] = Board.BLACK;
        board.grid[3][3] = Board.BLACK;
        board.grid[3][5] = Board.BLACK;
        board.grid[5][7] = Board.BLACK;
        board.grid[3][0] = Board.BLACK;
        System.out.println(board);
        System.out.println("Network detected: " + board.hasNetwork(Board.BLACK));
        
    }
    
    private static void testValidMove(){
    	
    	System.out.println("Initializing board...");
    	int[][] grid = new int[8][8];
    	grid[5][5] = Board.WHITE;
    	grid[2][2] = Board.BLACK;
    	grid[5][4] = Board.WHITE;
    	grid[3][1] = Board.BLACK;
    	
    	Board board = new Board(grid);
    
    	
    	System.out.println("There is a WHITE piece at (5, 5) and at (0, 4). There is a Board.BLACK piece at (2, 2) and at (3, 1)");
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
    					if (c == Board.BLACK){
    						System.out.println("BLACK");
    					} else {
    						System.out.println("WHITE");
    					}
    				}
    			}
    		}
    	}
    	
    	Board boardTwo = new Board();
    	boardTwo.makeMove(Board.WHITE, new Move (1, 3));
    	boardTwo.makeMove(Board.WHITE, new Move(0, 2));
    	System.out.println(boardTwo.isValid(Board.WHITE, new Move(0, 1)));
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

    private static void testAllValidMoves() {
    	Board board = new Board();
    	System.out.println("With an empty board, these are the valid moves for Board.WHITE:" + board.allValidMoves(Board.WHITE));
    	board.grid[4][4] = Board.WHITE;
    	System.out.println("WHITE goes on (4,4), BLACK can go on:" + board.allValidMoves(Board.BLACK));
    	board.grid[4][3] = Board.BLACK;
    	System.out.println("BLACK goes on (4,3), WHITE can go on:" + board.allValidMoves(Board.WHITE));
    	board.grid[4][5] = Board.WHITE;
    	System.out.println("WHITE goes on (4,5), BLACK can go on:" + board.allValidMoves(Board.BLACK));
    	for(int x = 0; x < board.grid.length; x++) {
    		board.grid[x][2] = Board.WHITE;
    	}
    	System.out.println("WHITE has 10 pieces, WHITE can now:" + board.allValidMoves(Board.WHITE));
    	for(int x = 0; x < board.grid.length; x++) {
    		for(int y = 0; y < board.grid[0].length; y++) {
    			board.grid[y][x] = Board.WHITE;
    		}
    	}
    	System.out.println("WHITE has all the spaces, BLACK can go on:" + board.allValidMoves(Board.BLACK));
    }
    
    public static void main(String[] args) {
    	testHasNetwork();
    	testValidMove();
    	testAllValidMoves();
    	testConnectedChips();
    	testHasNetwork2();
    }

}
