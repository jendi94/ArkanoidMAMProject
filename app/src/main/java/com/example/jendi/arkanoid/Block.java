package com.example.jendi.arkanoid;

import android.graphics.Bitmap;
import android.graphics.Rect;

class Block {
    private int posX, posY, value, width, height, lives;
    private Rect rect;
    private Bitmap bitmap;

    Block(int x, int y, int lives, Bitmap bitmap) {
        this.posX = x;
        this.posY = y;
        this.width = 108;
        this.height = 40;
        this.value = 1;
        this.lives = lives;
        this.bitmap = bitmap;
        this.rect = new Rect(posX - width/2, posY - height/2,
                posX + width/2, posY + height/2);
    }

    public void tierDown() {
        int index = GameView.blockTierList.indexOf(this.bitmap);
        this.bitmap = GameView.blockTierList.get(--index);
        this.lives -= 1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rect getRect() {
        return rect;
    }

    public int getLives() {
        return lives;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
