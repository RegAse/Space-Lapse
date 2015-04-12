package com.spacelapse.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class TextInput {

    private String content = "hello";
    private int count = 0;
    private boolean wait = true;

    public TextInput() {

    }

    public void render(GameContainer gameContainer, Graphics graphics) {
        graphics.setColor(Color.darkGray);
        graphics.fillRect(95, 103, 100, 30);
        graphics.setColor(Color.lightGray);
        graphics.drawRect(95, 100 + 3, 100, 30);
        graphics.setColor(Color.white);
        graphics.drawString(content, 100, 100);

        if (count % 30 == 0 || wait)
        {
            wait = true;
            graphics.setColor(Color.white);
            graphics.drawLine(165,105,165,130);
            graphics.fillRect(165, 110, 1, 16);
            if (wait && count % 450 == 0)
            {
                count = 0;
                wait = false;
            }
        }
        graphics.setColor(Color.white);
        count++;
    }
}
