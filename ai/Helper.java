package ai;

import list.List;
import player.Move;

public class Helper {

    /**
     * isValid() determines if a given move for a given player on a given board
     * is valid.
     * 
     * @param color the turn of the current player (determined by color)
     * @param move the proposed move
     * @param board the current state of the board
     * @return if the proposed move is valid
     */
    public boolean isValid(int color, Move move, int[][] board) {
        return false;
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

    }

}
