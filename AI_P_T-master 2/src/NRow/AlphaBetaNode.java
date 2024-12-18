package NRow;

public class AlphaBetaNode extends Node{
    private int score;
    private int column;

    public AlphaBetaNode(int score, int column){this.score=score; this.column=column;}

    public int getScore(){return score;}
    public int getColumn(){return column;}
}
