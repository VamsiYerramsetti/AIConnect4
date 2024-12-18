package NRow;
import java.util.LinkedList;
import java.util.List;

public abstract class Node{
    Node parent;
    List<Node> children = new LinkedList<>();

    public void setParent(Node parent){
        this.parent = parent;
    }

    public void addChild(Node node){
        children.add(node);
    }

    public void removeChild(Node node){
        children.remove(node);
    }
}
