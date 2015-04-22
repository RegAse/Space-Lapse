package com.spacelapse;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Settings extends BasicGameState{

    private TextButton back;

    public Settings(int state)
    {

    }

    public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException
    {
        Color color = Color.white;
        Color hovercolor = Color.darkGray;

        // Buttons
        back = new TextButton("Back to Main Menu", 40, gc.getHeight() - 80, 230, 40, color, hovercolor);
        back.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                sbg.enterState(0);
            }
        });
    }

    public void render(GameContainer gameContainer, StateBasedGame sbg, Graphics graphics) throws SlickException
    {
        //graphics.setFont(MainMenu.fontbig);
        graphics.drawString("Settings", 40, 40);

        //graphics.setFont(MainMenu.font);
        graphics.drawString("Video", 60 , 100);
        graphics.drawString("Audio", 60 , 140);
        back.render(gameContainer, graphics);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {

    }

    public int getID()
    {
        return 1;
    }
}
