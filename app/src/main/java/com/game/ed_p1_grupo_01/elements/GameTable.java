package com.game.ed_p1_grupo_01.elements;

import com.game.ed_p1_grupo_01.data_structure.Tree;
import com.game.ed_p1_grupo_01.operations.TableOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GameTable implements Serializable {
    public static final int[][] TABLEPOSITIONS = {
            {0,0},{0,1},{0,2},
            {1,0},{1,1},{1,2},
            {2,0},{2,1},{2,2}
    };
    private boolean isPlayer1Turn;
    private ArrayList<Token> tokens;


    public GameTable() {
        this.tokens = new ArrayList<>();
    }
    public GameTable(ArrayList<Token> tokens, boolean isPlayer1Turn){
        this.tokens = tokens;
        this.isPlayer1Turn = isPlayer1Turn;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        isPlayer1Turn = player1Turn;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
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
     * Verifica si el juego ha terminado. El juego termina si hay un ganador
     * o si se cumplen las condiciones mínimas para evaluar un ganador.
     *
     * @return {@code true} si el juego ha terminado, {@code false} en caso contrario.
     */
    public boolean gameIsEnd(){
        if(tokens.size() >= 5){
            ArrayList<Token> player1 = new ArrayList<>();
            ArrayList<Token> player2 = new ArrayList<>();
            Comparator<Token> comp = Token::compareTo;
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

        for (int[][] positionGroup : TableOperation.POSITIONS) {
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
        Tree.TreeTable(this);
    }
}
