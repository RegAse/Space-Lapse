package com.spacelapse.UI;

import com.spacelapse.resourcemanager.Fonts;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Font;


/**
 * TextLabel
 */
public class TextLabel {

    public static void render(int x, int y, String content, Color color, Font font, Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(content, x, y);

        graphics.setColor(oldColor);
    }
}
