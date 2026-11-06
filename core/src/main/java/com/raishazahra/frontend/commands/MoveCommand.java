package com.raishazahra.frontend.commands;

import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.systems.BulletManager;

public class MoveCommand implements Command {

    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setDirection(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player == null) return;

        player.move(dx, dy);
        player.updateAnimationState(dx);
    }
}
