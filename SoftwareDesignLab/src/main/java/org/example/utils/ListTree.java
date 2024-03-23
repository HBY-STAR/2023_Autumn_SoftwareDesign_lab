package org.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Node {
    public List<String> doc_line;
    public int depth;
    public List<Node> children = new ArrayList<>();
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTree {
    public Node root = new Node();
    public StringBuilder print=new StringBuilder();

    public int build_tree(List<List<String>> doc_lines, int start, Node cur_node_parent){
        if(doc_lines.size()==0){
            return 0;
        }
        Node cur_parent = new Node(doc_lines.get(start),getDepth(doc_lines.get(start).get(0)),new ArrayList<>());
        for(int i=start;i<doc_lines.size();i++){
            Node cur_node = new Node(doc_lines.get(i),getDepth(doc_lines.get(i).get(0)),new ArrayList<>());
            if(cur_node.depth<cur_node_parent.depth){
                if(cur_node.depth==cur_parent.depth){
                    cur_node_parent.children.add(cur_node);
                    cur_parent=cur_node;
                }else if(cur_node.depth< cur_parent.depth){
                    i=build_tree(doc_lines,i,cur_parent)-1;
                }else {
                    return i;
                }
            }else{
                return i;
            }
        }
        return doc_lines.size();
    }

    public void PrintDir(List<List<String>> doc_lines,int start){
        root=new Node(doc_lines.get(start),getDepth(doc_lines.get(start).get(0)),new ArrayList<>());
        print=new StringBuilder();
        build_tree(doc_lines,start+1,root);
        String temp = "└── " + getStringFromNode(root) + "\n";
        print.append(temp);
        print_tree(root,"    ");
        System.out.print(print);
    }

    public void PrintAll(List<List<String>> doc_lines){
        root=new Node(null,100,new ArrayList<>());
        print=new StringBuilder();
        build_tree(doc_lines,0,root);
        print_tree(root,"");
        System.out.print(print);
    }

    public void print_tree(Node node, String prefix){
        StringBuilder temp = new StringBuilder();
        if(node.children.size()>0){
            for(int i=0;i<node.children.size()-1;i++){
                Node child = node.children.get(i);
                temp = new StringBuilder();
                temp.append(prefix).append("├── ").append(getStringFromNode(child)).append("\n");
                print.append(temp);
                print_tree(child,prefix+"│   ");
            }
            Node last_child = node.children.get(node.children.size()-1);
            temp=new StringBuilder();
            temp.append(prefix).append("└── ").append(getStringFromNode(last_child)).append("\n");
            print.append(temp);
            print_tree(last_child,prefix+"    ");
        }
    }

    private String getStringFromNode(Node child) {
        StringBuilder temp=new StringBuilder();
        if(getDepth(child.doc_line.get(0))==0){
            temp.append(child.doc_line.get(0));
            for(int i=1;i<child.doc_line.size();i++){
                temp.append(child.doc_line.get(i));
            }
        }else {
            temp.append(child.doc_line.get(1));
        }
        return temp.toString();
    }

    private static int getDepth(String head) {
        int cur_dept;
        switch (head) {
            case "#" -> cur_dept = 6;
            case "##" -> cur_dept = 5;
            case "###" -> cur_dept = 4;
            case "####" -> cur_dept = 3;
            case "#####" -> cur_dept = 2;
            case "######" -> cur_dept = 1;
            default -> cur_dept = 0;
        }
        return cur_dept;
    }


}
