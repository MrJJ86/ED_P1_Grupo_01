package com.game.ed_p1_grupo_01.modelo.operations;

import android.widget.Button;
import android.widget.GridLayout;

import com.game.ed_p1_grupo_01.modelo.Token;

public class TableOperation {
    public static final int[][][] POSITIONS ={
            //Horizontal
            {{0,0},{0,1},{0,2}},
            {{1,0},{1,1},{1,2}},
            {{2,0},{2,1},{2,2}},
            //Vertical
            {{0,0},{1,0},{2,0}},
            {{0,1},{1,1},{2,1}},
            {{0,2},{1,2},{2,2}},
            //Diagonal
            {{0,0},{1,1},{2,2}},
            {{0,2},{1,1},{2,0}}
    };

    private boolean lineajugable(GridLayout table,int[][] comb, Token ficha){

        for(int i=0;i< comb.length;i++){
            int fila = comb[i][0];
            int columna = comb[i][1];
            int linealIndex = fila * 3 + columna;
            Button boton= (Button) table.getChildAt(linealIndex);
            Token tok=(Token) boton.getTag();

            if(!(tok==null) && !tok.equals(ficha) ){
                return false;
            }
        }
        return true;
    }

    public int p(GridLayout table, Token ficha) {
        int p = 0;

        for (int i = 0; i < POSITIONS.length; i++) {
                int[][] comb = POSITIONS[i];
                if (lineajugable(table,comb, ficha)) {
                    p++;
                }
        }

        return p;
    }

    public int utilidad(int pJugador,int pOponente){
    //Recibe como parametros los resultados de aplicar la funcion p al tablero actual, con la fincha del jugador 1 y jugador 2
    return pJugador-pOponente;
    }




}
