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
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    private double titleTextY;
    private boolean pushSpaceToPlayMessageVisible;
    private boolean creditsMessageVisible;
    
    public Title(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("title");
    }

    @Override
    public void updateTitle() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting title ...");
                    pushSpaceToPlayMessageVisible = false;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    titleTextY = titleTextY + 0.04 * (gc.getHeight() / 4.5 - titleTextY);
                    if (titleTextY < gc.getHeight() / 4.5 - 5) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    pushSpaceToPlayMessageVisible = (int) (System.nanoTime()* 0.000000005) % 2 == 0;
                    creditsMessageVisible = true;
                    game.broadcastMessage("showHiscore");
                    // start game when push space
                    if (gc.getInput().isKeyPressed(KeyEvent.VK_SPACE)) {
                        game.startGame();
                    }
                    break yield;
            }
        }
    }

    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            // draw title
            String text = "BREAKOUT";
            int textX = 0;
            int textWidth = gc.getWidth();
            int textAlign = 2;
            int textColor = 0xFFFFFFFF;
            int textBorderColor = 0xFF3333AA;
            game.drawText(render, text, textColor
                    , textX, (int) titleTextY
                    , textWidth, textAlign, Font.STANDARDX4, 5, textBorderColor);
            
            // draw "push space to play" message
            if (pushSpaceToPlayMessageVisible) {
                text = "PUSH SPACE TO PLAY";
                int textY = (int) (gc.getHeight() / 1.55);
                game.drawText(render, text, textColor
                        , textX, textY
                        , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
            }
            
            // draw credits
            if (creditsMessageVisible) {
                text = "PROGRAMMED BY O.L. (C) 2016";
                int textY = (int) (gc.getHeight() * 0.75);
                game.drawText(render, text, textColor
                        , textX, textY
                        , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);

                text = "POWERED BY LIBZ";
                textY = (int) (gc.getHeight() * 0.75) + 20;
                game.drawText(render, text, textColor
                        , textX, textY
                        , textWidth, textAlign, Font.STANDARDX2, 2, textBorderColor);
            }
        }
    }
    
    // received broadcast messages
    
    @Override
    public void stateChanged() {
        visible = game.getState() == TITLE;
        if (game.getState() == TITLE) {
            titleTextY = -100;
            instructionPointer = 0;
        }
    }
    
}
