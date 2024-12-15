package com.game.ed_p1_grupo_01.elements;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Token implements Serializable, Comparable<Token> {
    private boolean isPlayer1;
    private int positionX;
    private int positionY;

    public Token(boolean isPlayer1, int positionX, int positionY){
        this.isPlayer1 = isPlayer1;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public void setPlayer1(boolean player1) {
        isPlayer1 = player1;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getPositions(){
        return String.valueOf(this.positionX) + String.valueOf(this.positionY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return isPlayer1 == token.isPlayer1 && positionX == token.positionX && positionY == token.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPlayer1, positionX, positionY);
    }

    @Override
    public int compareTo(Token o) {
        int compPosX = this.positionX - o.positionX;
        if(compPosX != 0) return compPosX;
        return this.positionY - o.positionY;
    }
}
