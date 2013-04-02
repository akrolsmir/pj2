package ai;

import player.Move;

public class AI {
	
	static final int nom = 2;
	
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
		//  6. Central Tendency
		//In order of importance: Number of connections currently made > Number of possible moves > Size of largest Network
		double connections = 0;
		double possibleMoves = 0;
		double ourNetworkLength = 0.0;
		double opponentNetworkLength = 0.0;
		double netLength = 0.0;
		double averageDist = 0.0;
		double central = 0.0;
		
		double connectionWeight = .40;
		double possibleMovesWeight = .35;
		double longestNetworkWeight = .15;
		double centralWeight = .10;
//		double averageDistWeight = .05;
		
		
		int[] curr;
		
		//Check for winning Network
		if(board.hasNetwork(color)){
			return Double.MAX_VALUE;
		}
		if(board.hasNetwork(-color)){
			return -Double.MAX_VALUE;
		}

		
		//Size of largest Network
		ourNetworkLength = board.longestPathLength(color);
		opponentNetworkLength = board.longestPathLength(-color);
		netLength = ourNetworkLength - opponentNetworkLength;
		
		//Number of current connections
		for(Object pos : board.locationOfPieces(color)){
			curr = (int[]) pos;
			connections += board.connectedChips(curr).length();
			central = central + (Math.abs(curr[0] - 3.5) + Math.abs(curr[1] - 3.5)) / 2;
//			averageDist += board.averageChipDistance(curr);
		}
		for(Object pos : board.locationOfPieces(-color)){
			curr = (int[]) pos;
			connections -= board.connectedChips(curr).length();
			central = central - (Math.abs(curr[0] - 3.5) + Math.abs(curr[1] - 3.5)) / 2;
//			averageDist -= board.averageChipDistance(curr);
		}
		
		//Number of possible moves
		possibleMoves = board.allValidMoves(color).length() - board.allValidMoves(-color).length();
		
		
//		System.out.println(board + " " + connections);
		connections /= 2; //b/c doublecounted
		connections /= 40; //scale down connections
		connections *= connectionWeight;
		possibleMoves /= 40;
		possibleMoves *= possibleMovesWeight;
		netLength /= 6;
		netLength *= longestNetworkWeight;
		central = central / (board.locationOfPieces(color).length() + board.locationOfPieces(-color).length());
		central *= centralWeight;

//		averageDist /= 4;
//		if(averageDist < 1){
//			averageDist = -1;
//		}
//		averageDist *= averageDistWeight;
	
		return connections + possibleMoves + netLength + central;
	}
	
	/**
	 * bestMove() returns the strongest possible Move using our evaluation
	 * function
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @param depth the depth to which eval() checks
	 * @return an Object containing the strongest move and its corresponding strength
	 * 
	 * @author Michael Liu
	 */
	public static Move bestMove(int color, Board board, int depth) {
		return (Move) bestMoveHelper(color, color, board, depth, -Double.MAX_VALUE, Double.MAX_VALUE)[0];
	}
	

	/**
	 * bestMoveHelper() returns an Object[] containing the strongest move for
	 * the given player using our evaluation function along with the strength
	 * of the given move. It employs alpha-beta pruning.
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @param depth the depth to which eval() checks
	 * @param alpha
	 * @param beta
	 * @return an Object[] containing the strongest move and its corresponding strength
	 * 
	 * @author Michael Liu
	 */
	private static Object[] bestMoveHelper(int color, int AIcolor, Board board, int depth, double alpha, double beta){
		if (board.locationOfPieces(AIcolor).length() == 0) {
			if (board.isValid(AIcolor, new Move(3, 3))) {
				return new Object[] {new Move(3, 3), 0.0}; 
			} else {
				return new Object[] {new Move(3, 4), 0.0};
			}
		}
		
		Object[] optimalMove = new Object[] {new Move(0, 0), 0.0};
		Object[] replyMove;
		
		if(depth == 0) {
<<<<<<< HEAD
			optimalMove[1] = eval(-color, board);
=======
			optimalMove[1] = eval(AIcolor, board);
			//System.out.println(optimalMove[1]);
>>>>>>> Updated AI to correctly work on depth 2
			return optimalMove;
		}
		
		if (color == AIcolor) {
			optimalMove[1] = alpha;
		} else {
			optimalMove[1] = beta;
		}
		for(Object o : board.allValidMoves(color)) {
			Move m = (Move) o;
			board.makeMove(color, m);
			replyMove = bestMoveHelper(-color, AIcolor, board, depth - 1,
					alpha, beta); 
			Double heuristic = (Double) replyMove[1];
			
//			for(int i = depth; i < nom; i++){
//				System.out.print("--");
//			}
//			System.out.println(m.x1 + " " + m.y1 + " " + alpha + " " + beta + " " + 
//					((Move)optimalMove[0]).x1 +  " " + ((Move) optimalMove[0]).y1 + " " +
//					(Double) optimalMove[1] + " " + heuristic);
			
//			if(color == AIcolor){
//				System.out.println(((Move)optimalMove[0]).x1 + " " + ((Move) optimalMove[0]).y1 
//					+ " " + (Double) optimalMove[1] + " " + heuristic);
//			}
			
			board.grid[m.x1][m.y1] = 0;
			if (m.moveKind == Move.STEP) {
				Move y = new Move(m.x2, m.y2);
				board.makeMove(color, y);
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
	
	private static void testBestMove2(){
		Board board = new Board();
		board.grid[6][0] = Board.BLACK;
		board.grid[6][5] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[5][7] = Board.BLACK;
		board.grid[5][5] = Board.BLACK;
		System.out.println(board);
//		System.out.println(board.allValidMoves(Board.BLACK));
		System.out.println(AI.eval(Board.BLACK, board));
		System.out.println(bestMove(Board.BLACK, board, 1) + ", should be 55"); //55 wins
		System.out.println(bestMove(Board.BLACK, board, 1) + ", should be 55"); //55 blocks
		
	}
	
	private static void testHasNetwork2(){
		Board board = new Board();
		board.grid[6][0] = Board.BLACK;
		board.grid[6][5] = Board.BLACK;
		board.grid[5][5] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[5][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == true));
//		System.out.println(board);
		board = new Board();

		board.grid[2][0] = Board.BLACK;
		board.grid[2][5] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[1][3] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[5][5] = Board.BLACK;
		board.grid[5][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == true));
//		System.out.println(board);
		board = new Board();

//		not win

		board.grid[6][0] = Board.BLACK;
		board.grid[2][0] = Board.BLACK;
		board.grid[4][2] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[5][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == false));
		System.out.println(board);
		
		board = new Board();
		board.grid[2][0] = Board.BLACK;
		board.grid[4][2] = Board.BLACK;
		board.grid[6][0] = Board.BLACK;
		board.grid[6][5] = Board.BLACK;
		board.grid[5][5] = Board.BLACK;
		board.grid[5][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == false));
		System.out.println(board);
		
		board = new Board();
		board.grid[2][0] = Board.BLACK;
		board.grid[2][5] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[5][5] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[5][6] = Board.WHITE;
		board.grid[5][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == false));
//		System.out.println(board);
		
		board = new Board();
		board.grid[6][0] = Board.BLACK;
		board.grid[4][2] = Board.BLACK;
		board.grid[3][3] = Board.BLACK;
		board.grid[3][5] = Board.BLACK;
		board.grid[2][5] = Board.BLACK;
		board.grid[2][7] = Board.BLACK;
		System.out.println("passed test? " + (board.hasNetwork(Board.BLACK) == false));
//		System.out.println(board);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Board board = new Board();
//		board.grid[4][4] = Board.WHITE;
//		board.grid[2][4] = Board.BLACK;
//		board.grid[2][2] = Board.WHITE;
//		board.grid[4][2] = Board.WHITE;
//		board.grid[6][6] = Board.BLACK;
//		board.grid[6][4] = Board.WHITE;
//		board.grid[4][6] = Board.BLACK;
//		board.grid[4][3] = Board.WHITE;
//		board.grid[2][3] = Board.WHITE;
//		System.out.println(eval(Board.BLACK, board));
//		testBestMove();
		Board board = new Board();
		board.grid[3][3] = Board.WHITE;
		System.out.println(bestMove(Board.WHITE, board, 1));
		board.grid[0][1] = Board.WHITE;
		System.out.println(eval(Board.WHITE, board));
		board.grid[0][1] = 0;
		board.grid[5][4] = Board.WHITE;
		System.out.println(eval(Board.WHITE, board));
		board.grid[5][4] = 0;
		board.grid[4][4] = Board.WHITE;
		System.out.println(eval(Board.WHITE, board));
		testBestMove2();
		testHasNetwork2();
	}
	
	

}
