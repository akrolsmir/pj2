package ai;

import player.Move;

public class AI {

	public final static int BLACK = -1, EMPTY = 0, WHITE = 1;
	public final static int QUIT = 0;
	public final static int ADD = 1;
	public final static int STEP = 2;
	
	/**
	 * eval() analyzes the current board with respect to the current player 
	 * for a given move returning a value between -1.0 and 1.0 signifying
	 * the strength of the given move.
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @param depth the depth to which this checks
	 * @return a double signifying the strength of a move
	 * 
	 * @author Alec Mouri, Austin Chen, Michael Liu
	 */
	public static double eval(int color, int[][]board, int depth){
		return Math.random();
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
	public static Object[] bestMove(int color, int[][]board, int depth, double alpha, double beta){
		Object[] optimalMove = new Object[2];
		
		if(depth == 0) {
			optimalMove[1] = eval(color, board, depth);
			return optimalMove;
		}
		
		if(color == WHITE) {
			for(Object o: Helper.allValidMoves(color, board)) {
				if(((Move) o).moveKind == ADD) {
					board[((Move) o).y1][((Move) o).x1] = WHITE;
					Object[] trial = bestMove(BLACK, board, depth - 1, alpha, beta);
					if(alpha < (Double) trial[1]) {
						optimalMove[0] = o;
						alpha = (Double) trial[1];
					}
					board[((Move) o).y1][((Move) o).x1] = EMPTY;
					if(beta <= alpha) {
						break;
					}
				} else {
					board[((Move) o).y1][((Move) o).x1] = WHITE;
					board[((Move) o).y2][((Move) o).x2] = EMPTY;
					Object[] trial = bestMove(BLACK, board, depth - 1, alpha, beta);
					if(alpha < (Double) trial[1]) {
						optimalMove[0] = o;
						alpha = (Double) trial[1];
					}
					board[((Move) o).y1][((Move) o).x1] = EMPTY;
					board[((Move) o).y2][((Move) o).x2] = WHITE;
					if(beta <= alpha) {
						break;
					}
				}
			}
		} else {
			for(Object o: Helper.allValidMoves(color, board)) {
				if(((Move) o).moveKind == ADD) {
					board[((Move) o).y1][((Move) o).x1] = BLACK;
					Object[] trial = bestMove(WHITE, board, depth - 1, alpha, beta);
					if(beta < (Double) trial[1]) {
						optimalMove[0] = o;
						beta = (Double) trial[1];
					}
					board[((Move) o).y1][((Move) o).x1] = EMPTY;
					if(beta <= alpha) {
						break;
					}
				} else {
					board[((Move) o).y1][((Move) o).x1] = BLACK;
					board[((Move) o).y2][((Move) o).x2] = EMPTY;
					Object[] trial = bestMove(WHITE, board, depth - 1, alpha, beta);
					if(beta < (Double) trial[1]) {
						optimalMove[0] = o;
						beta = (Double) trial[1];
					}
					board[((Move) o).y1][((Move) o).x1] = EMPTY;
					board[((Move) o).y2][((Move) o).x2] = BLACK;
					if(beta <= alpha) {
						break;
					}
				}
			}
		}
		return optimalMove;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
