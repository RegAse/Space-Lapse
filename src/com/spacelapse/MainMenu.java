package com.spacelapse;


import java.io.File;

import com.spacelapse.UI.TextInput;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainMenu extends BasicGameState {

    public Vector2f midScreen;
    public java.awt.Font awtfont;
    public static TrueTypeFont font;
    public java.awt.Font awtfontbig;
    public static TrueTypeFont fontbig;

    // Particle System
    private ParticleSystem system;
    private ParticleSystem system2;
    private int val = 100;

    // Ship flying around
    private Ship ship;

    // Buttons
    private TextButton hostgame;
    private TextButton joingame;
    private TextButton settings;
    private TextButton quitgame;

    // Inputs
    private TextInput port;

    /** Images **/
    private static Image texture;
    private static Image bulletTexture;

    public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {
        midScreen = new Vector2f(gc.getWidth() / 2, gc.getHeight() / 2);
        awtfont = new java.awt.Font("Impact", Font.PLAIN, 30);
        font = new TrueTypeFont(awtfont, true);
        awtfontbig = new java.awt.Font("Impact", Font.PLAIN, 40);
        fontbig = new TrueTypeFont(awtfontbig, true);
        ship = new Ship(new Vector2f(100 ,100), 0.5f);
        /** Setup images **/
        texture = new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false);
        bulletTexture = new Image("data/bullets/bullet01_tiny2.png", false);
        MainMenuParticleSystem();


        // Buttons
        Color color = Color.white;
        Color hovercolor = Color.darkGray;
        hostgame = new TextButton("Host Game", 200, 200, 120, 40, color, hovercolor);
        joingame = new TextButton("Join Game", 200, 240, 120, 40, color, hovercolor);
        settings = new TextButton("Settings", 200, 280, 120, 40, color, hovercolor);
        quitgame = new TextButton("Quit Game", 200, 320, 120, 40, color, hovercolor);

        // Inputs
        port = new TextInput();

        hostgame.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                GameClient gameclient = new GameClient();
                gameclient.JoinGame("localhost", 8976);
                sbg.enterState(2); // Enter Host State
            }
        });
        joingame.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {

                GameClient gameclient = new GameClient();
                gameclient.JoinGame("localhost", 8976);
                sbg.enterState(2); // Enter Join State
            }
        });
        settings.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                sbg.enterState(1); // Enter settings
            }
        });
        quitgame.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                System.exit(0);
            }
        });
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        system2.render();
        system.render();
        Input input = gc.getInput();
        graphics.setBackground(Color.darkGray.darker(0.6f));

        // Render the ship
        ship.render(graphics, texture, bulletTexture);


        graphics.setFont(fontbig);
        graphics.drawString("Space Lapse", midScreen.x - 370, 100);
        graphics.setColor(Color.white);
        graphics.setFont(font);
        // Render the buttons
        hostgame.render(gc, graphics);
        joingame.render(gc, graphics);
        settings.render(gc, graphics);
        quitgame.render(gc, graphics);

        // Render the textinputs
        //port.render(gc, graphics);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();
        // Feed all the classes with the input and the delta
        ship.Controller(gameContainer, delta, texture);



        // Particle System
        system.update(delta);
        system2.update(delta);
    }

    public void MainMenuParticleSystem() throws SlickException
    {
        Image image = new Image("data/bullets/bullet01.png", false);
        Image image_1 = new Image("data/particle.png", false);
        system = new ParticleSystem(image_1, 1500);
        system2 = new ParticleSystem(image_1, 1500);

        try {
            File xmlFile = new File("data/test_emitter.xml");
            ConfigurableEmitter emitter = ParticleIO.loadEmitter(xmlFile);
            emitter.setPosition(midScreen.x + 100, 358);
            system.addEmitter(emitter);
            system2.addEmitter(emitter);
            emitter = ParticleIO.loadEmitter(xmlFile);
            emitter.setPosition(midScreen.x, 558);
            system.addEmitter(emitter);
            system2.addEmitter(emitter);
        } catch (Exception e) {
            System.out.println("Exception: " +e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }

        system.setBlendingMode(ParticleSystem.BLEND_COMBINE);
    }

    public MainMenu(int state) {

    }

    public int getID()
    {
        return 0;
    }
}
