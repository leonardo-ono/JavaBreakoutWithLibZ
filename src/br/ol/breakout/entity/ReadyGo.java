package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.logger.Logger;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * ReadyGo class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class ReadyGo extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    private String text;
    
    public ReadyGo(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("readyGo");
    }

    @Override
    public void updateReady() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting 'ready' ...");
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    game.startPlaying();
                    break yield;
            }
        }
    }
    
    @Override
    public void updatePlaying() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting 'go' ...");
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    visible = false;
                    break yield;
            }
        }
    }

    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            int textX = 0;
            int textY = (int) (gc.getHeight() * 0.50);
            int textWidth = gc.getWidth();
            int textAlign = 2;
            int textColor = 0xFFFFFFFF;
            int textBorderColor = 0xFF3333AA;
            game.drawText(render, text, textColor
                    , textX, (int) textY
                    , textWidth, textAlign, Font.STANDARDX2, 3, textBorderColor);

            // draw level
            textY = (int) (gc.getHeight() * 0.40);
            String level = "LEVEL " + game.getLevel();
            game.drawText(render, level, textColor
                    , textX, (int) textY
                    , textWidth, textAlign, Font.STANDARDX2, 3, textBorderColor);
        }
    }
    
    // received broadcast messages
    
    @Override
    public void stateChanged() {
        switch (game.getState()) {
            case READY:
                instructionPointer = 0;
                text = "READY ?";
                visible = true;
                break;
            case PLAYING:
                instructionPointer = 0;
                text = "GO !";
                visible = true;
                break;
            default:
                visible = false;
                break;
        }
    }
    
}
