package com.raishazahra.frontend.commands;

import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null && bulletManager != null) {
            player.shootBullet(bulletManager);
        }
    }
}
