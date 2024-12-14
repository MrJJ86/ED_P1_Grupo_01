package com.game.ed_p1_grupo_01.elements;

import com.game.ed_p1_grupo_01.data_structure.Tree;

import java.io.Serializable;
import java.util.ArrayList;

public class GameTable implements Serializable {
    private Tree<GameTable> gameTree;
    private ArrayList<Token> tokens;


    public GameTable() {
        this.gameTree = new Tree<>(this);
        this.tokens = new ArrayList<>();
    }

    public Tree<GameTable> getGameTree() {
        return gameTree;
    }

    public void setGameTree(Tree<GameTable> gameTree) {
        this.gameTree = gameTree;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    private boolean positionIsEmpty(int positionX, int positionY){
        for (Token token : tokens) {
            if (token.getPositionX() == positionX && token.getPositionY() == positionY) {
                return false;
            }
        }
        return true;
    }

    public boolean setToken(Token token){
        if(positionIsEmpty(token.getPositionX(), token.getPositionY())){
            tokens.add(token);
            return true;
        }
        return false;
    }

    public boolean gameIsEnd(){
        if(tokens.size() < 5){
            return false;
        }
        ArrayList<Token> player1 = new ArrayList<>();
        ArrayList<Token> player2 = new ArrayList<>();
        for(Token token: tokens){
            if(token.isPlayer1()) player1.add(token);
            player2.add(token);
        }
        //TODO: COMPLETAR MÉTODO
        return true;
    }

    public void computerProcess(){

    }
}
