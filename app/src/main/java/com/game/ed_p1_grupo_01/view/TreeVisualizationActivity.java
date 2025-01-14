package com.game.ed_p1_grupo_01.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.game.ed_p1_grupo_01.R;
import com.game.ed_p1_grupo_01.modelo.GameTable;
import com.game.ed_p1_grupo_01.modelo.Token;
import com.game.ed_p1_grupo_01.modelo.Tree;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeVisualizationActivity extends AppCompatActivity {

    //TODO: Eliminar este variable cuando se tenga el tablero actual correctamente
    boolean isComputerFirst;
    GameTable table;
    HashMap<GameTable, Integer> utilityTree;

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
        ImageButton returnButton = findViewById(R.id.btn_back_to_game);
        returnButton.setOnClickListener(v -> {
            this.finish();;
        });

        isComputerFirst = true;
        table = getIntent().getExtras().getSerializable("table", GameTable.class);
        Log.i("Table", "onCreate: " + table);
        utilityTree = (HashMap<GameTable, Integer>) getIntent().getExtras().getSerializable("utility", HashMap.class);
        Log.i("Table", "onCreate: " + utilityTree);
        generateTreeTable();


    }

    private LinearLayout createTableTemplate(){

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setPadding(5,5,5,5);
        container.setBackgroundColor(getColor(R.color.md_theme_onBackground));
        container.setDividerDrawable(AppCompatResources.getDrawable(container.getContext(), R.drawable.spacer_small));
        container.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        for(int i = 1; i < 4; i++){
            LinearLayout row = new LinearLayout(container.getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100));
            row.setDividerDrawable(AppCompatResources.getDrawable(container.getContext(), R.drawable.spacer_small));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            CardView card1 = new CardView(row.getContext());
            card1.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
            card1.setCardBackgroundColor(getColor(R.color.md_theme_background));
            card1.setTag(i*3);
            CardView card2 = new CardView(row.getContext());
            card2.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));;
            card2.setCardBackgroundColor(getColor(R.color.md_theme_background));
            card2.setTag(i*5);
            CardView card3 = new CardView(row.getContext());
            card3.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
            card3.setCardBackgroundColor(getColor(R.color.md_theme_background));
            card3.setTag(i*7);
            row.addView(card1);
            row.addView(card2);
            row.addView(card3);
            container.addView(row);

        }
        TextView utilityText = new TextView(this);
        utilityText.setTag("Utility");
        utilityText.setTextSize(2,20);
        utilityText.setTextColor(getColor(R.color.md_theme_background));
        utilityText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        utilityText.setVisibility(View.GONE);
        container.addView(utilityText);
        container.setId(ViewGroup.generateViewId());
        return container;
    }

    private void generateTreeTable(){

        Tree<GameTable> treeTable = table.getGameTree();

        //Guides
        Guideline guideH10 = findViewById(R.id.horizontal_guide_10);
        Guideline guideH30 = findViewById(R.id.horizontal_guide_30);
        Guideline guideH60 = findViewById(R.id.horizontal_guide_60);
        Guideline guideH90 = findViewById(R.id.horizontal_guide_90);

        //Constraint
        ConstraintLayout layout = findViewById(R.id.constraint_layout_tree);
        ConstraintSet constraintSet = new ConstraintSet();

        //Contenedor del nodo Raíz
        LinearLayout containerRoot = createTableTemplate();
        GameTable rootTable = treeTable.getRoot().getContent();
        Map<String, Token> rootTokenMap = getTokenMap(rootTable.getTokens());
        setImageToken(rootTokenMap, containerRoot);

        // Añadiendo el nodo raíz
        layout.addView(containerRoot);
        constraintSet.clone(layout);
        constraintSet.connect(containerRoot.getId(), ConstraintSet.TOP, guideH10.getId(), ConstraintSet.TOP,0);
        constraintSet.connect(containerRoot.getId(), ConstraintSet.BOTTOM, guideH30.getId(), ConstraintSet.BOTTOM,0);
        constraintSet.connect(containerRoot.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START,0);
        constraintSet.connect(containerRoot.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END,0);
        constraintSet.applyTo(layout);

        // Contenedor para las tablas del primer nivel
        LinearLayout containerFirstLevel = new LinearLayout(this);
        containerFirstLevel.setId(View.generateViewId());
        containerFirstLevel.setOrientation(LinearLayout.HORIZONTAL);
        containerFirstLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        containerFirstLevel.setPadding(5,5,5,5);

        int listChildrenSize = treeTable.getRoot().getChildren().size();
        Log.i("parametros", "generateTreeTable: " + listChildrenSize);
        int[] spacers = {R.drawable.spacer_midium,R.drawable.spacer_large,R.drawable.spacer_large_3,
                        R.drawable.spacer_large_4,R.drawable.spacer_large_5,R.drawable.spacer_large_6,
                        R.drawable.spacer_large_7,R.drawable.spacer_large_8,R.drawable.spacer_extra_larger};
        int divider = listChildrenSize > 0 ? spacers[listChildrenSize-1] : spacers[0];

        containerFirstLevel.setDividerDrawable(AppCompatResources.getDrawable(containerRoot.getContext(), divider));
        containerFirstLevel.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        //Contenedor para los contenedores de las tablas del segundo nivel
        LinearLayout containerSecondLevel = new LinearLayout(this);
        containerSecondLevel.setId(View.generateViewId());
        containerSecondLevel.setOrientation(LinearLayout.HORIZONTAL);
        containerSecondLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        containerSecondLevel.setPadding(5,5,5,5);
        containerSecondLevel.setDividerDrawable(AppCompatResources.getDrawable(containerRoot.getContext(), R.drawable.spacer_large));
        containerSecondLevel.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        // nivel 1
        int whatColor = 0;
        for(Tree<GameTable> firstchildTree: treeTable.getRoot().getChildren()){

            LinearLayout containerFirstChild = createTableTemplate();
            GameTable firstChildTable = firstchildTree.getRoot().getContent();
            Map<String, Token> firstChildTokenMap = getTokenMap(firstChildTable.getTokens());
            setImageToken(firstChildTokenMap, containerFirstChild);
            // UTILIDAD
            TextView firstChildUtility = containerFirstChild.findViewWithTag("Utility");
            int utilityNumber = utilityTree.get(firstChildTable);
            String firstUtility = String.valueOf(utilityNumber == Integer.MAX_VALUE ? "" : utilityNumber);

            firstChildUtility.setText(firstUtility);
            firstChildUtility.setVisibility(View.VISIBLE);

            containerFirstLevel.addView(containerFirstChild);

            // nivel 2
            LinearLayout containerChildForFirstLevel = new LinearLayout(this);
            containerChildForFirstLevel.setOrientation(LinearLayout.HORIZONTAL);
            containerChildForFirstLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            containerChildForFirstLevel.setPadding(20,30,20,30);
            containerChildForFirstLevel.setDividerDrawable(AppCompatResources.getDrawable(containerRoot.getContext(), R.drawable.spacer_midium));
            containerChildForFirstLevel.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            // Color de fondo del containerChildForFirstLevel
            int[] colors = {getColor(R.color.md_theme_primaryFixed),getColor(R.color.md_theme_tertiaryFixed)};
            containerChildForFirstLevel.setBackgroundColor(colors[whatColor]);

            for(Tree<GameTable> secondChildTree: firstchildTree.getRoot().getChildren()){
                LinearLayout containerSecondChild = createTableTemplate();
                GameTable secondChildTable = secondChildTree.getRoot().getContent();
                Map<String, Token> secondChildTokenMap = getTokenMap(secondChildTable.getTokens());
                setImageToken(secondChildTokenMap, containerSecondChild);
                // UTILIDAD
                TextView secondChildUtility = containerSecondChild.findViewWithTag("Utility");
                String secondUtility = String.valueOf(utilityTree.get(secondChildTable));
                secondChildUtility.setText(secondUtility);
                secondChildUtility.setVisibility(View.VISIBLE);

                containerChildForFirstLevel.addView(containerSecondChild);
            }

            containerSecondLevel.addView(containerChildForFirstLevel);
            whatColor++;
            if(whatColor > 1) whatColor = 0;
        }

        // añadiendo el containerFirstLevel al layout
        layout.addView(containerFirstLevel);
        constraintSet.clone(layout);
        constraintSet.connect(containerFirstLevel.getId(), ConstraintSet.TOP, guideH30.getId(), ConstraintSet.TOP,0);
        constraintSet.connect(containerFirstLevel.getId(), ConstraintSet.BOTTOM, guideH60.getId(), ConstraintSet.BOTTOM,0);
        constraintSet.connect(containerFirstLevel.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START,0);
        constraintSet.connect(containerFirstLevel.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END,0);
        constraintSet.applyTo(layout);

        // añadiendo el containerSecondLevel al layout
        layout.addView(containerSecondLevel);
        constraintSet.clone(layout);
        constraintSet.connect(containerSecondLevel.getId(), ConstraintSet.TOP, guideH60.getId(), ConstraintSet.TOP,0);
        constraintSet.connect(containerSecondLevel.getId(), ConstraintSet.BOTTOM, guideH90.getId(), ConstraintSet.BOTTOM,0);
        constraintSet.connect(containerSecondLevel.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START,0);
        constraintSet.connect(containerSecondLevel.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END,0);
        constraintSet.applyTo(layout);

        //Colocando el scroll en el nodo raíz
        containerRoot.setFocusedByDefault(true);
        HorizontalScrollView scroll = findViewById(R.id.h_scroll);
        scroll.requestChildFocus(layout,containerRoot);
    }

    private Map<String, Token> getTokenMap(List<Token> tokens){
        Map<String, Token> tokenMap = new HashMap<>();
        for (Token token : tokens) {
            String key = token.getPositionX() + "," + token.getPositionY();
            tokenMap.put(key, token);
        }
        return tokenMap;
    }

    private void setImageToken(Map<String, Token> tokenMap, LinearLayout container){
        //Map para cada Card en el LinearLayout
        Map<String,CardView> positionsMap = new LinkedHashMap<>();
        positionsMap.put("0,0", container.findViewWithTag(3));
        positionsMap.put("1,0", container.findViewWithTag(6));
        positionsMap.put("2,0", container.findViewWithTag(9));
        positionsMap.put("0,1", container.findViewWithTag(5));
        positionsMap.put("1,1", container.findViewWithTag(10));
        positionsMap.put("2,1", container.findViewWithTag(15));
        positionsMap.put("0,2", container.findViewWithTag(7));
        positionsMap.put("1,2", container.findViewWithTag(14));
        positionsMap.put("2,2", container.findViewWithTag(21));

        // Se agrega la imagen
        for(String keyTMap: tokenMap.keySet()){
            CardView card = positionsMap.get(keyTMap);
            if(card != null){
                Token token = tokenMap.get(keyTMap);
                ImageView image = new ImageView(card.getContext());
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //TODO: Colocar una mejor condicion con los datos de configActivity
                if(token.isPlayer1()){
                    if(table.getPlayer1Symbol().equalsIgnoreCase("X")){
                        image.setImageResource(R.drawable.cross);
                    }else{
                        image.setImageResource(R.drawable.circle);
                    }
                }else{
                    if(table.getPlayer2Symbol().equalsIgnoreCase("X")){
                        image.setImageResource(R.drawable.cross);
                    }else{
                        image.setImageResource(R.drawable.circle);
                    }
                }
                card.addView(image);
            }
        }
    }
}
