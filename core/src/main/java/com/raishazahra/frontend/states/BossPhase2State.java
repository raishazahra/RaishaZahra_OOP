
package com.raishazahra.frontend.states;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.items.ItemType;
import com.raishazahra.frontend.systems.LevelWaveManager;

import java.util.List;
import java.util.function.Supplier;

public class BossPhase2State extends SpellCardState {

    @Override
    protected String getSpellName() {
        return "夜符「ナイトバード」 (Night Sign 'Night Bird')";
    }

    @Override
    protected int getStartingLife() {
        return 6300;
    }

    @Override
    protected float getTimeoutSeconds() {
        return 25f;
    }

    @Override
    protected List<Supplier<Attack>> createAttackBlocks(GameObject player) {
        return List.of(
            () -> RumiaAttacks.nightBirdWings(player)
        );
    }

    @Override
    protected float getGlideDistance() {
        return 120f;
    }

    @Override
    protected float getGlideSeconds() {
        return 2f;
    }

    @Override
    protected void onSpellEnd(
        LevelWaveManager manager,
        Boss boss
    ) {
        if (boss != null) {
            manager.spawnItem(
                boss.getX(),
                boss.getY(),
                ItemType.POWER
            );
        }
    }

    @Override
    protected WaveState createNextState() {
        return new BossPhase3State();
    }
}
