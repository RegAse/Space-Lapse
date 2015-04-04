package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;
import java.awt.Font;

public class MainMenu extends BasicGameState {

    public Vector2f midScreen;
    public java.awt.Font awtfont;
    public TrueTypeFont font;
    public java.awt.Font awtfontbig;
    public TrueTypeFont fontbig;


    public MainMenu(int state)
    {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        midScreen = new Vector2f(gc.getWidth() / 2, gc.getHeight() / 2);
        awtfont = new java.awt.Font("Impact", Font.PLAIN, 30);
        font = new TrueTypeFont(awtfont, true);
        awtfontbig = new java.awt.Font("Impact", Font.PLAIN, 40);
        fontbig = new TrueTypeFont(awtfontbig, true);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        //g.setAntiAlias(false);
        g.setFont(fontbig);

        // Title
        g.drawString("Space Lapse", midScreen.x - 370, 100);
        g.setFont(font);

        //Buttons
        g.drawString("Survival", midScreen.x - 300, 160);
        g.drawString("Adventure", midScreen.x - 300, 200);
        g.drawString("Settings", midScreen.x - 300, 240);
        g.drawString("Exit Game", midScreen.x - 300, 280);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();
    }

    public int getID()
    {
        return 0;
    }
}
