package com.raishazahra.frontend.objects.patterns.shootingStrategy;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.systems.BulletManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class CompositeShootingPattern implements ShootingPattern {
    private final List<ShootingPattern> patterns = new ArrayList<>();
    public CompositeShootingPattern(ShootingPattern... patterns) {
        if (patterns != null) {
            this.patterns.addAll(Arrays.asList(patterns));
        }
    }
    public void addPattern(ShootingPattern pattern) {
        if (pattern != null) {
            patterns.add(pattern);
        }
    }
    public void removePattern(ShootingPattern pattern) {
        patterns.remove(pattern);
    }
    @Override
    public void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    ) {
        for (ShootingPattern pattern : patterns) {
            if (pattern != null) {
                pattern.execute(
                    originX, originY, bulletManager, isPlayer
                );
            }
        }
    }
}
