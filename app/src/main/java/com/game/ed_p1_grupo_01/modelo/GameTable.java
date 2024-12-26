package com.game.ed_p1_grupo_01.modelo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GameTable implements Serializable {
    private Tree<GameTable> gameTree;
    private ArrayList<Token> tokens;
    public static final int[][][] POSITIONS ={
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

    /**
     * Constructor para inicializar un tablero con una lista de tokens y un estado.
     *
     * @param tokens Lista de tokens colocados en el tablero.
     */
    public GameTable(ArrayList<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        this.gameTree = new Tree<>(this);
    }
    /**
     * Crea una copia del estado actual del tablero.
     *
     * @return Una nueva instancia de GameTable con los mismos tokens.
     */
    @Override
    public GameTable clone() {
        ArrayList<Token> clonedTokens = new ArrayList<>();
        for (Token token : this.tokens) {
            clonedTokens.add(new Token(token.isPlayer1(), token.getPositionX(), token.getPositionY()));
        }
        return new GameTable(clonedTokens);
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
    /**
     * Determina de quién es el turno actual.
     *
     * @return {@code true} si es el turno del jugador 1, {@code false} si es el turno del jugador 2.
     */
    public boolean isPlayer1Turn() {
        // Cuenta los tokens colocados en el tablero
        int player1Tokens = 0;
        int player2Tokens = 0;

        for (Token token : tokens) {
            if (token.isPlayer1()) {
                player1Tokens++;
            } else {
                player2Tokens++;
            }
        }

        // El turno es del jugador 1 si el número de tokens del jugador 1 es menor o igual al del jugador 2
        return player1Tokens <= player2Tokens;
    }


    /**
     * Obtiene todas las posiciones disponibles en el tablero.
     *
     * @return Una lista de arreglos de dos enteros que representan las posiciones vacías.
     */
    public ArrayList<int[]> getAvailablePositions() {
        ArrayList<int[]> availablePositions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (positionIsEmpty(i, j)) {
                    availablePositions.add(new int[]{i, j});
                }
            }
        }
        return availablePositions;
    }


    public void computerProcess(){

    }
}