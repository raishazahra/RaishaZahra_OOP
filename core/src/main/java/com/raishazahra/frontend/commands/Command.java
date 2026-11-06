package com.raishazahra.frontend.commands;

import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}
