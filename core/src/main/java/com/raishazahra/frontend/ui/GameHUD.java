package com.raishazahra.frontend.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.raishazahra.frontend.observers.GameObserver;

public class GameHUD implements GameObserver {

    private long score;
    private int hp;
    private int power;
    private int spellCards;
    private int graze;
    private int lives;

    private final BitmapFont font;

    public GameHUD() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    @Override
    public void onScoreChanged(long score) {
        this.score = score;
    }

    @Override
    public void onHpChanged(int hp) {
        this.hp = hp;
    }

    @Override
    public void onPowerChanged(int power) {
        this.power = power;
    }

    @Override
    public void onSpellCardsChanged(int spellCards) {
        this.spellCards = spellCards;
    }

    @Override
    public void onGrazeChanged(int graze) {
        this.graze = graze;
    }

    @Override
    public void onLivesChanged(int lives) {
        this.lives = lives;
    }

    public void render(SpriteBatch batch) {
        if (batch == null) return;

        font.draw(batch, "SCORE: " + score, 450, 550);
        font.draw(batch, "HP: " + hp, 450, 510);
        font.draw(batch, "POWER: " + power, 450, 480);
        font.draw(batch, "SPELL CARDS: " + spellCards, 450, 450);
        font.draw(batch, "GRAZE: " + graze, 450, 420);
        font.draw(batch, "LIVES: " + lives, 450, 390);

        font.draw(batch, "CONTROLS", 450, 300);
        font.draw(batch, "WASD / Arrows: Move", 450, 270);
        font.draw(batch, "SHIFT: Focus", 450, 240);
        font.draw(batch, "Z: Shoot", 450, 210);
        font.draw(batch, "X: Bomb", 450, 180);
    }

    public void dispose() {
        font.dispose();
    }
}
