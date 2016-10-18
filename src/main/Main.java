package main;

import br.ol.breakout.BreakoutGame;
import static br.ol.breakout.BreakoutGame.SCREEN_HEIGHT;
import static br.ol.breakout.BreakoutGame.SCREEN_WIDTH;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import me.winspeednl.libz.core.GameCore;

/**
 *
 * @author leonardo
 */
public class Main {
    
    public static void main(String args[]) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                GameCore gc = new GameCore(new BreakoutGame());
                gc.setScale(1);
                gc.setTitle("Breakout - Powered by LibZ");
                gc.setWidth(SCREEN_WIDTH);
                gc.setHeight(SCREEN_HEIGHT);
                gc.setSpriteBGColor(0xFF000000);
                gc.start();
                
            });
        } catch (Exception ex) {
            Logger.getLogger(BreakoutGame.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
}
