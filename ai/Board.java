package ai;

import list.DList;
import list.InvalidNodeException;

import list.List;
import player.Move;

public class Board {

  public final static int BLACK = -1, EMPTY = 0, WHITE = 1;
  public final static int DEFAULT_BOARD_SIZE = 8;

  private enum Direction {
    N, E, S, W, NE, SE, SW, NW, NONE
  }

  private List blackChips, whiteChips;

  public int[][] grid;

  public Board() {
    grid = new int[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
  }

  public Board(int[][] init) {
    grid = init;
  }

  /**
   * makeMove() tries to make the given move on this board.
   * 
   * @param color the turn of the current player (determined by color)
   * @param move the proposed move
   * @return if the move was made (i.e. valid)
   * 
   * @author Austin Chen
   */
  public boolean makeMove(int color, Move move) {
    if (!isValid(color, move)) {
      return false;
    }

    makeMoveHelper(color, EMPTY, move);
    return true;
  }

  /**
   * unmakeMove() does the opposite of the given move on this board.
   * It presupposes that this is a valid action.
   * 
   * @param color the turn of the current player (determined by color)
   * @param move the proposed move
   * 
   * @author Austin Chen
   */
  public void unmakeMove(int color, Move move) {
    makeMoveHelper(EMPTY, color, move);
  }

  /**
   * makeMoveHelper() sets the board to color1 at x1, y1 of move. If move is a
   * STEP, it also sets the board to color2 at x2, y2.
   * 
   * @param color1 first color to set
   * @param color2 second color to set, if move is a STEP
   * @param move the proposed move
   */
  private void makeMoveHelper(int color1, int color2, Move move) {
    grid[move.x1][move.y1] = color1;
    if (move.moveKind == Move.STEP) {
      grid[move.x2][move.y2] = color2;
    }

    // Reset the memoized lists. These will be recalculated when needed.
    blackChips = null;
    whiteChips = null;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(" ");
    for (int i = 0; i < grid.length; i++) {
      result.append(" " + i);
    }
    for (int i = 0; i < grid.length; i++) {
      result.append("\n" + i);
      for (int j = 0; j < grid[i].length; j++) {
        switch (grid[j][i]) {
        case BLACK:
          result.append(" B");
          break;
        case WHITE:
          result.append(" W");
          break;
        default:
          result.append(" .");
          break;
        }
      }
    }
    return result.toString();
  }

  /**
   * isValid() determines if a given move for a given player on this board
   * is valid.
   * 
   * @param color the turn of the current player (determined by color)
   * @param move the proposed move
   * @return if the proposed move is valid
   * 
   * @author Alec Mouri
   */
  public boolean isValid(int color, Move move) {
    //No chip may be placed in any of the four corners of the board
    //So x1, y1 cannot simultaneously be a multiple of 7
    int oldX = move.x2;
    int oldY = move.y2;
    int x = move.x1;
    int y = move.y1;
    int len = grid.length - 1;
    int adjacent = 0;

    //If less than 10 moves in game, cannot make step moves. Otherwise cannot make add moves.
    if ((move.moveKind == Move.STEP && numberChips(color) < 10)
        || (move.moveKind == Move.ADD && numberChips(color) >= 10)) {
      return false;
    }

    if (x % len == 0 && y % len == 0) {
      return false;
    }

    //No chip may be placed in a goal of the opposite color.
    //Note that Black cannot be placed in the left/right goal spaces,
    //and White cannot be placed in the top/down goal spaces
    if ((color == BLACK && x % len == 0) || (color == WHITE && y % len == 0)) {
      return false;
    }

    //No chip may be placed in a square that is already occupied.
    if (grid[x][y] != EMPTY) {
      return false;
    }

    if (move.moveKind == Move.STEP) {
      //Cannot step to same location
      if (x == oldX && y == oldY) {
        return false;
      }
      //Cannot move nonexistent piece
      if (grid[oldX][oldY] == EMPTY) {
        return false;
      }
      grid[oldX][oldY] = EMPTY;
    }
    //Groups of more than two chips of the same color are not allowed.
    //Naive implementation since depth of search is at most 2
    //Note: can probably be optimized.
    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {

        //Do not consider case of nonexistent adjacent square and the square we want to make sure that is valid
        if (i < 0 || i > len || j < 0 || j > len || (i == x && j == y)) {
          continue;
        }
        //If an adjacent chip of the same color is found then check chips adjacent to those
        if (!(i == x && j == y) && grid[i][j] == color) {
          adjacent += 1;
          //If two adjacent pieces, then move is not valid
          if (adjacent == 2) {
            if (move.moveKind == Move.STEP) {
              grid[oldX][oldY] = color;
            }
            return false;
          }
          for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
              //Do not consider case of nonexistent adjacent square and coordinates of
              //proposed move.
              if (k < 0 || k > len || l < 0 || l > len || (k == i && l == j)) {
                continue;
              }

              //If a chip of the same color is found then move is illegal
              if (grid[k][l] == color) {
                if (move.moveKind == Move.STEP) {
                  grid[oldX][oldY] = color;
                }
                return false;
              }
            }
          }
        }
      }
    }
    if (move.moveKind == Move.STEP) {
      grid[oldX][oldY] = color;
    }

    //All rules are satisfied so return true
    return true;
  }
  
  private int numberChips(int color) {
    return locationOfPieces(color).length();
  }

  /**
   * locationOfPieces() returns a list of all the given pieces of
   * a player.
   * 
   * @param color the color of the pieces of the player
   * @return a list of the location of the pieces
   * 
   * @author Austin Chen and Michael Liu
   */
public List locationOfPieces(int color) {
    // If the list was already calculated, use that.
    List rightList = color == WHITE ? whiteChips : blackChips;
    if (rightList != null) {
      return rightList;
    }

    // Otherwise, calculate the list and save it.
    DList listed = new DList();
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[0].length; y++) {
        if (grid[x][y] == color) {
          listed.insertFront(new int[] { x, y });
        }
      }
    }

    if (color == WHITE) {
      whiteChips = listed;
    }
    else {
      blackChips = listed;
    }
    return listed;
  }

  /**
   * allValidMoves() creates a list of all valid Moves for a given board and
   * player.
   * 
   * @param color the turn of the current player (determined by color)
   * @return
   * 
   * @author Michael Liu
   */
  public List allValidMoves(int color) {
    DList validList = new DList();
    List pieces = locationOfPieces(color);
    //differentiates adding and moving pieces
    if (pieces.length() <= 9) {
      //iterating through both dimensions of the board
      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
          Move m = new Move(i, j);
          if (isValid(color, m)) {
            validList.insertBack(m);
          }
        }
      }
    }
    else {
      for (Object o : pieces) {
        for (int i = 0; i < grid.length; i++) {
          for (int j = 0; j < grid[0].length; j++) {
            Move m = new Move(i, j, ((int[]) o)[0], ((int[]) o)[1]);
            if (isValid(color, m)) {
              validList.insertBack(m);
            }
          }
        }
      }
    }
    return validList;
  }

  /**
   * connectedChips() creates a list of all chips which are connected to a
   * given chip on the board. Chips are represented by an int array of length
   * 2, in the form of {x, y}. If there is no chip at that location, return
   * an empty List.
   * 
   * @param chip the position of the chip to check for connections
   * @return a List of int arrays of chip positions
   * 
   * @author Austin Chen
   */
  public List connectedChips(int[] chip) {
    int x0 = chip[0], y0 = chip[1];
    if (!inBounds(x0, y0) || grid[x0][y0] == EMPTY) {
      return new DList();
    }

    List connectedChips = new DList();
    // i and j form offsets for each of the 8 directions
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (!(i == 0 && j == 0)) {
          // Keep incrementing x and y by i and j, until out of
          // bounds or chip found
          int x = x0 + i, y = y0 + j;
          while (inBounds(x, y) && grid[x][y] == EMPTY) {
            x += i;
            y += j;
          }
          // Add to the list if it's the same color as the original
          if (inBounds(x, y) && grid[x][y] == grid[x0][y0]) {
            connectedChips.insertBack(new int[] { x, y });
          }
        }
      }
    }
    return connectedChips;
  }

  private boolean inBounds(int x, int y) {
    return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
  }

  /**
   * hasNetwork() checks to see if a board contains a winning network for a
   * given player.
   * 
   * @param color the turn of the current player (determined by color)
   * @param board the current state of the board
   * @return if there is a valid network
   * 
   * @author Alec Mouri
   */
  public boolean hasNetwork(int color) {
    List pieces = locationOfPieces(color);
    for (Object curr : pieces) {
      int[] c = (int[]) curr;
      //disregard initial path that is not in goal area
      if ((color == WHITE && c[0] % (grid.length - 1) == 0)
          || (color == BLACK && c[1] % (grid.length - 1) == 0)) {
        //memoized list
        List network = new DList();
        network.insertBack(c);
        if (hasNetworkHelper(network, c, color, Direction.NONE)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * hasNetworkHelper() is a helper method for hasNetwork() that checks for all rules of a valid network and performs a tree
   * search to check all possible candidate networks until a valid one is found.
   * 
   * @param memo the memoized list of previous chips in the network
   * @param pos the position of the latest chip in the network
   * @param color the color of the network we are detecting the network for
   * @param board the current state of the board
   * @param dir the previous direction that was searched for
   * @return whether a network of a color exists that starts from pos
   * 
   * @author Alec Mouri
   */
  private boolean hasNetworkHelper(List memo, int[] pos, int color,
      Direction dir) {
    int len = grid.length - 1;

    List chips = connectedChips(pos);
    try {
      //If network length is at least 6 and network begins and ends at opposite goals
      if (memo.length() >= 6
          && ((color == WHITE && pos[0] % len == 0 && pos[0] != ((int[]) memo
              .front().item())[0]) || (color == BLACK && pos[1] % len == 0 && pos[1] != ((int[]) memo
              .front().item())[1]))) {
        return true;
      }
      else if (memo.length() != 1
          && ((color == WHITE && pos[0] % len == 0) || (color == BLACK && pos[1]
              % len == 0))) {
        return false;
      }
    } catch (InvalidNodeException e) {
      //do nothing
    }

    //iterate over all chips connected to current chip
    for (Object curr : chips) {
      int[] c = (int[]) curr;
      Direction newDir = getDirection(pos, c);
      if (dir != newDir && !inNetwork(memo, c)) {
        memo.insertBack(c);
        if (hasNetworkHelper(memo, c, color, newDir)) {
          return true;
        }
        else {
          try {
            memo.back().remove();
          } catch (InvalidNodeException e) {

          }
        }
      }
    }

    //No possible connections found, return false;
    return false;
  }

  /**
   * Helper function for hasNetworkHelper() for determining the direction that the path takes when going from
   * one point to another.
   * @param pos A starting point on the path.
   * @param newPos An ending point on the path
   * @return A direction that corresponds to the direction of the path from pos to newPos
   * 
   * @author Alec Mouri
   */
  private static Direction getDirection(int[] pos, int[] newPos) {
    if (pos[0] > newPos[0] && pos[1] < newPos[1]) {
      return Direction.SW;
    }
    else if (pos[0] < newPos[0] && pos[1] < newPos[1]) {
      return Direction.SE;
    }
    else if (pos[0] < newPos[0] && pos[1] > newPos[1]) {
      return Direction.NE;
    }
    else if (pos[0] > newPos[0] && pos[1] > newPos[1]) {
      return Direction.NW;
    }
    else if (pos[0] > newPos[0]) {
      return Direction.W;
    }
    else if (pos[0] < newPos[0]) {
      return Direction.E;
    }
    else if (pos[1] < newPos[1]) {
      return Direction.S;
    }
    else {
      return Direction.N;
    }
  }

  private static boolean inNetwork(List memo, int[] pos) {
    for (Object o : memo) {
      int[] curr = (int[]) o;
      if (pos[0] == curr[0] && pos[1] == curr[1]) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Utility function for finding the longest path length for a given color. Used by AI.
   * @param color
   * @return length of the longest path for a given color on the board.
   * 
   * @author Alec Mouri
   */
  public int longestPathLength(int color) {
    int length = 0;
    List pieces = locationOfPieces(color);
    for (Object curr : pieces) {
      int[] c = (int[]) curr;
      List network = new DList();
      network.insertBack(c);
      length = Math.max(
          longestPathLengthHelper(network, c, color, Direction.NONE), length);
    }
    return length;
  }

  /**
   * Helper function for longestPathLength() that finds all possible paths from a piece and
   * returns the maximum length of those paths.
   * @param memo A list of pieces that grows as the function is repeatedly called
   * @param pos The piece that we are considering
   * @param color The color of the piece
   * @param dir The direction from which we arrived at the piece
   * @return The longest path length that has pos as its root
   * 
   * @author Alec Mouri
   */
  private int longestPathLengthHelper(List memo, int[] pos, int color,
      Direction dir) {
    int pathLength = memo.length();

    List chips = connectedChips(pos);

    //iterate over all chips connected to current chip
    for (Object curr : chips) {
      int[] c = (int[]) curr;
      Direction newDir = getDirection(pos, c);
      if (dir != newDir && !inNetwork(memo, c)) {
        memo.insertBack(c);
        pathLength = Math.max(longestPathLengthHelper(memo, c, color, newDir),
            pathLength);
      }
    }

    //No possible connections found, return length of memoized list;
    return pathLength;
  }

}
