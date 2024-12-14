package com.game.ed_p1_grupo_01;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean lineajugable(GridLayout table, int[] comb,String ficha){

        for(int i=0;i<comb.length;i++){
            Button boton= (Button) table.getChildAt(comb[i]);
            String cont=boton.getText().toString();

            if(!cont.equals(ficha) && !cont.isEmpty()){
                return false;
            }
        }
        return true;
    }

    public int p(GridLayout table,String ficha) {
        int p = 0;

        int[][] combinaciones = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  //Lineas horizontales
                {0, 4, 8}, {2, 4, 6},  //Lineas diagonales
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}  //Lineas Verticales
        };

        for (int i = 0; i < combinaciones.length; i++) {
            int[] comb = combinaciones[i];
            if (lineajugable(table, comb, ficha)) {
                p++;
            }
        }

        return p;
    }


}