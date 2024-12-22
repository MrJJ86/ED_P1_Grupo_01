package com.game.ed_p1_grupo_01.operations;

public class TableOperation {
    public static final int[][][] POSITIONS ={
            //Horizontal
            {{0,0},{0,1},{0,2}},
            {{1,0},{1,1},{1,2}},
            {{2,0},{2,1},{2,2}},
            //Vertical
            {{0,1},{1,0},{2,0}},
            {{0,1},{1,1},{2,1}},
            {{0,2},{1,2},{2,2}},
            //Diagonal
            {{0,0},{1,1},{2,2}},
            {{0,2},{1,1},{2,0}}
    };
}
