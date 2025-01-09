package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.game.ed_p1_grupo_01.R;
import com.game.ed_p1_grupo_01.modelo.GameTable;
import com.game.ed_p1_grupo_01.modelo.Token;

import java.util.ArrayList;

/**
 * Activity para manejar la lógica y la interfaz del tablero de juego.
 */
public class GameBoardActivity extends AppCompatActivity {

    private Button[][] boardButtons; // Botones del tablero 3x3
    private GameTable gameTable; // Modelo lógico del tablero
    private TextView tvTurnIndicator; // Indicador de turno
    private String gameMode; // Modo de juego (PLAYER_VS_PC, PLAYER_VS_PLAYER, PC_VS_PC)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // Inicializar componentes de la interfaz
        initUIComponents();

        // Inicializar lógica del tablero
        gameTable = new GameTable();

        // Obtener configuraciones del Intent
        gameMode = getIntent().getStringExtra("MODE");
        String startingPlayer = getIntent().getStringExtra("STARTING_PLAYER");
        String symbol = getIntent().getStringExtra("SYMBOL");

        // Configurar quién inicia el juego
        boolean player1Starts = "PLAYER_1".equals(startingPlayer);
        configureStartingPlayer(player1Starts, symbol);

        // Configurar listeners para los botones del tablero
        setUpBoardListeners();
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     */
    private void initUIComponents() {
        tvTurnIndicator = findViewById(R.id.tv_turn_indicator);
        ImageButton btnBack = findViewById(R.id.btn_back);
        ImageButton btnViewTrees = findViewById(R.id.btn_view_trees);
        Button btnRestartGame = findViewById(R.id.btn_restart_game);

        // Configurar el botón de regreso
        btnBack.setOnClickListener(view -> finish());

        // Botón para visualizar los árboles (implementación futura)
        btnViewTrees.setOnClickListener(view -> {
            Intent intent = new Intent(GameBoardActivity.this, TreeVisualizationActivity.class);
            startActivity(intent);
        });

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
    private void setUpBoardListeners() {
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

        // Lógica para los modos de juego
        if (gameMode.equals("PLAYER_VS_PC") && !gameTable.isPlayer1Turn()) {
            gameTable.computerProcess();
            updateBoardFromLogic();
        } else if (gameMode.equals("PC_VS_PC")) {
            while (!gameTable.isPlayer1Turn() && !gameTable.gameIsEnd()) {
                gameTable.computerProcess();
                updateBoardFromLogic();
            }
        }

        // Actualizar el indicador de turno
        tvTurnIndicator.setText(gameTable.isPlayer1Turn() ? "Turno: Jugador 1" : "Turno: Jugador 2");
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
        gameTable = new GameTable();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardButtons[i][j].setText("");
                boardButtons[i][j].setEnabled(true);
            }
        }
        tvTurnIndicator.setText("Turno: Jugador 1");
    }

    /**
     * Maneja el final del juego.
     */
    private void endGame() {
        Intent intent = new Intent(GameBoardActivity.this, ResultActivity.class);
        intent.putExtra("WINNER", gameTable.isPlayer1Turn() ? "Jugador 1" : "Jugador 2");
        startActivity(intent);
        finish();
    }
}
