package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.game.ed_p1_grupo_01.R;
import com.game.ed_p1_grupo_01.modelo.GameTable;
import com.game.ed_p1_grupo_01.modelo.Token;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity para manejar la lógica y la interfaz del tablero de juego.
 */
public class GameBoardActivity extends AppCompatActivity {
    private Button[][] boardButtons; // Botones del tablero 3x3
    private GameTable gameTable; // Modelo lógico del tablero
    private TextView tvTurnIndicator; // Indicador de turno
    private String gameMode; // Modo de juego (PLAYER_VS_PC, PLAYER_VS_PLAYER, PC_VS_PC)
    private HashMap<GameTable, Integer> utilityTree;
    private boolean player1Starts;
    private String startingPlayer;
    private String symbol;
    ImageButton btnViewTrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // Inicializar lógica del tablero
        gameTable = new GameTable();

        // Obtener configuraciones del Intent
        gameMode = getIntent().getStringExtra("MODE");
        startingPlayer = getIntent().getStringExtra("STARTING_PLAYER");
        symbol = getIntent().getStringExtra("SYMBOL");

        // Configurar quién inicia el juego
        player1Starts = "PLAYER_1".equalsIgnoreCase(startingPlayer);

        // Inicializar componentes de la interfaz
        initUIComponents();

        //Configurar jugador
        configureStartingPlayer(player1Starts, symbol);

        // Configurar listeners para los botones del tablero
        setUpBoardListeners(player1Starts);
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     */
    private void initUIComponents() {
        tvTurnIndicator = findViewById(R.id.tv_turn_indicator);
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnViewTrees = findViewById(R.id.btn_view_trees);
        Button btnRestartGame = findViewById(R.id.btn_restart_game);

        // Configurar el botón de regreso
        btnBack.setOnClickListener(view -> finish());

        // Botón para visualizar los árboles (implementación futura)
        btnViewTrees.setOnClickListener(view -> {
            Intent intent = new Intent(GameBoardActivity.this, TreeVisualizationActivity.class);
            intent.putExtra("table", gameTable);
            intent.putExtra("utility", utilityTree);
            startActivity(intent);
        });

        if(gameMode.equals("PLAYER_VS_PLAYER")){
            btnViewTrees.setVisibility(View.GONE);
        }
        Log.i("parametros", "onResume: " + player1Starts);
        Log.i("parametros", "onResume: " + gameMode.equals("PLAYER_VS_PC"));
        if(player1Starts && gameMode.equals("PLAYER_VS_PC")){
            btnViewTrees.setVisibility(View.GONE);
        }

        // Botón para reiniciar el juego
        btnRestartGame.setOnClickListener(view -> restartGame());

        // Inicializar matriz de botones del tablero
        boardButtons = new Button[3][3];
        boardButtons[0][0] = findViewById(R.id.btn_cell_00);
        boardButtons[0][1] = findViewById(R.id.btn_cell_01);
        boardButtons[0][2] = findViewById(R.id.btn_cell_02);
        boardButtons[1][0] = findViewById(R.id.btn_cell_10);
        boardButtons[1][1] = findViewById(R.id.btn_cell_11);
        boardButtons[1][2] = findViewById(R.id.btn_cell_12);
        boardButtons[2][0] = findViewById(R.id.btn_cell_20);
        boardButtons[2][1] = findViewById(R.id.btn_cell_21);
        boardButtons[2][2] = findViewById(R.id.btn_cell_22);
    }

    /**
     * Configura el jugador inicial y sus símbolos.
     *
     * @param player1Starts Indica si el jugador 1 empieza.
     * @param symbol        Símbolo del jugador 1 ("X" o "O").
     */
    private void configureStartingPlayer(boolean player1Starts, String symbol) {
        gameTable.setTokens(new ArrayList<>()); // Reinicia el tablero
        gameTable.setGameTree(null); // Reinicia el árbol de decisiones

        // Configurar símbolos dinámicos según la configuración recibida
        if ("X".equals(symbol)) {
            gameTable.setPlayer1Symbol("X");
            gameTable.setPlayer2Symbol("O");
        } else {
            gameTable.setPlayer1Symbol("O");
            gameTable.setPlayer2Symbol("X");
        }

        // Actualizar el indicador de turno
        tvTurnIndicator.setText(player1Starts ? "Turno: Jugador 1 (" + gameTable.getPlayer1Symbol() + ")" : "Turno: Jugador 2 (" + gameTable.getPlayer2Symbol() + ")");
    }

    /**
     * Configura los listeners para los botones del tablero.
     */
    private void setUpBoardListeners(boolean player1Start) {
        if(!player1Start && gameMode.equals("PLAYER_VS_PC")){
            utilityTree = gameTable.computerProcess(true);
            updateBoardFromLogic();
            gameTable.player1Start=false;//Cambia el valor por default de player1Start para que el metodo de player1turn cambie su return
        }
        //Lo manejo fuera del setOnClickListener, porque el usuario no deberia hacer clic en nada
        if (gameMode.equals("PC_VS_PC")) {
            new Thread(() -> {
                while (!gameTable.gameIsEnd()) {
                    runOnUiThread(() -> {
                        if (gameTable.isPlayer1Turn()) {
                            tvTurnIndicator.setText("Turno: PC 1");
                            utilityTree = gameTable.computerProcess(true); // Como envio true al metodo, la compu asume que ella es la player1
                            updateBoardFromLogic();

                            if (gameTable.gameIsEnd()) {
                                endGame();
                            }

                        } else {
                            tvTurnIndicator.setText("Turno: PC 2");
                            utilityTree = gameTable.computerProcess(false); //Al enviar false la computadora sabe que es la player2
                            updateBoardFromLogic();

                            if (gameTable.gameIsEnd()) {//Comprueba si se acabo el juego
                                endGame();
                            }
                        }
                    });

                    try {//Hago esto para que el juego no se acabe automaticamente, sino que haya un tiempo entre cada movimiento
                        Thread.sleep(2000); //En este caso 2segundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int x = i;
                final int y = j;

                boardButtons[x][y].setOnClickListener(view -> handleCellClick(x, y));
            }
        }
    }

    /**
     * Maneja el evento de clic en una celda del tablero.
     *
     * @param x Coordenada X de la celda.
     * @param y Coordenada Y de la celda.
     */
    private void handleCellClick(int x, int y) {
        boolean isPlayer1Turn = gameTable.isPlayer1Turn();

        // Intentar colocar la ficha en la celda seleccionada
        if (!gameTable.setToken(new Token(isPlayer1Turn, x, y))) {
            return; // Celda ocupada, no hacer nada
        }

        // Actualizar el botón de la celda con el símbolo correspondiente
        boardButtons[x][y].setText(isPlayer1Turn ? gameTable.getPlayer1Symbol() : gameTable.getPlayer2Symbol());
        boardButtons[x][y].setEnabled(false);

        // Verificar si el juego terminó
        if (gameTable.gameIsEnd()) {
            endGame();
            return;
        }

        // Lógica para el cambio de texto del turno
        if (gameMode.equals("PLAYER_VS_PLAYER")) {
            // Modo PLAYER_VS_PLAYER: cambiar entre Jugador 1 y Jugador 2
            if(player1Starts){
                tvTurnIndicator.setText(isPlayer1Turn ? "Turno: Jugador 2" : "Turno: Jugador 1");
            }else{
                tvTurnIndicator.setText(isPlayer1Turn ? "Turno: Jugador 1" : "Turno: Jugador 2");
            }
        } else if (gameMode.equals("PLAYER_VS_PC")) {
            // Modo PLAYER_VS_PC: indicar el turno de la computadora
            tvTurnIndicator.setText("Turno: Computador");

            // Añadir un retraso antes de que la computadora juegue
            new android.os.Handler().postDelayed(() -> {
                // Computadora realiza su movimiento
                if (player1Starts) {
                    utilityTree = gameTable.computerProcess(false); // Computador juega como jugador 2
                } else {
                    utilityTree = gameTable.computerProcess(true); // Computador juega como jugador 1
                }
                updateBoardFromLogic();

                //Visualizar el botón del árbol
                btnViewTrees.setVisibility(View.VISIBLE);

                // Verificar si el juego terminó después del turno de la computadora
                if (gameTable.gameIsEnd()) {
                    endGame();
                } else {
                    // Actualizar el texto del turno al jugador
                    tvTurnIndicator.setText("Turno: Jugador");
                }
            }, 1000); // Retraso de 2 segundos
        }
    }




    /**
     * Actualiza la interfaz del tablero desde el modelo lógico.
     */
    private void updateBoardFromLogic() {
        for (Token token : gameTable.getTokens()) {
            int x = token.getPositionX();
            int y = token.getPositionY();
            boardButtons[x][y].setText(token.isPlayer1() ? gameTable.getPlayer1Symbol() : gameTable.getPlayer2Symbol());
            boardButtons[x][y].setEnabled(false);
        }
    }

    /**
     * Reinicia el estado del juego y limpia la interfaz.
     */
    private void restartGame() {
        Intent intent = new Intent(GameBoardActivity.this, GameBoardActivity.class);
        intent.putExtra("MODE", gameMode);
        intent.putExtra("SYMBOL", symbol);
        intent.putExtra("STARTING_PLAYER", startingPlayer);
        startActivity(intent);
        finish();
    }

    /**
     * Maneja el final del juego.
     */
    private void endGame() {
        String winnerText;

        // Obtener las fichas ganadoras de cada jugador
        ArrayList<Token> firstPlayerTokens = GameTable.isGameWin(gameTable.getPlayer1Tokens());
        ArrayList<Token> secondPlayerTokens = GameTable.isGameWin(gameTable.getPlayer2Tokens());

        // Empate
        if (firstPlayerTokens == null && secondPlayerTokens == null) {
            winnerText = "DRAW";
        } else if (firstPlayerTokens != null && secondPlayerTokens == null) {
            // Gana el jugador 1
            if (gameMode.equals("PLAYER_VS_PC")) {
                winnerText = player1Starts ? "Jugador" : "Computadora";
            } else if (gameMode.equals("PC_VS_PC")) {
                winnerText = player1Starts ? "Computadora 1" : "Computadora 2";
            } else {
                winnerText = player1Starts ? "Jugador 1" : "Jugador 2";
            }
        } else if (firstPlayerTokens == null && secondPlayerTokens != null) {
            // Gana el jugador 2
            if (gameMode.equals("PLAYER_VS_PC")) {
                winnerText = player1Starts ? "Computadora" : "Jugador";
            } else if (gameMode.equals("PC_VS_PC")) {
                winnerText = player1Starts ? "Computadora 2" : "Computadora 1";
            } else {
                winnerText = player1Starts ? "Jugador 2" : "Jugador 1";
            }
        } else {
            // Esto no debería ocurrir, pero por seguridad se maneja.
            winnerText = "Error: Estado inesperado";
        }

        // Log para verificar el resultado
        Log.i("Game End", "Ganador: " + winnerText);

        // Crear el intent para pasar el resultado al siguiente Activity
        Intent intent = new Intent(GameBoardActivity.this, ResultActivity.class);
        intent.putExtra("WINNER", winnerText); // Pasar el texto del ganador
        intent.putExtra("MODE", gameMode);
        intent.putExtra("SYMBOL", symbol);
        intent.putExtra("STARTING_PLAYER", startingPlayer);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }


}
