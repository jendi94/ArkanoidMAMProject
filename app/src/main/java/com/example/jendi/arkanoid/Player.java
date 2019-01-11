package com.example.jendi.arkanoid;

import android.graphics.Bitmap;
import android.graphics.Rect;

class Player {
    private Bitmap bitmap;
    private Rect rect;
    private int posX, posY, score;

    Player(int x, int y, Bitmap bitmap) {
        this.posX = x;
        this.posY = y;
        this.score = 0;
        this.rect = new Rect(posX - 100, posY - 10, posX + 100, posY + 10);
        this.bitmap = bitmap;
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
}
