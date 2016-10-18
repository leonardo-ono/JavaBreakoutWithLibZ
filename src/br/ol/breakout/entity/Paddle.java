package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.INIT;
import java.awt.event.KeyEvent;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.core.Input;
import me.winspeednl.libz.image.Sprite;

/**
 * Paddle class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Paddle extends BreakoutEntity {
    
    private final int moveSpeed;
    
    public Paddle(GameCore gc, BreakoutGame game) {
        super(gc, game);
        this.w = 32;
        this.h = 8;
        this.x = gc.getWidth() / 2 - 16;
        this.y = gc.getHeight() - 30;
        pixels = new Sprite("/res/Paddle.png", 0, 0, w, h).pixels;
        moveSpeed = 3;
    }

    @Override
    public void updateTitle() {
        // play automatically when 'title' state
        x = game.getBall().x - w / 3;
        
        // check level cleared ?
        if (game.isLevelCleared()) {
            game.backToIntro();
        }
        
        updateInternal();
    }

    @Override
    public void updatePlaying() {
        Input input = gc.getInput();
        if (input.isKeyPressed(KeyEvent.VK_A) 
                || input.isKeyPressed(KeyEvent.VK_LEFT)) {
            x -= moveSpeed;
        }
        else if (input.isKeyPressed(KeyEvent.VK_D) 
                || input.isKeyPressed(KeyEvent.VK_RIGHT)) {
            x += moveSpeed;
        }
        // for debuggin purposes
        else if (input.isKeyPressed(KeyEvent.VK_C)) {
            game.destroyOneBrick();
        }
        
        // check level cleared ?
        if (game.isLevelCleared()) {
            game.levelCleared();
        }
        
        updateInternal();
    }

    private void updateInternal() {
        x = x < 0 ? x = 0 : (x > gc.getWidth() - w) ? x = gc.getWidth() - w : x; 

        Ball ball = game.getBall();
        
        if (collides(ball.x, ball.y, ball.w, ball.h)) {
            if(ball.y <= y - (h * 0.5) || ball.y >= y + (h * 0.5)) {
                ball.invertYDir();
            }
            else if(ball.x < x || ball.x > x) {
                ball.invertXDir();
            }
            ball.changeAngleRandomically();
        }
        
        // keep updating just ball until it not collides anymore
        while (collides(ball.x, ball.y, ball.w, ball.h)) {
            ball.update(gc);
        }        
    }
    
    // received broadcast messages

    @Override
    public void reset() {
        this.x = gc.getWidth() / 2 - 16;
    }

    @Override
    public void stateChanged() {
        visible = game.getState() != INIT;
    }
	
}
