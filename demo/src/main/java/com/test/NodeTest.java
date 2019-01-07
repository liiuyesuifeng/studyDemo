package com.test;

import com.example.demo.NodeTree;
import com.google.gson.Gson;
import com.utils.PrintUtil;
import org.junit.Test;

public class NodeTest {
    private String[] city = {"北京", "武汉"};
    private String[] cq = {"东城区", "西城区", "南城区", "北城区"};

    @Test
    public void test1() {
        NodeTree node1 = new NodeTree("中国");
        for (String city1 : city) {
            NodeTree node2 = new NodeTree(city1);
            for (String cq : cq) {
                NodeTree node3 = new NodeTree(cq);
                node2.setNextNode(node3);
            }
            node1.setNextNode(node2);
        }
        Gson gson = new Gson();
        String s = gson.toJson(node1);
        PrintUtil.print(s);
    }

}
