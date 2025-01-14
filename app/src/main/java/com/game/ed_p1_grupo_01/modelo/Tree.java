package com.game.ed_p1_grupo_01.modelo;

import android.util.Log;

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

    /**
     * Genera un árbol de posibles tableros a partir del estado actual.
     *
     * @param table El tablero actual.
     * @return Un árbol de posibles estados del tablero.
     */
    public static Tree<GameTable> TreeTable(GameTable table) {
        Tree<GameTable> tree = new Tree<>(table);
        boolean isPlayer1 = table.isPlayer1Turn();

        // Obtener posiciones disponibles
        ArrayList<int[]> availablePositions = table.getAvailablePositions();

        // Crear nodos hijos para el primer nivel
        for (int[] pos : availablePositions) {
            Token token = new Token(isPlayer1, pos[0], pos[1]);
            GameTable newTable = table.clone();
            if (newTable.setToken(token)) {
                Tree<GameTable> childTree = new Tree<>(newTable);
                tree.getRoot().setChild(childTree);

                // Crear nodos hijos para el segundo nivel
                boolean nextPlayer = !isPlayer1;
                ArrayList<int[]> nextAvailablePositions = newTable.getAvailablePositions();
                for (int[] nextPos : nextAvailablePositions) {
                    Token nextToken = new Token(nextPlayer, nextPos[0], nextPos[1]);
                    GameTable nextTable = newTable.clone();
                    if (nextTable.setToken(nextToken)) {
                        childTree.getRoot().setChild(new Tree<>(nextTable));
                    }
                }
            }
        }
        return tree;
    }

}
