package com.raishazahra.frontend.commands;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements Command {

    private final List<GameObject> entities;

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null && bulletManager != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
