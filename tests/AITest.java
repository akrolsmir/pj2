package tests;

import ai.AI;
import ai.Board;

public class AITest {

  private static void testBestMove() {
    Board board = new Board();
    System.out.println(AI.bestMove(Board.WHITE, board, 1));
    board.makeMove(Board.WHITE, AI.bestMove(Board.WHITE, board, 1));
    System.out.println(board);
    System.out.println(AI.bestMove(Board.BLACK, board, 1));
    board.makeMove(Board.BLACK, AI.bestMove(Board.BLACK, board, 1));
    System.out.println(board);
    System.out.println(AI.bestMove(Board.WHITE, board, 1));
    board.makeMove(Board.WHITE, AI.bestMove(Board.WHITE, board, 1));
    System.out.println(board);

  }

  private static void testBestMove2() {
    Board board = new Board();
    board.grid[6][0] = Board.BLACK;
    board.grid[6][5] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[5][7] = Board.BLACK;
    //		board.grid[5][5] = Board.BLACK;
    System.out.println(board);
    //		System.out.println(board.allValidMoves(Board.BLACK));
    System.out.println(AI.eval(Board.BLACK, board));
    System.out.println(AI.bestMove(Board.BLACK, board, 2) + ", should be 63"); //55 wins
    System.out.println(AI.bestMove(Board.WHITE, board, 2) + ", should be 34"); //55 blocks

  }

  private static void testHasNetwork2() {
    Board board = new Board();
    board.grid[6][0] = Board.BLACK;
    board.grid[6][5] = Board.BLACK;
    board.grid[5][5] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[5][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == true));
    //		System.out.println(board);
    board = new Board();

    board.grid[2][0] = Board.BLACK;
    board.grid[2][5] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[1][3] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[5][5] = Board.BLACK;
    board.grid[5][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == true));
    //		System.out.println(board);
    board = new Board();

    //		not win

    board.grid[6][0] = Board.BLACK;
    board.grid[2][0] = Board.BLACK;
    board.grid[4][2] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[5][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == false));
    //		System.out.println(board);

    board = new Board();
    board.grid[2][0] = Board.BLACK;
    board.grid[4][2] = Board.BLACK;
    board.grid[6][0] = Board.BLACK;
    board.grid[6][5] = Board.BLACK;
    board.grid[5][5] = Board.BLACK;
    board.grid[5][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == false));
    //		System.out.println(board);

    board = new Board();
    board.grid[2][0] = Board.BLACK;
    board.grid[2][5] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[5][5] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[5][6] = Board.WHITE;
    board.grid[5][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == false));
    //		System.out.println(board);

    board = new Board();
    board.grid[6][0] = Board.BLACK;
    board.grid[4][2] = Board.BLACK;
    board.grid[3][3] = Board.BLACK;
    board.grid[3][5] = Board.BLACK;
    board.grid[2][5] = Board.BLACK;
    board.grid[2][7] = Board.BLACK;
    System.out.println("passed test? "
        + (board.hasNetwork(Board.BLACK) == false));
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
    //		testAI.bestMove();
    Board board = new Board();
    board.grid[0][2] = Board.WHITE;
    //		System.out.println(AI.bestMove(Board.WHITE, board, 1));
    board.grid[1][3] = Board.WHITE;
    //		System.out.println(eval(Board.WHITE, board));
    //		board.grid[0][1] = 0;
    board.grid[3][2] = Board.WHITE;
    //		System.out.println(eval(Board.WHITE, board));
    //		board.grid[5][4] = 0;
    board.grid[3][5] = Board.WHITE;
    //		System.out.println(eval(Board.WHITE, board));
    //		testAI.bestMove2();
    board.grid[5][3] = Board.WHITE;
    board.grid[1][1] = Board.BLACK;
    board.grid[2][1] = Board.BLACK;
    board.grid[1][6] = Board.BLACK;
    board.grid[2][6] = Board.BLACK;
    board.grid[4][1] = Board.BLACK;
    board.grid[5][1] = Board.BLACK;
    board.grid[6][1] = Board.WHITE;
    board.grid[6][5] = Board.WHITE;
    board.grid[5][5] = Board.BLACK;
    //		System.out.println(board.hasNetwork(board.BLACK));
    System.out.println(board);
    //		System.out.println(eval(Board.WHITE, board));
    System.out.println(AI.bestMove(Board.WHITE, board, 1));

    testHasNetwork2();
    testBestMove();
    testBestMove2();

  }

}
