package com.spacelapse;

import com.spacelapse.resourcemanager.Fonts;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import javax.swing.event.EventListenerList;

public class TextButton{
    /* TODO: button hover (X)
    *  TODO: button press (X)
    *  TODO: button audio ()
    *  TODO: button color (X)
    *  TODO: button render (X)
    * */

    private String content;
    private Vector2f position;
    private float width;
    private float height;
    private Color color;
    private Color hoverColor;
    private boolean _pressed;
    private TrueTypeFont font = Fonts.getImpact();

    public TextButton(String content, float x, float y, float width, float height, Color color, Color hoverColor) {
        this.content = content;
        this.position = new Vector2f(x, y);
        this.width = width;
        this.height = height;
        this.color = color;
        this.hoverColor = hoverColor;
    }

    public void render(GameContainer gameContainer, Graphics graphics) {
        Font oldFont = graphics.getFont();
        Color oldColor = graphics.getColor();

        graphics.setFont(font);
        graphics.setColor(color);
        Input input = gameContainer.getInput();
        if (input.getMouseX() >= position.x && input.getMouseX() <= position.x + font.getWidth(this.content)) {
            if (input.getMouseY() >= position.y && input.getMouseY() <= position.y + font.getHeight(this.content)) {
                if (input.isMouseButtonDown(input.MOUSE_LEFT_BUTTON)) {
                    _pressed = true;
                }
                graphics.setColor(this.hoverColor);
                if (_pressed && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    _pressed = false;
                    fireMyEvent(new ClickEvent(this));
                }
            }
        }

        // Render the TextButton
        graphics.drawString(content, position.x, position.y);

        // Reset graphics values
        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }

    public void setFont(TrueTypeFont font1) {
        this.font = font1;
    }

    public TrueTypeFont getFont() {
        return this.font;
    }

    public void setHoverColor(Color color)
    {
        this.hoverColor = color;
    }

    public Color getHoverColor()
    {
        return this.hoverColor;
    }

    /* Event listener */
    protected EventListenerList listenerList = new EventListenerList();

    public void addOnClickEventListener(ClickEventListener listener) {
        listenerList.add(ClickEventListener.class, listener);
    }
    public void removeOnClickEventListener(ClickEventListener listener) {
        listenerList.remove(ClickEventListener.class, listener);
    }
    void fireMyEvent(ClickEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == ClickEventListener.class) {
                ((ClickEventListener) listeners[i+1]).onClickEvent(evt);
            }
        }
    }
}
