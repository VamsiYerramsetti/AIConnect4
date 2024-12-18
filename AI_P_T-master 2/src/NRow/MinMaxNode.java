package NRow;

import java.util.List;
public class MinMaxNode extends Node{
    private int score;
    private int column;

    public MinMaxNode(int score, int column){this.score=score; this.column=column;}

    public int getScore(){return score;}
    public int getColumn(){return column;}
}
