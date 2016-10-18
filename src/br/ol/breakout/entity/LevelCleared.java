package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.logger.Logger;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * LevelCleared class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelCleared extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    private String text = "";
    
    public LevelCleared(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("levelCleared");
    }

    @Override
    public void updateCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting level cleared ...");
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    game.nextLevel();
                    break yield;
            }
        }
    }
    
    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            int textX = 0;
            int textY = (int) (gc.getHeight() * 0.45);
            int textWidth = gc.getWidth();
            int textAlign = 2;
            int textColor = 0xFFFFFFFF;
            int textBorderColor = 0xFF3333AA;
            game.drawText(render, text, textColor
                    , textX, (int) textY
                    , textWidth, textAlign, Font.STANDARDX2, 3, textBorderColor);
        }
    }
    
    // received broadcast messages
    
    @Override
    public void stateChanged() {
        visible = game.getState() == LEVEL_CLEARED;
        if (game.getState() == LEVEL_CLEARED) {
            text = "LEVEL " + game.getLevel() + " CLEARED :) !";
            instructionPointer = 0;
        }
    }
    
}
