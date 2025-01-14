package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.game.ed_p1_grupo_01.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener referencias a los botones
        Button btnPlayerVsPc = findViewById(R.id.btn_player_vs_pc);
        Button btnPlayerVsPlayer = findViewById(R.id.btn_player_vs_player);
        Button btnPcVsPc = findViewById(R.id.btn_pc_vs_pc);

        // Configurar listeners para los botones
        btnPlayerVsPc.setOnClickListener(view -> navigateToConfig("PLAYER_VS_PC"));
        btnPlayerVsPlayer.setOnClickListener(view -> navigateToConfig("PLAYER_VS_PLAYER"));
        btnPcVsPc.setOnClickListener(view -> navigateToConfig("PC_VS_PC"));
    }

    /**
     * Navega a la configuración con el modo de juego seleccionado.
     *
     * @param mode El modo de juego seleccionado (PLAYER_VS_PC, PLAYER_VS_PLAYER, PC_VS_PC).
     */
    private void navigateToConfig(String mode) {
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }
}
