package com.example.jendi.arkanoid;

import android.graphics.Bitmap;
import android.graphics.Rect;

class Player {
    private Bitmap bitmap;
    private Rect rect;
    private int posX, posY, score, lives;

    Player(int x, int y, Bitmap bitmap) {
        this.posX = x;
        this.posY = y;
        this.score = 0;
        this.rect = new Rect(posX - 100, posY - 10, posX + 100, posY + 10);
        this.bitmap = bitmap;
        this.lives = 3;
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

    private void refreshRect() {
        this.rect = new Rect(posX - 100, posY - 10, posX + 100, posY + 10);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
