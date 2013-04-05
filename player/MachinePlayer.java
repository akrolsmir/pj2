/* MachinePlayer.java */

package player;

import ai.AI;
import ai.Board;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  private Board board = new Board();
  private int color, opponentColor, searchDepth;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this(color, 3);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    if (color == 0) {
      this.color = Board.BLACK;
      this.opponentColor = Board.WHITE;
    }
    else {
      this.color = Board.WHITE;
      this.opponentColor = Board.BLACK;
    }
    this.searchDepth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    //	long start = System.currentTimeMillis();
    Move move = AI.bestMove(color, board, searchDepth);
    board.makeMove(color, move);
    //    System.out.println("chooseMove took: " + (System.currentTimeMillis() - start) + " ms");
    return move;
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return board.makeMove(opponentColor, m);
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return board.makeMove(color, m);
  }

  public static void main(String args[]) {
    MachinePlayer player = new MachinePlayer(0);
    player.opponentMove(new Move(2, 0));
    player.opponentMove(new Move(2, 5));
    player.opponentMove(new Move(3, 5));
    player.opponentMove(new Move(1, 3));
    player.opponentMove(new Move(5, 5));
    player.opponentMove(new Move(5, 7));
    //opp's 33 wins
    System.out.println(player.chooseMove() + ", should be 33");
  }

}
