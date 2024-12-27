package com.game.ed_p1_grupo_01.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameTable implements Serializable {
    private Tree<GameTable> gameTree;
    private ArrayList<Token> tokens;

    public static final int[][][] POSITIONS = {
            // Horizontal
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            // Vertical
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            // Diagonal
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}}
    };

    public GameTable() {
        this.gameTree = new Tree<>(this);
        this.tokens = new ArrayList<>();
    }

    public GameTable(ArrayList<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        this.gameTree = new Tree<>(this);
    }

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

    private boolean positionIsEmpty(int positionX, int positionY) {
        for (Token token : tokens) {
            if (token.getPositionX() == positionX && token.getPositionY() == positionY) {
                return false;
            }
        }
        return true;
    }

    public boolean setToken(Token token) {
        if (positionIsEmpty(token.getPositionX(), token.getPositionY())) {
            tokens.add(token);
            return true;
        }
        return false;
    }

    public boolean gameIsEnd() {
        if (tokens.size() >= 5) {
            ArrayList<Token> player1 = new ArrayList<>();
            ArrayList<Token> player2 = new ArrayList<>();

            for (Token token : tokens) {
                if (token.isPlayer1()) player1.add(token);
                else player2.add(token);
            }

            return isGameWin(player1) != null || isGameWin(player2) != null;
        }
        return false;
    }

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

    public boolean isPlayer1Turn() {
        int player1Tokens = 0;
        int player2Tokens = 0;

        for (Token token : tokens) {
            if (token.isPlayer1()) {
                player1Tokens++;
            } else {
                player2Tokens++;
            }
        }

        return player1Tokens <= player2Tokens;
    }

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

    /**
     * Calcula el valor P (líneas jugables) para un jugador dado.
     *
     * @param playerToken El token del jugador a evaluar.
     * @return El número de líneas jugables para el jugador.
     */
    public int calculateP(Token playerToken) {
        int p = 0;

        for (int[][] combination : POSITIONS) {
            boolean isPlayable = true;
            for (int[] position : combination) {
                int x = position[0];
                int y = position[1];
                Token existingToken = getTokenAtPosition(x, y);

                if (existingToken != null && !existingToken.equals(playerToken)) {
                    isPlayable = false;
                    break;
                }
            }
            if (isPlayable) {
                p++;
            }
        }
        return p;
    }

    /**
     * Calcula la utilidad del tablero (U).
     *
     * @param playerToken El token del jugador para quien se calcula la utilidad.
     * @param opponentToken El token del oponente.
     * @return La utilidad del tablero.
     */
    public int calculateU(Token playerToken, Token opponentToken) {
        int pPlayer = calculateP(playerToken);
        int pOpponent = calculateP(opponentToken);
        return pPlayer - pOpponent;
    }

    /**
     * Obtiene el token en una posición específica del tablero.
     *
     * @param x Coordenada X.
     * @param y Coordenada Y.
     * @return El token encontrado en la posición, o null si está vacía.
     */
    private Token getTokenAtPosition(int x, int y) {
        for (Token token : tokens) {
            if (token.getPositionX() == x && token.getPositionY() == y) {
                return token;
            }
        }
        return null;
    }
    public void computerProcess() {
        // Verificar si el juego ya terminó
        if (gameIsEnd()) {
            return; // Si ya terminó, no hace nada
        }

        // Obtener el token de la computadora y del oponente
        boolean isPlayer1 = !isPlayer1Turn(); // La computadora es el jugador actual
        Token computerToken = new Token(isPlayer1, -1, -1);
        Token opponentToken = new Token(!isPlayer1, -1, -1);

        // Variables para guardar el mejor movimiento
        int bestUtility = Integer.MIN_VALUE ;
        int[] bestMove = null;

        // Iterar sobre todas las posiciones disponibles
        for (int[] position : getAvailablePositions()) {
            // Clonar el tablero para simular el movimiento
            GameTable simulatedTable = this.clone();
            Token simulatedToken = new Token(isPlayer1, position[0], position[1]);

            // Realizar el movimiento simulado
            if (simulatedTable.setToken(simulatedToken)) {
                // Calcular la utilidad del tablero resultante
                int utility = simulatedTable.calculateU(computerToken, opponentToken);

                // Verificar si es el mejor movimiento encontrado
                if (utility > bestUtility) {
                    bestUtility = utility;
                    bestMove = position;
                }
            }
        }

        // Realizar el mejor movimiento en el tablero actual
        if (bestMove != null) {
            Token bestToken = new Token(isPlayer1, bestMove[0], bestMove[1]);
            setToken(bestToken);
        }
    }

}
