package br.ol.breakout;

import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.entities.LibZ_Entity;
import me.winspeednl.libz.screen.Render;

/**
 * BreakoutEntity abstract class.
 *
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public abstract class BreakoutEntity extends LibZ_Entity {
    
    protected GameCore gc;
    protected BreakoutGame game;
    protected int[] pixels;
    protected boolean visible;
    
    public BreakoutEntity(GameCore gc, BreakoutGame game) {
        this.gc = gc;
        this.game = game;
        this.visible = false;
    }
    
    @Override
    public void render(GameCore gc, Render render) {
        if (!visible) {
            return;
        }
        for (int xp = 0; xp < w; xp++) {
            for (int yp = 0; yp < h; yp++) {
                render.setPixel(this.x + xp, this.y + yp, pixels[xp + yp * w]);
            }
        }
    }

    @Override
    public void update(GameCore gc) {
        switch (game.getState()) {
            case INIT: updateInit(); break;
            case TITLE: updateTitle(); break;
            case READY: updateReady(); break;
            case PLAYING: updatePlaying(); break;
            case LEVEL_CLEARED: updateCleared(); break;
            case GAME_OVER: updateGameOver(); break;
            case ENDING: updateEnding(); break;
        }
    }
    
    public void updateInit() {
    }
    
    public void updateTitle() {
    }
    
    public void updateReady() {
    }
    
    public void updatePlaying() {
    }
    
    public void updateCleared() {
    }
    
    public void updateGameOver() {
    }

    public void updateEnding() {
    }
    
    // received broadcast messages
    
    public void reset() {
    }

    public void stateChanged() {
    }
    
}
