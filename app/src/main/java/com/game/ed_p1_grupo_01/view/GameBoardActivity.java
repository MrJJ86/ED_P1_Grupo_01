package com.game.ed_p1_grupo_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.game.ed_p1_grupo_01.R;

public class GameBoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_game_board), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        goToTreeVisualization();
    }

    private void goToTreeVisualization(){
        ImageButton button = findViewById(R.id.btn_view_trees);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, TreeVisualizationActivity.class);
            startActivity(intent);
        });
    }
}
