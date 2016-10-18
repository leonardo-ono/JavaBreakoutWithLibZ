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
 * Ending class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ending extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    
    private int currentIndex;
    private final int[] letterIndex = new int[3];
    private final String[] messageText =  { "CONGRATULATIONS :) !"
            , "YOU HAVE CLEARED ALL AVAILABLE LEVELS !"
            , "THANKS FOR PLAYING :) !" };
    
    private boolean newHiscoreTextVisible;
    private boolean newHiscoreTextVisible2;
    private final String newHiscoreText = "IT'S A NEW HISCORE !!!";
    private double newHiscoreX;
    private double newHiscoreY;
    
    public Ending(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("ending");
    }
    
    @Override
    public void updateEnding() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting 'ending' ...");
                    waitTime = System.currentTimeMillis();
                    currentIndex = 0;
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
                    if (System.currentTimeMillis() - waitTime < 100) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    letterIndex[currentIndex]++;
                    if (letterIndex[currentIndex] == messageText[currentIndex].length()) {
                        currentIndex++;
                        if (currentIndex > 2) {
                            instructionPointer = 3;
                        }
                        else {
                            instructionPointer = 1;
                        }
                        waitTime = System.currentTimeMillis();
                    }
                    break yield;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    instructionPointer = 4;
                    break yield;
                case 4:
                    // check for hiscore
                    newHiscoreX = gc.getWidth() / 2;
                    newHiscoreY = (int) (gc.getHeight() * 0.60) + 20;
                    newHiscoreTextVisible = game.isNewHiscore();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 5;
                    break yield;
                case 5:
                    if (System.currentTimeMillis() - waitTime < 5000) {
                        break yield;
                    }
                    instructionPointer = 6;
                    if (!game.isNewHiscore()) {
                        instructionPointer = 7;
                    }
                    break yield;
                case 6:
                    newHiscoreX += 0.1 * (100 - newHiscoreX);
                    newHiscoreY += 0.1 * (8 - newHiscoreY);
                    if (Math.abs(100 - newHiscoreX) < 3 && Math.abs(8 - newHiscoreY) < 3) {
                        game.updateHiscore();
                        newHiscoreTextVisible = false;
                        newHiscoreTextVisible2 = false;
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 7;
                    }
                    break yield;
                case 7:
                    if (gc.getInput().isKeyPressed(KeyEvent.VK_SPACE)) {
                        instructionPointer = 8;
                    }
                    if (System.currentTimeMillis() - waitTime < 10000) {
                        break yield;
                    }
                    instructionPointer = 8;
                    break yield;
                case 8:
                    game.backToIntro();
                    break yield;
            }
        }
    }

    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            int textX = 0;
            int textY = 0;
            int textWidth = gc.getWidth();
            int textAlign = 2;
            int textColor = 0xFFFFFFFF;
            int textBorderColor = 0xFF3333AA;
            
            textY = (int) (gc.getHeight() * 0.20);
            game.drawText(render, messageText[0].substring(0, letterIndex[0]), textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX4, 2, textBorderColor);

            textY = (int) (gc.getHeight() * 0.40);
            game.drawText(render, messageText[1].substring(0, letterIndex[1]), textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);

            textY = (int) (gc.getHeight() * 0.50);
            game.drawText(render, messageText[2].substring(0, letterIndex[2]), textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
            
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
        visible = game.getState() == ENDING;
        if (game.getState() == ENDING) {
            instructionPointer = 0;
            letterIndex[0] = 0;
            letterIndex[1] = 0;
            letterIndex[2] = 0;
            newHiscoreTextVisible = false;
        }
    }
    
}
