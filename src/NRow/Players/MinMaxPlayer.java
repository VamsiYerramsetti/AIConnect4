package NRow.Players;

import NRow.Board;
import NRow.Game;
import NRow.Heuristics.Heuristic;
import NRow.MinMaxNode;
import NRow.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MinMaxPlayer extends PlayerController {
    private int depth;
    private int otherPlayerId;

    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        this.otherPlayerId = playerId == 1 ? 2 : 1;
    }

    /**
     * Gets all valid locations for the given board
     * @param board the current board
     * @return List of all valid locations to make a move
     */
    public List<Integer> getValidLocations(Board board){
        List<Integer> validLocations = new ArrayList<>();
        for(int i = 0; i < board.width; i++){
            if(board.isValid(i)) validLocations.add(i);
        }
        return validLocations;
    }

    /**
     * Recursive function for making a move using the MinMax algorithm
     * @param board the current board
     * @param depth the current depth
     * @param isMaxPlayer should we evaluate as the max player
     * @return move to make containing the move's score and column
     */
    public MinMaxNode miniMax(Board board, int depth, Boolean isMaxPlayer){
        //List of all valid locations the player is allowed to play in
        List<Integer> validLocations = getValidLocations(board);
        //Is this board in a winning or losing state?
        int isTerminalNode = Game.winning(board.getBoardState(), gameN);
        if(isTerminalNode != 0){
            //The MinMax Player won
            if(isTerminalNode == playerId){
                return new MinMaxNode(Integer.MAX_VALUE, -1);
            }
            //The other player won
            else if(isTerminalNode == otherPlayerId){
                return new MinMaxNode(Integer.MIN_VALUE, -1);
            }
            //It's a draw
            else if(isTerminalNode == -1){
                return new MinMaxNode(0, -1);
            }
        }
        //Base case for recursive function, max depth reached
        else if (depth == 0){
            //Use the heuristic to evaluate the reached board state
            return new MinMaxNode(heuristic.evaluateBoard(isMaxPlayer ? playerId : otherPlayerId, board), -1);
        }
        //Iterate through all possible board options with respect to the max depth
        int value;
        int column = new Random().nextInt(validLocations.size());
        if(isMaxPlayer) //Maximizing player
        {
            value = Integer.MIN_VALUE;
            for(int i = 0; i < validLocations.size(); i++){
                int col = validLocations.get(i);
                Board copy = board.getNewBoard(col, playerId);
                int newScore = miniMax(copy, depth-1, false).getScore();
                if(newScore > value){
                    value = newScore;
                    column = col;
                }
            }
        }
        else //Minimizing player
        {
            value = Integer.MAX_VALUE;
            for(int i = 0; i < validLocations.size(); i++){
                int col = validLocations.get(i);
                Board copy = board.getNewBoard(col, otherPlayerId);
                int newScore = miniMax(copy, depth-1, true).getScore();
                if(newScore < value){
                    value = newScore;
                    column = col;
                }
            }
        }
        return new MinMaxNode(value, column);
    }

    public int alphaBeta(){
        return 0;
    }

    /**
   * Implement this method yourself!
   * @param board the current board
   * @return column integer the player chose
   */
    @Override
    public int makeMove(Board board) {
        int move = miniMax(board, this.depth, true).getColumn();
        return move;

        // TODO: implement minmax player!
        // HINT: use the functions on the 'board' object to produce a new board given a specific move
        // HINT: use the functions on the 'heuristic' object to produce evaluations for the different board states!

        // Example:
        /*int maxValue = Integer.MIN_VALUE;
        int maxMove = 0;
        for(int i = 0; i < board.width; i++) { //for each of the possible moves
            if(board.isValid(i)) { //if the move is valid
                Board newBoard = board.getNewBoard(i, playerId); // Get a new board resulting from that move
                int value = heuristic.evaluateBoard(playerId, newBoard); //evaluate that new board to get a heuristic value from it
                if(value > maxValue) {
                    maxMove = i;
                }
            }
        }*/
        // This returns the same as:
        //heuristic.getBestAction(playerId, board); // Very useful helper function!


        /*
        This is obviously not enough (this is depth 1)
        Your assignment is to create a data structure (tree) to store the gameboards such that you can evaluate a higher depths.
        Then, use the minmax algorithm to search through this tree to find the best move/action to take!
        */

//        return maxMove;
    }
}
