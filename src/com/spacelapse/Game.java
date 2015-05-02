package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

    public static final String GameName = "Space Lapse";
    public static final double VersionNumber = 0.02;
    public static boolean hasController = false; // set to true when using controller
    public static boolean usingController = false;
    private static int next_entity_id;

    // States
    public static final int mainmenu = 0;
    public static final int settings = 1;
    public static final int survival = 2;

    public Game() {
        super(GameName + " Version: " + VersionNumber);
        this.addState(new MainMenu(mainmenu));
        this.addState(new Settings(settings));
        this.addState(new Survival(survival));
    }

    public static int getNextEntityId(){
        return next_entity_id++;
    }

    public void initStatesList(GameContainer gameContainer) throws SlickException {
        //this.getState(survival).init(gc, this); -> useless

        //GameClient gameclient = new GameClient();
        //gameclient.JoinGame("localhost", 8976);

        this.enterState(mainmenu);
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(800, 600, false);
            app.setShowFPS(false); // set to false later
            app.setAlwaysRender(true);
            app.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}