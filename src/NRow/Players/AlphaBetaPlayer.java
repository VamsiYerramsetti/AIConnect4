package NRow.Players;

import NRow.AlphaBetaNode;
import NRow.Board;
import NRow.Game;
import NRow.Heuristics.Heuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlphaBetaPlayer extends PlayerController{
    private int depth;
    private int otherPlayerId;

    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
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
    public AlphaBetaNode alphaBeta(Board board, int depth, int alpha, int beta, Boolean isMaxPlayer){
        //List of all valid locations the player is allowed to play in
        List<Integer> validLocations = getValidLocations(board);
        //Is this board in a winning or losing state?
        int isTerminalNode = Game.winning(board.getBoardState(), gameN);
        if(isTerminalNode != 0){
            //The MinMax Player won
            if(isTerminalNode == playerId){
                return new AlphaBetaNode(Integer.MAX_VALUE, -1);
            }
            //The other player won
            else if(isTerminalNode == otherPlayerId){
                return new AlphaBetaNode(Integer.MIN_VALUE, -1);
            }
            //It's a draw
            else if(isTerminalNode == -1){
                return new AlphaBetaNode(0, -1);
            }
        }
        //Base case for recursive function, max depth reached
        else if (depth == 0){
            //Use the heuristic to evaluate the reached board state
            return new AlphaBetaNode(heuristic.evaluateBoard(isMaxPlayer ? playerId : otherPlayerId, board), -1);
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
                int newScore = alphaBeta(copy, depth-1, alpha, beta, false).getScore();
                if(newScore > value){
                    value = newScore;
                    column = col;
                }
                alpha = Integer.max(alpha, value);
                if(alpha >= beta){
                    break;
                }
            }
        }
        else //Minimizing player
        {
            value = Integer.MAX_VALUE;
            for(int i = 0; i < validLocations.size(); i++){
                int col = validLocations.get(i);
                Board copy = board.getNewBoard(col, otherPlayerId);
                int newScore = alphaBeta(copy, depth-1, alpha, beta, true).getScore();
                if(newScore < value){
                    value = newScore;
                    column = col;
                }
                beta = Integer.min(beta, value);
                if(alpha >= beta){
                    break;
                }
            }
        }
        return new AlphaBetaNode(value, column);
    }

    /**
     * Implement this method yourself!
     * @param board the current board
     * @return column integer the player chose
     */
    @Override
    public int makeMove(Board board) {
        int move = alphaBeta(board, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true).getColumn();
        return move;
    }
}
