package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboHally";

        /*
        * The width is composed of 12 tile-sizes to
        * fit the grid, and 4 tile-sizes to fit the
        * cards the player has been dealt and may choose
        * from
        */
        cfg.width = 32*20;
        cfg.height = 32*20;

        RoboRally roborally = new RoboRally();

        new LwjglApplication(roborally, cfg);
    }
}