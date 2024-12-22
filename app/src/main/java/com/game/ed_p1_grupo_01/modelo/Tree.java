package com.game.ed_p1_grupo_01.modelo;

import com.game.ed_p1_grupo_01.elements.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree<E> implements Serializable {
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

    public int countLevels(){
        if(this.isEmpty()){
            return 0;
        }
        if(this.isLeaf()){
            return 1;
        }

        int maxlevels = 0;
        for(Tree tree: this.getRoot().getChildren()){
            if(!tree.isEmpty()){
                maxlevels = Math.max(maxlevels, tree.countLevels());
            }
        }

        return maxlevels + 1;
    }

    public static Tree<GameTable> TreeTable(GameTable table){
        Tree<GameTable> tree = new Tree<>(table);
        boolean isPlayer1 = table.isPlayer1Turn();
        for(int[] pos: GameTable.TABLEPOSITIONS){
            Token token = new Token(isPlayer1,pos[0],pos[1]);
            //noinspection unchecked
            ArrayList<Token> currentTokens = (ArrayList<Token>) table.getTokens().clone();
            GameTable tableCurrentTurn = new GameTable(currentTokens, isPlayer1);
            if(tableCurrentTurn.setToken(token)){
                tree.getRoot().setChild(new Tree<>(tableCurrentTurn));
            }
        }
        for(Tree<GameTable> treeChild: tree.getRoot().getChildren()){
            GameTable tableLastTurn = treeChild.getRoot().getContent();
            for(int[] pos: GameTable.TABLEPOSITIONS){
                Token token = new Token(!isPlayer1,pos[0],pos[1]);
                //noinspection unchecked
                ArrayList<Token> currentTokens = (ArrayList<Token>) tableLastTurn.getTokens().clone();
                GameTable tableCurrentTurn = new GameTable(currentTokens, !tableLastTurn.isPlayer1Turn());
                if(tableCurrentTurn.setToken(token)){
                    treeChild.getRoot().setChild(new Tree<>(tableCurrentTurn));
                }
            }
        }
        return tree;
    }
}
