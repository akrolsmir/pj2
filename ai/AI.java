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
	 * @param depth the depth to which this checks
	 * @return a double signifying the strength of a move
	 * 
	 * @author Alec Mouri, Austin Chen, Michael Liu
	 */
	public static double eval(int color, Board board, int depth){
		return Math.random();
	}
	
	public static Move bestMove(int color, Board board, int depth) {
		return (Move) bestMoveHelper(color, board, depth, -1.0, 1.-0)[1];
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
	private static Object[] bestMoveHelper(int color, Board board, int depth, double alpha, double beta){
		Object[] optimalMove = new Object[2];
		
		if(depth == 0) {
			optimalMove[1] = eval(color, board, depth);
			return optimalMove;
		}
		
		if(color == Board.WHITE) {
			for(Object o: board.allValidMoves(color)) {
				if(((Move) o).moveKind == Move.ADD) {
					board.grid[((Move) o).y1][((Move) o).x1] = Board.WHITE;
					Object[] trial = bestMoveHelper(Board.BLACK, board, depth - 1, alpha, beta);
					if(alpha < (Double) trial[1]) {
						optimalMove[0] = o;
						alpha = (Double) trial[1];
					}
					board.grid[((Move) o).y1][((Move) o).x1] = Board.EMPTY;
					if(beta <= alpha) {
						break;
					}
				} else {
					board.grid[((Move) o).y1][((Move) o).x1] = Board.WHITE;
					board.grid[((Move) o).y2][((Move) o).x2] = Board.EMPTY;
					Object[] trial = bestMoveHelper(Board.BLACK, board, depth - 1, alpha, beta);
					if(alpha < (Double) trial[1]) {
						optimalMove[0] = o;
						alpha = (Double) trial[1];
					}
					board.grid[((Move) o).y1][((Move) o).x1] = Board.EMPTY;
					board.grid[((Move) o).y2][((Move) o).x2] = Board.WHITE;
					if(beta <= alpha) {
						break;
					}
				}
			}
		} else {
			for(Object o: board.allValidMoves(color)) {
				if(((Move) o).moveKind == Move.ADD) {
					board.grid[((Move) o).y1][((Move) o).x1] = Board.BLACK;
					Object[] trial = bestMoveHelper(Board.WHITE, board, depth - 1, alpha, beta);
					if(beta < (Double) trial[1]) {
						optimalMove[0] = o;
						beta = (Double) trial[1];
					}
					board.grid[((Move) o).y1][((Move) o).x1] = Board.EMPTY;
					if(beta <= alpha) {
						break;
					}
				} else {
					board.grid[((Move) o).y1][((Move) o).x1] = Board.BLACK;
					board.grid[((Move) o).y2][((Move) o).x2] = Board.EMPTY;
					Object[] trial = bestMoveHelper(Board.WHITE, board, depth - 1, alpha, beta);
					if(beta < (Double) trial[1]) {
						optimalMove[0] = o;
						beta = (Double) trial[1];
					}
					board.grid[((Move) o).y1][((Move) o).x1] = Board.EMPTY;
					board.grid[((Move) o).y2][((Move) o).x2] = Board.BLACK;
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
