package com.raishazahra.frontend.observers;

public interface GameObserver {

    void onScoreChanged(long score);

    void onHpChanged(int hp);

    void onPowerChanged(int power);

    void onSpellCardsChanged(int spellCards);

    void onGrazeChanged(int graze);

    void onLivesChanged(int lives);
}
