package com.raishazahra.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.raishazahra.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    private int keyUp = Input.Keys.W;
    private int keyDown = Input.Keys.S;
    private int keyLeft = Input.Keys.A;
    private int keyRight = Input.Keys.D;

    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    private final MoveCommand moveCommand = new MoveCommand(0, 0);
    private final FocusCommand focusOn = new FocusCommand(true);
    private final FocusCommand focusOff = new FocusCommand(false);
    private final ShootCommand shootCommand = new ShootCommand();

    private final List<Command> activeCommands = new ArrayList<>();
    private final BombCommand bombCommand;

    public InputHandler(List<GameObject> entities) {
        bombCommand = new BombCommand(entities);
    }

    public List<Command> handleInput(float delta) {
        activeCommands.clear();

        if (Gdx.input == null) {
            return activeCommands;
        }

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(keyLeft)
            || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 1;
        }

        if (Gdx.input.isKeyPressed(keyRight)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 1;
        }

        if (Gdx.input.isKeyPressed(keyUp)
            || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 1;
        }

        if (Gdx.input.isKeyPressed(keyDown)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 1;
        }

        if (dx != 0 || dy != 0) {
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        moveCommand.setDirection(dx, dy);
        activeCommands.add(moveCommand);

        if (Gdx.input.isKeyPressed(keyFocus)) {
            activeCommands.add(focusOn);
        } else {
            activeCommands.add(focusOff);
        }

        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }

    public int getKeyShoot() {
        return keyShoot;
    }

    public void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }

    public int getKeyBomb() {
        return keyBomb;
    }

    public void setKeyBomb(int keyBomb) {
        this.keyBomb = keyBomb;
    }

    public int getKeyFocus() {
        return keyFocus;
    }

    public void setKeyFocus(int keyFocus) {
        this.keyFocus = keyFocus;
    }
}
