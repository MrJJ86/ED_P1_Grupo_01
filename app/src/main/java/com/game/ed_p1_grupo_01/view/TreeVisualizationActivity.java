package com.game.ed_p1_grupo_01.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.game.ed_p1_grupo_01.R;

public class TreeVisualizationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tree_visualization);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_tree_visualization), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createTreeGame();
    }

    private void createTreeGame(){
        HorizontalScrollView scroll = findViewById(R.id.h_scroll);
        LinearLayout linearLayout = new LinearLayout(scroll.getContext());
        scroll.addView(linearLayout);

        for(int i = 0; i < 5; i++){
            View view = new View(linearLayout.getContext());
            view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
            Dialog dialog = new Dialog(view.getContext());
            dialog.setContentView(R.layout.game_table_template);
            //dialog.setCancelable(false);
            dialog.show();

            linearLayout.addView(view);
        }

    }

    private void createDialog(){

    }
}
