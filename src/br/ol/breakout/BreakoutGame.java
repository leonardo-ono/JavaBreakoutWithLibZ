package br.ol.breakout;

import static br.ol.breakout.BreakoutGame.State.*;
import br.ol.breakout.entity.Paddle;
import br.ol.breakout.entity.Brick;
import br.ol.breakout.entity.Ball;
import br.ol.breakout.entity.Ending;
import br.ol.breakout.entity.GameOver;
import br.ol.breakout.entity.HUD;
import br.ol.breakout.entity.Init;
import br.ol.breakout.entity.LevelCleared;
import br.ol.breakout.entity.ReadyGo;
import br.ol.breakout.entity.Title;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.winspeednl.libz.core.GameCore;
import me.winspeednl.libz.core.LibZ;
import me.winspeednl.libz.screen.Font;
import me.winspeednl.libz.screen.Render;

/**
 * This version of Breakout game was implemented using LibZ game library 
 * that was developed by winspeednl.
 *
 * You can get LibZ from: https://github.com/winspeednl/LibZ
 *
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BreakoutGame extends LibZ {

    public static final int SCREEN_WIDTH = 400, SCREEN_HEIGHT = 300;
    public static final String LEVELS_RESOURCE = "/res/levels";
    
    private static final int[] brickColors = {
        0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFF00FFFF, 0xFFFF00FF
    };
    
    public static enum State {
        INIT, TITLE, READY, PLAYING, LEVEL_CLEARED, GAME_OVER, ENDING
    }

    private State state;
    private int level;
    private int score;
    private int hiscore;
    private int lives;

    // entities
    private final List<BreakoutEntity> entities;
    private final Brick[][] bricks;
    private Ball ball;
    
    private int[][][] levels;
    
    public BreakoutGame() {
        state = State.INIT;
        entities = new ArrayList<>();
        bricks = new Brick[10][5];
        loadLevels();
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            broadcastMessage("stateChanged");
        }
    }

    public State getState() {
        return state;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public void clearScore() {
        score = 0;
    }

    public int getHiscore() {
        return hiscore;
    }

    public boolean isNewHiscore() {
        return score > hiscore;
    }

    public void updateHiscore() {
        if (score > hiscore) {
            hiscore = score;
        }
    }

    public int getLives() {
        return lives;
    }

    public Ball getBall() {
        return ball;
    }

    @Override
    public void init(GameCore gc) {
        entities.add(new Init(gc, this));
        entities.add(ball = new Ball(gc, this));
        entities.add(new Paddle(gc, this));
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 10; x++) {
                Brick brick = new Brick(gc, this, x, y);
                bricks[x][y] = brick;
                entities.add(brick);
            }
        }
        setLevel((int) (getLevelsSize() * Math.random()));
        entities.add(new Title(gc, this));
        entities.add(new HUD(gc, this));
        entities.add(new ReadyGo(gc, this));
        entities.add(new LevelCleared(gc, this));
        entities.add(new GameOver(gc, this));
        entities.add(new Ending(gc, this));
    }

    public boolean isLevelCleared() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 10; x++) {
                if (!bricks[x][y].isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update(GameCore gc) {
        entities.forEach((entity) -> {
            if (entity instanceof Brick) {
                Brick brick = (Brick) entity;
                if (!brick.isDestroyed()) {
                    brick.update(gc);
                }
            }
            else {
                entity.update(gc);
            }
        });
    }

    @Override
    public void render(GameCore gc, Render r) {
        entities.forEach((entity) -> {
            if (entity instanceof Brick) {
                Brick brick = (Brick) entity;
                if (!brick.isDestroyed()) {
                    brick.render(gc, r);
                }
            }
            else {
                entity.render(gc, r);
            }
        });
    }

    public void broadcastMessage(String message) {
        entities.forEach((entity) -> {
            try {
                Method method = entity.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(entity);
                }
            } catch (Exception ex) {
            }
        });
    }

    // --- levels ---
    
    private void loadLevels() {
        InputStream is = getClass().getResourceAsStream(LEVELS_RESOURCE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            int lvl = 0;
            int y = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("s")) {
                    int lvls = Integer.parseInt(line.split("\\ ")[1]);
                    int cols = Integer.parseInt(line.split("\\ ")[2]);
                    int rows = Integer.parseInt(line.split("\\ ")[3]);
                    levels = new int[lvls][cols][rows];
                }
                else if (line.startsWith("-")) {
                    lvl++;
                    y = 0;
                }
                else {
                    for (int x = 0; x < line.length(); x++) {
                        levels[lvl][x][y] = Integer.parseInt(line.substring(x, x + 1));
                    }
                    y++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BreakoutGame.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    private int getLevelsSize() {
        return levels.length;
    }
    
    private void setLevel(int lvl) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 10; x++) {
                int colorIndex = levels[lvl][x][y];
                if (colorIndex > 0) {
                    bricks[x][y].setDestroyed(false);
                    bricks[x][y].setColor(brickColors[colorIndex - 1]);
                }
                else {
                    bricks[x][y].setDestroyed(true);
                }
            }
        }
    }
    
    // --- draw text ---
    
    // draw text. align 1=left, 2=center, 3=right
    public void drawText(Render render, String text, int color
            , int x, int y, int w, int align, Font font) {
        
        int textWidth = render.getStringWidth(text, font);
        int tx = x; // left alignment
        switch (align) {
            case 2: tx = x + w / 2 - textWidth / 2; break;
            case 3: tx = x + w - textWidth; break;
        }
        render.drawString(text, color, tx, y, font);        
    }
    
    public void drawText(Render render, String text, int color, int x, int y
            , int w, int align, Font font, int thickness, int borderColor) {
        
        for (int dy=-thickness; dy<=thickness; dy++) {
            for (int dx=-thickness; dx<=thickness; dx++) {
                drawText(render, text, borderColor, x + dx, y + dy, w, align, font);
            }
        }
        drawText(render, text, color, x, y, w, align, font);
    }
    
    // ---
    
    // for debugging purposes
    public void destroyOneBrick() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 10; x++) {
                if (!bricks[x][y].isDestroyed()) {
                    bricks[x][y].setDestroyed(true);
                    return;
                }
            }
        }
    }
    
    public void startGame() {
        score = 0;
        level = 1;
        lives = 3;
        setLevel(level - 1);
        startReady();
    }
    
    public void startReady() {
        broadcastMessage("reset");
        broadcastMessage("showScore");
        broadcastMessage("showLevel");
        broadcastMessage("showLives");
        setState(READY);
    }
    
    public void startPlaying() {
        setState(PLAYING);
    }

    public void died() {
        lives--;
        if (lives == 0) {
            setState(GAME_OVER);
        }
        else {
            setState(READY);
            broadcastMessage("reset");
        }
    }
    
    public void backToTitle() {
        setLevel((int) (getLevelsSize() * Math.random()));
        broadcastMessage("reset");
        broadcastMessage("hideScore");
        broadcastMessage("hideLevel");
        broadcastMessage("hideLives");
        setState(TITLE);
    }

    public void levelCleared() {
        setState(LEVEL_CLEARED);
    }

    public void nextLevel() {
        if (level == getLevelsSize()) {
            setState(ENDING);
        }
        else {
            level++;
            setLevel(level - 1);
            startReady();
        }
    } 
    
    public void backToIntro() {
        setLevel((int) (getLevelsSize() * Math.random()));
        broadcastMessage("reset");
        broadcastMessage("hideScore");
        broadcastMessage("hideLevel");
        broadcastMessage("hideLives");
        setState(INIT);
    }
    
}
