package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Survival extends BasicGameState {

    public Survival(int state)
    {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        g.drawString("Survival", 40, 40);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();
    }

    public int getID()
    {
        return 1;
    }
}
