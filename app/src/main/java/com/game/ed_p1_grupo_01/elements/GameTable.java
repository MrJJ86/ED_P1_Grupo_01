package com.game.ed_p1_grupo_01.elements;

import com.game.ed_p1_grupo_01.data_structure.Tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GameTable implements Serializable {
    private Tree<GameTable> gameTree;
    private ArrayList<Token> tokens;
    private static final int[][][] POSITIONS ={
            //Horizontal
            {{0,0},{0,1},{0,2}},
            {{1,0},{1,1},{1,2}},
            {{2,0},{2,1},{2,2}},
            //Vertical
            {{0,1},{1,0},{2,0}},
            {{0,1},{1,1},{2,1}},
            {{0,2},{1,2},{2,2}},
            //Diagonal
            {{0,0},{1,1},{2,2}},
            {{0,2},{1,1},{2,0}}
    };


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

    /**
     * Verifica si una posición en el tablero está vacía.
     *
     * @param positionX la coordenada X de la posición.
     * @param positionY la coordenada Y de la posición.
     * @return {@code true} si la posición está vacía, {@code false} en caso contrario.
     */
    private boolean positionIsEmpty(int positionX, int positionY){
        for (Token token : tokens) {
            if (token.getPositionX() == positionX && token.getPositionY() == positionY) {
                return false;
            }
        }
        return true;
    }

    /**
     * Intenta colocar un token en el tablero en la posición especificada.
     *
     * @param token el token que se desea colocar.
     * @return {@code true} si el token se colocó con éxito, {@code false} si la posición ya estaba ocupada.
     */
    public boolean setToken(Token token){
        if(positionIsEmpty(token.getPositionX(), token.getPositionY())){
            tokens.add(token);
            return true;
        }
        return false;
    }

    /**
     * Verifica si el juego ha terminado. El juego termina si hay un ganador
     * o si se cumplen las condiciones mínimas para evaluar un ganador.
     *
     * @return {@code true} si el juego ha terminado, {@code false} en caso contrario.
     */
    public boolean gameIsEnd(){
        if(tokens.size() >= 5){
            ArrayList<Token> player1 = new ArrayList<>();
            ArrayList<Token> player2 = new ArrayList<>();
            Comparator<Token> comp = (t1,t2) -> {
                return t1.compareTo(t2);
            };
            player1.sort(comp);
            player2.sort(comp);

            for(Token token: tokens){
                if(token.isPlayer1()) player1.add(token);
                player2.add(token);
            }

            ArrayList<Token> player1Win = isGameWin(player1);
            ArrayList<Token> player2Win = isGameWin(player2);

            if (player1Win != null || player2Win != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si un jugador ha ganado el juego. Un jugador gana si tiene una secuencia de tres tokens
     * en las posiciones definidas por las combinaciones ganadoras.
     *
     * @param tokens los tokens del jugador a verificar.
     * @return una lista con los tokens que forman la secuencia ganadora si hay un ganador,
     *         o {@code null} si no hay secuencia ganadora.
     */
    private static ArrayList<Token> isGameWin(ArrayList<Token> tokens) {
        Map<String, Token> tokenMap = new HashMap<>();
        for (Token token : tokens) {
            String key = token.getPositionX() + "," + token.getPositionY();
            tokenMap.put(key, token);
        }

        for (int[][] positionGroup : POSITIONS) {
            ArrayList<Token> temporal = new ArrayList<>();

            for (int[] pos : positionGroup) {
                String key = pos[0] + "," + pos[1];
                if (tokenMap.containsKey(key)) {
                    temporal.add(tokenMap.get(key));
                } else {
                    break;
                }
            }

            if (temporal.size() == 3) {
                return temporal;
            }
        }

        return null;
    }



    public void computerProcess(){

    }
}
