package com.spacelapse;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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

    public TextButton(String content, float x, float y, float width, float height, Color color, Color hoverColor)
    {
        this.content = content;
        this.position = new Vector2f(x, y);
        this.width = width;
        this.height = height;
        this.color = color;
        this.hoverColor = hoverColor;
    }

    public void render(GameContainer gc, Graphics g)
    {
        g.setColor(color);
        Input input = gc.getInput();
        if (input.getMouseX() >= position.x && input.getMouseX() <= position.x + width)
        {
            if (input.getMouseY() >= position.y && input.getMouseY() <= position.y + height)
            {
                if (input.isMouseButtonDown(input.MOUSE_LEFT_BUTTON))
                {
                    _pressed = true;
                }
                g.setColor(this.hoverColor);
                if (_pressed && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
                {
                    _pressed = false;
                    fireMyEvent(new ClickEvent(this));
                }
            }
        }

        // Render the TextButton
        g.drawString(content, position.x, position.y);

        // Reset color
        g.setColor(Color.white);
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
        for (int i = 0; i < listeners.length; i = i+2){
            if (listeners[i] == ClickEventListener.class) {
                ((ClickEventListener) listeners[i+1]).onClickEvent(evt);
            }
        }
    }
}
