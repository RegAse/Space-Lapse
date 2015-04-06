package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

    public static final String GameName = "Space Lapse";
    public static final double VersionNumber = 0.01;

    // States
    public static final int mainmenu = 0;
    public static final int survival = 1;

    public Game()
    {
        super(GameName + " Version: " + VersionNumber);
        this.addState(new MainMenu(mainmenu));
        this.addState(new Survival(survival));
    }

    public void initStatesList(GameContainer gc) throws SlickException
    {
        this.getState(mainmenu).init(gc, this);
        this.enterState(mainmenu);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        // Draws the scene
        g.drawString("Space Lapse", 30, 30);
    }

    public static void main(String[] arguments)
    {
        try
        {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(1280, 720, false);
            app.setShowFPS(true); // set to false later
            app.setAlwaysRender(true);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
}