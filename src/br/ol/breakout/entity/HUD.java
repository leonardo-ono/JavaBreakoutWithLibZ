package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * HUD class.
 *
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends BreakoutEntity {
    
    private boolean hiscoreVisible = false;
    private boolean levelVisible = false;
    private boolean scoreVisible = false;
    private boolean livesVisible = false;
    
    public HUD(GameCore gc, BreakoutGame game) {
        super(gc, game);
    }
    
    @Override
    public void render(GameCore gc, Render render) {
        if (!visible) {
            return;
        }
        
        String text;
        int textX = 10;
        int textY = 10;
        int textWidth = gc.getWidth() - 20;
        int textAlign;
        int textColor = 0xFFFFFFFF;
        int textBorderColor = 0xFF3333AA;

        // draw hiscore
        if (hiscoreVisible) {
            textAlign = 1;
            text = "HI-SCORE: " + game.getHiscore();
            game.drawText(render, text, textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
        }
        
        // draw level
        if (levelVisible) {
            textAlign = 1;
            textX = gc.getWidth() / 3;
            text = "LV: " + game.getLevel();
            game.drawText(render, text, textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
        }
        

        // draw score
        if (livesVisible) {
            textAlign = 3;
            textX = 10;
            text = "LIVES: " + game.getLives();
            game.drawText(render, text, textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
        }
        
        // draw score
        if (scoreVisible) {
            textAlign = 1;
            textX = gc.getWidth() / 2;
            text = "SCORE: " + game.getScore();
            game.drawText(render, text, textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
        }
    }

    // received broadcast messages
    
    @Override
    public void stateChanged() {
        visible = game.getState() != INIT;
    }
    
    public void showScore() {
        scoreVisible = true;
    }

    public void showLevel() {
        levelVisible = true;
    }

    public void showHiscore() {
        hiscoreVisible = true;
    }
    
    public void showLives() {
        livesVisible = true;
    }

    public void hideScore() {
        scoreVisible = false;
    }

    public void hideLevel() {
        levelVisible = false;
    }

    public void hideHiscore() {
        hiscoreVisible = false;
    }
    
    public void hideLives() {
        livesVisible = false;
    }
    
}
