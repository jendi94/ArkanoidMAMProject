package com.example.jendi.arkanoid;

import android.graphics.Bitmap;
import android.graphics.Rect;

class Ball {
    private Rect rect;
    private int posX, posY, dirX, dirY, speed;
    private Bitmap bitmap;

    Ball(int x, int y, Bitmap bitmap) {
        this.posX = x;
        this.posY = y;
        this.dirX = 1;
        this.dirY = 1;
        this.speed = 10;
        this.rect = new Rect(posX - 10, posY - 10, posX + 10, posY + 10);
        this.bitmap = bitmap;
    }

    public Ball(Ball ball) {
        this.posX = ball.posX;
        this.posY = ball.posY;
        this.dirX = ball.dirX;
        this.dirY = ball.dirY;
        this.speed = ball.speed;
        this.rect = ball.rect;
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

    public int getSpeed() {
        return speed;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        refreshRect();
    }

    public void setPosY(int posY) {
        this.posY = posY;
        refreshRect();
    }

    private void refreshRect() {
        this.rect = new Rect(posX - 10, posY - 10,posX + 10, posY + 10);
    }

    public int getDirX() {
        return dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void reboundVertically() {
        this.dirY*=-1;
    }

    public void reboundHorizontally() {
        this.dirX*=-1;
    }

    public void reset(int x, int y) {
        this.posX = x/2;
        this.posY = y/2;
        this.dirX = 1;
        this.dirY = 1;
        this.speed = 10;
        refreshRect();
    }

    public void moveBall() {
        this.posX += this.dirX * this.speed;
        this.posY += this.dirY * this.speed;
        refreshRect();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
