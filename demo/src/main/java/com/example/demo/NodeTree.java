package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class NodeTree {
    private String nodeName;
    private List<NodeTree> nextNodes = new ArrayList<>();

    public NodeTree(String nodeName) {
        this.nodeName = nodeName;
    }

    public NodeTree() {

    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<NodeTree> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(List<NodeTree> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public void setNextNode(NodeTree nextName) {
        getNextNodes().add(nextName);
    }

}
