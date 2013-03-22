package ai;

import player.Move;

public class AI {
	
	/**
	 * eval() analyzes the current board with respect to the current player 
	 * for a given move returning a value between -1.0 and 1.0 signifying
	 * the strength of the given move.
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @return a double signifying the strength of a move
	 * 
	 * @author Alec Mouri, Austin Chen, Michael Liu
	 */
	public static double eval(int color, Board board){
		//Possible heuristics for determining optimal board state for each player are:
		//	1. Number of connections currently made
		//	2. Number of possible moves
		//	3. Size of largest network
		//	4. Average distance between connections (smaller distances are more difficult to break)
		//	5. Whether possible connections can be broken
		//
		//In order of importance: Size of largest network > Number of connections currently made > Number of possible moves
		double connections = 0;
		double possibleMoves = 0;
		double ourNetworkLength = 0.0;
		double opponentNetworkLength = 0.0;
		double netLength = 0.0;
		
		//Check for winning Network
		for(int i = 10; i >= 6; i--){
			if(board.hasNetwork(color, i)){
				return 1;
			}
			if(board.hasNetwork(-color, i)){
				return -1;
			}
			
		}
		
		//Size of largest Network
		ourNetworkLength = board.longestPathLength(color);
		opponentNetworkLength = board.longestPathLength(-color);
		netLength = ourNetworkLength - opponentNetworkLength;
		
		//Number of current connections
		for(Object pos : board.locationOfPieces(color)){
			connections += board.connectedChips((int[]) pos).length();
		}
		for(Object pos : board.locationOfPieces(-color)){
			connections -= board.connectedChips((int[]) pos).length();
		}
		
		//Number of possible moves
		possibleMoves = board.allValidMoves(color).length() - board.allValidMoves(-color).length();
		
//		System.out.println(board + " " + connections);
		connections /= 2; //b/c doublecounted
		connections /= 40; //scale down connections
		connections *= .3; //weight to .3
		possibleMoves /= 48;
		possibleMoves *= .2; //weight to .2
		netLength /= 10;
		netLength *= .5; //weight to .5
		return connections + possibleMoves + netLength;
	}
	
	public static Move bestMove(int color, Board board, int depth) {
		return (Move) bestMoveHelper(color, color, board, depth, -1.0, 1.0)[0];
	}
	
	/**
	 * bestMove() returns an Object containing the strongest move for the given player 
	 * using our evaluation function along with the strength of the given move.
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @param depth the depth to which eval() checks
	 * @param alpha
	 * @param beta
	 * @return an Object containing the strongest move and its corresponding strength
	 * 
	 * @author Michael Liu
	 */
	private static Object[] bestMoveHelper(int color, int AIcolor, Board board, int depth, double alpha, double beta){
		Object[] optimalMove = new Object[] {new Move(0, 0), 0.0};
		Object[] replyMove;
 		Board copyBoard = new Board();
		for(int i = 0; i < board.grid.length; i++) {
    		for(int j = 0; j < board.grid[0].length; j++) {
    			copyBoard.grid[i][j] = board.grid[i][j];
    		}
		}
		
		if(depth == 0) {
			optimalMove[1] = eval(color, copyBoard);
			return optimalMove;
		}
		
		if (color == AIcolor) {
			optimalMove[1] = alpha;
		} else {
			optimalMove[1] = beta;
		}
		
		for(Object o : copyBoard.allValidMoves(color)) {
			Move m = (Move) o;
			copyBoard.makeMove(color, m);
			replyMove = bestMoveHelper(-color, AIcolor, copyBoard, depth - 1,
					alpha, beta); 
			Double heuristic = (Double) replyMove[1];
			copyBoard.makeMove(Board.EMPTY, m);
			if (m.moveKind == Move.STEP) {
				Move y = new Move(m.x2, m.y2);
				copyBoard.makeMove(color, y);
			}
			if (color == AIcolor && (Double) optimalMove[1] <= heuristic) {
				optimalMove[0] = m;
				optimalMove[1] = heuristic;
				alpha = heuristic;
			} else if (color == -AIcolor && (Double) optimalMove[1] >= heuristic) {
				optimalMove[0] = m;
				optimalMove[1] = heuristic;
				beta = heuristic;
			}
			if (beta <= alpha) {
				break;
			}
		}
		return optimalMove;
	}
	
	private static void testBestMove() {
		Board board = new Board();
		System.out.println(bestMove(Board.WHITE, board, 1));
		board.makeMove(Board.WHITE, bestMove(Board.WHITE, board, 1));
		System.out.println(board);
		System.out.println(bestMove(Board.BLACK, board, 1));
		board.makeMove(Board.BLACK, bestMove(Board.BLACK, board, 1));
		System.out.println(board);
		System.out.println(bestMove(Board.WHITE, board, 1));
		board.makeMove(Board.WHITE, bestMove(Board.WHITE, board, 1));
		System.out.println(board);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = new Board();
		board.grid[4][4] = Board.WHITE;
		board.grid[2][4] = Board.BLACK;
		board.grid[2][2] = Board.WHITE;
		board.grid[4][2] = Board.WHITE;
		board.grid[6][6] = Board.BLACK;
		board.grid[6][4] = Board.WHITE;
		board.grid[4][6] = Board.BLACK;
		board.grid[4][3] = Board.WHITE;
		board.grid[2][3] = Board.WHITE;
		System.out.println(eval(Board.BLACK, board));
		testBestMove();
	}

}
