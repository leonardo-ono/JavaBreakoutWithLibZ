package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import java.util.Arrays;
import me.winspeednl.libz.core.GameCore;

/**
 * Brick class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Brick extends BreakoutEntity {
    
    private boolean destroyed;
    private int color;
    
    public Brick(GameCore gc, BreakoutGame game, int x, int y) {
        super(gc, game);
        this.w = 30;
        this.h = 10;
        this.x = x * 35 + 25;
        this.y = y * 15 + 40;
        this.pixels = new int[w * h];
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        Arrays.fill(pixels, color);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    public void updateTitle() {
        updateInternal();
    }

    @Override
    public void updatePlaying() {
        updateInternal();
    }
    
    private void updateInternal() {
        Ball ball = game.getBall();
        if (collides(ball.x, ball.y, ball.w, ball.h)) {
            game.incrementScore();
            if(ball.y <= y - (h * 0.5) || ball.y >= y + (h * 0.5)) {
                ball.invertYDir();
            }
            else if(ball.x < x || ball.x > x) {
                ball.invertXDir();
            }
            destroyed = true;
        }
    }
    
    // received broadcast messages
    
    @Override
    public void stateChanged() {
        visible = game.getState() != INIT;
    }
    
}
