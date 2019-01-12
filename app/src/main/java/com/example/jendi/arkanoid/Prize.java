package com.example.jendi.arkanoid;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Prize {
    private int posX, posY, type;
    private Rect rect;
    private Bitmap bitmap;

    public Prize(int posX, int posY, int type, Bitmap bitmap) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.rect = new Rect(posX - 30, posY - 30, posX + 30, posY + 30);
        this.bitmap = bitmap;
    }

    public void move(){
        this.posY += 5;
        refreshRect();
    }

    private void refreshRect() {
        this.rect = new Rect(posX - 30, posY - 30, posX + 30, posY + 30);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Rect getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
