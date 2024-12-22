package com.game.ed_p1_grupo_01.modelo;

import java.util.LinkedList;

public class NodeTree<E> {
    private E content;
    private LinkedList<Tree<E>> children;

    public NodeTree(E content){
        this.content = content;
        this.children = new LinkedList<>();
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public LinkedList<Tree<E>> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<Tree<E>> children) {
        this.children = children;
    }

    public Tree<E> getChild(int index) {
        return this.children.get(index);
    }

    public boolean setChild(Tree<E> tree) {
        return this.children.add(tree);
    }
}