package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.game.ed_p1_grupo_01.R;

/**
 * Activity para mostrar el resultado del juego.
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Obtener referencias a los elementos de la interfaz
        TextView tvResultMessage = findViewById(R.id.tv_result_message);
        Button btnMainMenu = findViewById(R.id.btn_main_menu);
        Button btnPlayAgain = findViewById(R.id.btn_play_again);

        // Recuperar el resultado del Intent
        String winner = getIntent().getStringExtra("WINNER");
        String gameMode = getIntent().getStringExtra("MODE");
        String startingPlayer = getIntent().getStringExtra("STARTING_PLAYER");
        String symbol = getIntent().getStringExtra("SYMBOL");

        // Mostrar el mensaje adecuado según el resultado
        if (winner != null) {
            if (winner.equals("DRAW")) {
                tvResultMessage.setText("¡Es un empate!");
            } else {
                tvResultMessage.setText("¡Ganó " + winner + "!");
            }
        }

        // Configurar el botón "Volver al Menú Principal"
        btnMainMenu.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Configurar el botón "Jugar de Nuevo"
        btnPlayAgain.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, GameBoardActivity.class);
            intent.putExtra("MODE", gameMode);
            intent.putExtra("SYMBOL", symbol);
            intent.putExtra("STARTING_PLAYER", startingPlayer);
            startActivity(intent);
            finish();
        });
    }
}
