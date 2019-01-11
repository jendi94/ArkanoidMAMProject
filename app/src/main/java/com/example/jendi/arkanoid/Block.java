package com.example.jendi.arkanoid;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static android.graphics.Color.*;

class Block {
    private int posX, posY, value, width, height;
    private Rect rect;
    private Paint paint;
    private int[] colors = {BLUE, RED, GREEN, YELLOW, MAGENTA, CYAN};

    Block(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.width = 108;
        this.height = 40;
        this.value = 1;
        this.rect = new Rect(posX - width/2, posY - height/2,
                posX + width/2, posY + height/2);
        this.paint = new Paint();
        this.paint.setColor(colors[(int)(Math.random()*6)]);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
