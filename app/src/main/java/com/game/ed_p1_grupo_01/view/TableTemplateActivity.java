package com.game.ed_p1_grupo_01.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Barrier;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableTemplateActivity extends AppCompatActivity {

    boolean isComputerFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_table_template);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_table_template), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        isComputerFirst = true;
        generateTreeTable();
    }

    private LinearLayout createTableTemplate(){

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setPadding(5,5,5,5);
        container.setBackgroundColor(getColor(R.color.md_theme_onBackground));
        container.setDividerDrawable(AppCompatResources.getDrawable(container.getContext(), R.drawable.spacer_midium));
        container.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        for(int i = 1; i < 4; i++){
            LinearLayout row = new LinearLayout(container.getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100));
            row.setDividerDrawable(AppCompatResources.getDrawable(container.getContext(), R.drawable.spacer_midium));
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
        container.setId(ViewGroup.generateViewId());
        return container;
    }

    private void generateTreeTable(){
        // Reemplazar lineas de prueba
        GameTable table = new GameTable();
        Map<GameTable, Integer> utilityTree = table.computerProcess(isComputerFirst);
        //arbol
        Tree<GameTable> treeTable = table.getGameTree();
        // tags
        LinearLayout container = createTableTemplate();
        //raiz
        GameTable rootTable = treeTable.getRoot().getContent();
        Map<String, Token> rootTokenMap = getTokenMap(rootTable.getTokens());
        setImageToken(rootTokenMap, container);
        //Guides
        Guideline guideH10 = findViewById(R.id.horizontal_guide_10);
        Guideline guideH30 = findViewById(R.id.horizontal_guide_30);
        Guideline guideH60 = findViewById(R.id.horizontal_guide_60);
        Guideline guideH90 = findViewById(R.id.horizontal_guide_90);
        //Constraint
        ConstraintLayout layout = findViewById(R.id.constraint_layout_tree);
        ConstraintSet constraintSet = new ConstraintSet();
        layout.addView(container);
        constraintSet.clone(layout);
        constraintSet.connect(container.getId(), ConstraintSet.TOP, guideH10.getId(), ConstraintSet.TOP,0);
        constraintSet.connect(container.getId(), ConstraintSet.BOTTOM, guideH30.getId(), ConstraintSet.BOTTOM,0);
        constraintSet.connect(container.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START,0);
        constraintSet.connect(container.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END,0);
        constraintSet.applyTo(layout);
        // List of container ID
        ArrayList<Integer> containerChildrenID = new ArrayList<>();
        // nivel 1
        for(Tree<GameTable> firstchildTree: treeTable.getRoot().getChildren()){
            LinearLayout containerFisrtChild = createTableTemplate();
            containerChildrenID.add(containerFisrtChild.getId());
            GameTable childTable = firstchildTree.getRoot().getContent();
            Map<String, Token> childTokenMap = getTokenMap(childTable.getTokens());
            setImageToken(childTokenMap, containerFisrtChild);
            layout.addView(containerFisrtChild);
            constraintSet.clone(layout);
            constraintSet.connect(containerFisrtChild.getId(), ConstraintSet.TOP, guideH30.getId(), ConstraintSet.TOP,0);
            constraintSet.connect(containerFisrtChild.getId(), ConstraintSet.BOTTOM, guideH60.getId(), ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(layout);
        }
        int firstChildID = containerChildrenID.get(0);
        int lastChildID = containerChildrenID.get(containerChildrenID.size()-1);
        for (int i = 0; i < containerChildrenID.size(); i++) {
            int childID = containerChildrenID.get(i);
            if (childID != firstChildID && childID != lastChildID) {
                constraintSet.createBarrier(childID, Barrier.END, 20);
                constraintSet.applyTo(layout);
            }
        }
        int[] chainIDs = new int[containerChildrenID.size()];
        int i = 0;
        for(Integer id: containerChildrenID){
            chainIDs[i++] = id;
        }

        constraintSet.createHorizontalChainRtl(layout.getId(),ConstraintSet.START,layout.getId(),ConstraintSet.END,chainIDs,null,ConstraintSet.CHAIN_SPREAD_INSIDE);
        constraintSet.applyTo(layout);
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
        //Map para cada card en el LinearLayout
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

        for(String keyRTMap: tokenMap.keySet()){
            CardView card = positionsMap.get(keyRTMap);
            if(card != null){
                Token token = tokenMap.get(keyRTMap);
                ImageView image = new ImageView(card.getContext());
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //TODO: Colocar una mejor condicion con los datos de configActivity
                if(token.isPlayer1()){
                    image.setImageResource(R.drawable.download);
                }else{
                    image.setImageResource(R.drawable.round_shape);
                }
                card.addView(image);
            }
        }
    }
}
