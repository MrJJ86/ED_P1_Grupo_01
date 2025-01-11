package com.game.ed_p1_grupo_01.modelo;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    public ArrayList<Token> getPlayer1Tokens(){
        ArrayList<Token> tokens = new ArrayList<>();
        for(Token t: getTokens()){
            if(t.isPlayer1()){
                tokens.add(t);
            }
        }
        return tokens;
    }

    public ArrayList<Token> getPlayer2Tokens(){
        ArrayList<Token> tokens = new ArrayList<>();
        for(Token t: getTokens()){
            if(!t.isPlayer1()){
                tokens.add(t);
            }
        }
        return tokens;
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
     * @param playerTokens Lista de tokens del jugador a evaluar.
     * @return El número de líneas jugables para el jugador.
     */
    public int calculateP(ArrayList<Token> playerTokens) {
        int p = 0;

        for (int[][] combination : POSITIONS) {
            boolean isPlayable = true;
            int tokenCapacity = 0;

            for (int[] position : combination) {
                int x = position[0];
                int y = position[1];
                Token existingToken = getTokenAtPosition(x, y);

                if (existingToken != null) {
                    List<Token> equalTokens =  playerTokens.stream()
                            .filter((token)->{
                                return existingToken.equals(token);
                            })
                            .toList();
                    if(equalTokens.isEmpty()){
                        isPlayable = false;
                        break;
                    }
                    if(equalTokens.size() == 1){
                        tokenCapacity++;
                    }
                }
            }
            if(tokenCapacity == 3){
                isPlayable = false;
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
     * @param playerTokens El token del jugador para quien se calcula la utilidad.
     * @param opponentTokens El token del oponente.
     * @return La utilidad del tablero.
     */
    public int calculateU(ArrayList<Token> playerTokens, ArrayList<Token> opponentTokens) {
        int pPlayer = calculateP(playerTokens);
        int pOpponent = calculateP(opponentTokens);
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

    public Map<GameTable, Integer> computerProcess(boolean isComputerFirst) {
        gameTree = Tree.TreeTable(this.clone());
        Map<GameTable, Integer> firstChildrenUtilityMap = new HashMap<>();
        Map<GameTable, Integer> treeUtilityMap = new HashMap<>();

        for(Tree<GameTable> treeTable: gameTree.getRoot().getChildren()){
            int tableUtility = Integer.MAX_VALUE;
            for(Tree<GameTable> childTreeTable: treeTable.getRoot().getChildren()){
                GameTable table = childTreeTable.getRoot().getContent();

                ArrayList<Token> computer;
                ArrayList<Token> player;
                int currentUtility;

                if(isComputerFirst){
                    computer = table.getPlayer1Tokens();
                    player = table.getPlayer2Tokens();
                    currentUtility = table.calculateU(computer, player);
                }else{
                    computer = table.getPlayer2Tokens();
                    player = table.getPlayer1Tokens();
                    currentUtility = table.calculateU(player, computer);
                }
                treeUtilityMap.put(table, currentUtility);
                tableUtility = Math.min(currentUtility, tableUtility);

            }
            firstChildrenUtilityMap.put(treeTable.getRoot().getContent(),tableUtility);
        }
        treeUtilityMap.putAll(firstChildrenUtilityMap);

        //Obtener la mayor utilidad
        Iterator<GameTable> iteratorKey = firstChildrenUtilityMap.keySet().iterator();
        int maxUtility = Integer.MIN_VALUE;
        while(iteratorKey.hasNext()){
            int utility = firstChildrenUtilityMap.get(iteratorKey.next());
            maxUtility = Math.max(maxUtility, utility);
        }
        //Colocar token con el tablero de mejor utilidad

        for (GameTable table : firstChildrenUtilityMap.keySet()) {
            int utility = firstChildrenUtilityMap.get(table);
            if(utility == maxUtility){
                //Siguiente Token que depende del turno del computador
                Token nextToken;
                if(isComputerFirst){
                    nextToken = table.getPlayer1Tokens().get(table.getPlayer1Tokens().size()-1);
                }else{
                    nextToken = table.getPlayer2Tokens().get(table.getPlayer2Tokens().size()-1);
                }
                this.setToken(nextToken);
                break;
            }
        }
        return treeUtilityMap;
    }

    @Override
    public String toString() {
        return "GameTable{" +
                "gameTree=" + gameTree +
                ", tokens=" + tokens +
                '}';
    }
}
