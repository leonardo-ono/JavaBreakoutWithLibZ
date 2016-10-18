package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import com.sun.glass.events.KeyEvent;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.logger.Logger;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * GameOver class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GameOver extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    
    private final String gameOverText = "GAME OVER";
    
    private boolean newHiscoreTextVisible;
    private boolean newHiscoreTextVisible2;
    private final String newHiscoreText = "IT'S A NEW HISCORE !!!";
    private double newHiscoreX;
    private double newHiscoreY;
    
    private int letterIndex = 0;
    
    public GameOver(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("gameOver");
    }
    
    @Override
    public void updateGameOver() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting game over ...");
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2; 
                    break yield;
                case 2:
                    if (System.currentTimeMillis() - waitTime < 200) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    letterIndex++;
                    if (letterIndex == gameOverText.length()) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 3;
                    }
                    break yield;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    // check for hiscore
                    newHiscoreX = gc.getWidth() / 2;
                    newHiscoreY = (int) (gc.getHeight() * 0.60) + 20;
                    newHiscoreTextVisible = game.isNewHiscore();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 4;
                    break yield;
                case 4:
                    if (System.currentTimeMillis() - waitTime < 5000) {
                        break yield;
                    }
                    instructionPointer = 5;
                    if (!game.isNewHiscore()) {
                        instructionPointer = 6;
                    }
                    break yield;
                case 5:
                    newHiscoreX += 0.1 * (100 - newHiscoreX);
                    newHiscoreY += 0.1 * (8 - newHiscoreY);
                    if (Math.abs(100 - newHiscoreX) < 3 && Math.abs(8 - newHiscoreY) < 3) {
                        game.updateHiscore();
                        newHiscoreTextVisible = false;
                        newHiscoreTextVisible2 = false;
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 6;
                    }
                    break yield;
                case 6:
                    if (gc.getInput().isKeyPressed(KeyEvent.VK_SPACE)) {
                        instructionPointer = 7;
                    }
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    instructionPointer = 7;
                    break yield;
                case 7:
                    game.backToTitle();
                    break yield;
            }
        }
    }

    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            int textX = 0;
            int textY = (int) (gc.getHeight() * 0.5) - 20;
            int textWidth = gc.getWidth();
            int textAlign = 2;
            int textColor = 0xFFFFFFFF;
            int textBorderColor = 0xFF3333AA;
            game.drawText(render, gameOverText.substring(0, letterIndex), textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX4, 2, textBorderColor);
            
            if (newHiscoreTextVisible && (int) (System.nanoTime()* 0.000000005) % 2 == 0) {
                textY = (int) (gc.getHeight() * 0.60);
                textAlign = 2;
                game.drawText(render, newHiscoreText, textColor
                        , textX, textY
                        , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
                newHiscoreTextVisible2 = true;
            }
            
            if (newHiscoreTextVisible2) {
                textAlign = 1;
                game.drawText(render, game.getScore() + "", textColor
                        , (int) newHiscoreX, (int) newHiscoreY
                        , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
            }
        }
    }
    
    // received broadcast messages
    
    @Override
    public void stateChanged() {
        visible = game.getState() == GAME_OVER;
        if (game.getState() == GAME_OVER) {
            instructionPointer = 0;
            letterIndex = 0;
            newHiscoreTextVisible = false;
        }
    }
    
}
