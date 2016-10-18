package br.ol.breakout.entity;

import br.ol.breakout.BreakoutEntity;
import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.State.*;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.logger.Logger;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * Init class.
 * 
 * Show 'powered by LibZ' and 'O.L. presents'
 *
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Init extends BreakoutEntity {
    
    private final Logger log;
    private int instructionPointer;
    private long waitTime;
    
    private String text;
    private double textColorIntensity;
    private int textColor;
    private int textColorBorder;
    private int textX;
    private int textY;
    private int textWidth;
    private int textAlign;
    
    public Init(GameCore gc, BreakoutGame game) {
        super(gc, game);
        log = new Logger("init");
    }

    @Override
    public void updateInit() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    log.info("starting init ...");
                    
                    // game.setState(TITLE);
                    
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1; 
                    break yield;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    text = "POWERED BY LIBZ";
                    textX = 0;
                    textY = gc.getHeight() / 2 - 20;
                    textWidth = gc.getWidth();
                    textAlign = 2;
                    textColorIntensity = 0;
                    visible = true;
                    instructionPointer = 3;
                    break yield;
                case 3:
                    textColorIntensity += 0.02;
                    if (textColorIntensity < 1) {
                        break yield;
                    }
                    textColorIntensity = 1;
                    instructionPointer = 4;
                case 4:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 5; 
                    break yield;
                case 5:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    instructionPointer = 6;
                case 6:
                    textColorIntensity -= 0.02;
                    if (textColorIntensity > 0) {
                        break yield;
                    }
                    textColorIntensity = 0;
                    instructionPointer = 7;
                case 7:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 8; 
                    break yield;
                case 8:
                    if (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    text = "O.L. PRESENTS";
                    instructionPointer = 9;
                    break yield;
                case 9:
                    textColorIntensity += 0.01;
                    if (textColorIntensity < 1) {
                        break yield;
                    }
                    textColorIntensity = 1;
                    instructionPointer = 10;
                case 10:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 11; 
                    break yield;
                case 11:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    instructionPointer = 12;
                case 12:
                    textColorIntensity -= 0.01;
                    if (textColorIntensity > 0) {
                        break yield;
                    }
                    textColorIntensity = 0;
                    instructionPointer = 13;
                case 13:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 14; 
                    break yield;
                case 14:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    instructionPointer = 15;
                case 15:
                    visible = false;
                    game.setState(TITLE);
                    break yield;
            }
        }
    }

    @Override
    public void render(GameCore gc, Render render) {
        if (visible) {
            textColor = (int) (255 * textColorIntensity);
            textColor = (255 << 24) + (textColor << 16) + (textColor << 8) + textColor;
            textColorBorder = (255 << 24) + ((int) (0x33 * textColorIntensity) << 16) 
                    + ((int) (0x33 * textColorIntensity) << 8) + (int) (0xAA * textColorIntensity);
            
            game.drawText(render, text, textColor
                    , textX, textY
                    , textWidth, textAlign, Font.STANDARDX2, 2, textColorBorder);
        }
    }
    
    @Override
    public void stateChanged() {
        visible = game.getState() == INIT;
        if (game.getState() == INIT) {
            instructionPointer = 0;
        }
    }
    
}
