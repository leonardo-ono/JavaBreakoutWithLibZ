package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.image.Sprite;

/**
 * Ball class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ball extends BreakoutEntity {
	
    private boolean xDir = false, yDir = true;
    private double speedX, speedY;

    public Ball(GameCore gc, BreakoutGame game) {
        super(gc, game);
        w = 8;
        h = 8;
        x = gc.getWidth() / 2 - 4;
        y = gc.getHeight() / 2 - 4;
        pixels = new Sprite("/res/Ball.png", 0, 0, w, h).pixels;
        changeAngleRandomically();
    }

    @Override
    public void updateTitle() {
        updateInternal();
    }
    
    @Override
    public void updatePlaying() {
        updateInternal();
        if (y > gc.getHeight() - 10) {
            game.died();
        }
    }
    
    public void updateInternal() {
        x += xDir ? speedX : -speedX;
        y += yDir ? speedY : -speedY;
        xDir = (x <= 0 || x >= gc.getWidth()- w) ? !xDir : xDir;
        yDir = (y <= 0 || y >= gc.getHeight() - h) ? !yDir : yDir;
    }

    public void invertXDir() {
        xDir = !xDir;
    }

    public void invertYDir() {
        yDir = !yDir;
    }
    
    // changes the angle just a little randomically
    public void changeAngleRandomically() {
        double angle = Math.toRadians(45);
        angle += (Math.toRadians(20) * Math.random() - Math.toRadians(10));
        speedX = 4 * Math.cos(angle);
        speedY = 4 * Math.sin(angle);
    }
        
    // received broadcast messages

    @Override
    public void reset() {
        x = gc.getWidth() / 2 - 4;
        y = (int) (gc.getHeight() / 1.75);
        yDir = false;
    }
    
    @Override
    public void stateChanged() {
        visible = game.getState() != INIT 
                && game.getState() != GAME_OVER 
                && game.getState() != ENDING;
    }
    
}
