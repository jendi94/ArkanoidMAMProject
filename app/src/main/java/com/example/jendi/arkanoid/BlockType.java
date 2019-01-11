package com.example.jendi.arkanoid;

public enum BlockType
{
    RED(3, R.drawable.red_block),
    ORANGE(2, R.drawable.orange_block),
    YELLOW(1, R.drawable.yellow_block);

    private final int lives;
    private final int color;

    BlockType(int lives, int color) {
        this.lives = lives;
        this.color = color;
    }

    public int getLives() {
        return lives;
    }

    public int getColor() {
        return color;
    }
}
