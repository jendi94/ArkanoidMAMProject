package com.example.jendi.arkanoid;

import android.graphics.Rect;

class Player {
    private Rect rect;
    private int posX, posY, score;

    Player(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.score = 0;
        this.rect = new Rect(posX - 100, posY - 10, posX + 100, posY + 10);
    }

    public Rect getRect() {
        return rect;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getScore() {
        return score;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        refreshRect();
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    private void refreshRect() {
        this.rect = new Rect(posX - 100, posY - 10, posX + 100, posY + 10);
    }
}
