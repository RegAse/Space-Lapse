package com.spacelapse.resourcemanager;

import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class Fonts {

    private static int fontSize = 16;
    private static java.awt.Font awt_impact;
    private static TrueTypeFont impact;

    public static TrueTypeFont getImpact() {
        if (awt_impact == null || impact == null || awt_impact.getSize() != fontSize) {
            awt_impact = new java.awt.Font("Impact", Font.PLAIN, fontSize);
            impact = new TrueTypeFont(awt_impact, true);
        }
        return impact;
    }

    public static void setFontSize(Integer size) {
        fontSize = size;

    }
}
/* Move to Fonts
        awtfont = new java.awt.Font("Impact", Font.PLAIN, 30);
        font = new TrueTypeFont(awtfont, true);
        awtfontbig = new java.awt.Font("Impact", Font.PLAIN, 40);
        fontbig = new TrueTypeFont(awtfontbig, true);
public java.awt.Font awtfont;
public static TrueTypeFont font;
public java.awt.Font awtfontbig;
public static TrueTypeFont fontbig;*/