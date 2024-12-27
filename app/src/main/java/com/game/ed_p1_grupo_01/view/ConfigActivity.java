package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.game.ed_p1_grupo_01.R;

public class ConfigActivity extends AppCompatActivity {

    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Obtener referencias a los elementos de la interfaz
        ImageButton btnBack = findViewById(R.id.btn_back);
        TextView tvConfigTitle = findViewById(R.id.tv_config_title);
        TextView tvPlayerSymbol = findViewById(R.id.tv_player_symbol);
        RadioGroup rgSymbols = findViewById(R.id.rg_symbols);
        RadioButton rbSymbolX = findViewById(R.id.rb_symbol_x);
        RadioButton rbSymbolO = findViewById(R.id.rb_symbol_o);
        TextView tvStartingPlayer = findViewById(R.id.tv_starting_player);
        RadioGroup rgStartingPlayer = findViewById(R.id.rg_starting_player);
        RadioButton rbPlayer1 = findViewById(R.id.rb_player_1);
        RadioButton rbComputer = findViewById(R.id.rb_computer);
        Button btnStartGame = findViewById(R.id.btn_start_game);

        // Recuperar el modo de juego enviado desde MainActivity
        gameMode = getIntent().getStringExtra("MODE");

        // Configurar el título según el modo de juego
        if (gameMode != null) {
            switch (gameMode) {
                case "PLAYER_VS_PC":
                    tvConfigTitle.setText("Jugador vs PC");
                    tvStartingPlayer.setVisibility(View.VISIBLE);
                    rgStartingPlayer.setVisibility(View.VISIBLE);
                    break;
                case "PLAYER_VS_PLAYER":
                    tvConfigTitle.setText("Jugador vs Jugador");
                    tvStartingPlayer.setText("¿Quién inicia?");
                    rbPlayer1.setText("Jugador 1");
                    rbComputer.setText("Jugador 2");
                    rbComputer.setVisibility(View.VISIBLE);
                    break;
                case "PC_VS_PC":
                    tvConfigTitle.setText("PC vs PC");
                    tvStartingPlayer.setText("¿Quién inicia?");
                    rbPlayer1.setText("PC 1");
                    rbComputer.setText("PC 2");
                    rbComputer.setVisibility(View.VISIBLE);
                    break;
            }
        }

        // Configurar botón de regresar
        btnBack.setOnClickListener(view -> finish());

        // Configurar botón para iniciar el juego
        btnStartGame.setOnClickListener(view -> {
            // Obtener configuraciones seleccionadas
            String selectedSymbol = rbSymbolX.isChecked() ? "X" : "O";
            String startingPlayer = rbPlayer1.isChecked() ? "PLAYER_1" : "PLAYER_2";

            // Pasar configuraciones a GameBoardActivity
            Intent intent = new Intent(ConfigActivity.this, GameBoardActivity.class);
            intent.putExtra("MODE", gameMode);
            intent.putExtra("SYMBOL", selectedSymbol);
            intent.putExtra("STARTING_PLAYER", startingPlayer);
            startActivity(intent);
        });
    }
}
