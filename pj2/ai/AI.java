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
	 */
	public double eval(int color, int[][]board){
		return 0;
	}
	
	/**
	 * bestMove() returns the strongest move for the given player using 
	 * our evaluation function.
	 * 
	 * @param color the turn of the current player (determined by color)
	 * @param board the current state of the board
	 * @return a move with the strongest score using our evaluation function
	 */
	public Move bestMove(int color, int[][]board){
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
