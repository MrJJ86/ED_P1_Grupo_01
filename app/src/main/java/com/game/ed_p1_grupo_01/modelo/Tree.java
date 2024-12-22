package com.game.ed_p1_grupo_01.modelo;

public class Tree<E> {
    private NodeTree<E> root;

    public Tree(){
        this.root = null;
    }
    public Tree(NodeTree<E> root){
        this.root = root;
    }
    public Tree(E content){
        this.root = new NodeTree<>(content);
    }

    public NodeTree<E> getRoot() {
        return root;
    }

    public void setRoot(NodeTree<E> root) {
        this.root = root;
    }

    public boolean isEmpty(){
        return this.root == null;
    }

    public boolean isLeaf(){
        if(!this.isEmpty()){
            return this.root.getChildren().isEmpty();
        }
        return false;
    }
}