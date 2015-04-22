package com.spacelapse;


import java.io.File;
import java.util.ArrayList;

import com.spacelapse.UI.TextInput;
import com.spacelapse.entities.Asteroid;
import com.spacelapse.entities.Enforcer;
import com.spacelapse.entities.Entity;
import com.spacelapse.resourcemanager.Fonts;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenu extends BasicGameState {

    public Vector2f midScreen;

    // Particle System
    private ParticleSystem system;
    private ParticleSystem system2;
    private int val = 100;

    // Buttons
    private TextButton hostgame;
    private TextButton joingame;
    private TextButton settings;
    private TextButton quitgame;

    // Inputs
    private TextInput port;

    /** Fighter entities tests **/
    public static ArrayList<Entity> entities = new ArrayList<>();

    public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {
        midScreen = new Vector2f(gc.getWidth() / 2, gc.getHeight() / 2);

        entities.add(new Enforcer(100, 100, 0.5f, 40f));
        entities.add(new Asteroid(100, 100, 0.7f, 30f));

        MainMenuParticleSystem();

        // Buttons
        Color color = Color.white;
        Color hoverColor = Color.darkGray;
        hostgame = new TextButton("Host Game", 200, 200, 120, 40, color, hoverColor);
        joingame = new TextButton("Join Game", 200, 240, 120, 40, color, hoverColor);
        settings = new TextButton("Settings", 200, 280, 120, 40, color, hoverColor);
        quitgame = new TextButton("Quit Game", 200, 320, 120, 40, color, hoverColor);

        // Inputs
        port = new TextInput();

        hostgame.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                // TODO Start the server ()
                GameClient gameclient = new GameClient();
                gameclient.JoinGame("localhost", 8976);
                sbg.enterState(2); // Enter Join State
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

    public void render(GameContainer gameContainer, StateBasedGame sbg, Graphics graphics) throws SlickException {
        graphics.setFont(Fonts.getImpact());
        //system2.render();
        //system.render();
        Input input = gameContainer.getInput();
        graphics.setBackground(Color.darkGray.darker(0.6f));

        // Render the entities
        //entities.render(gc, graphics, texture, bulletTexture);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(gameContainer, graphics);
        }

        graphics.drawString("Space Lapse", midScreen.x - 370, 100);
        graphics.setColor(Color.white);
        //graphics.setFont(font);
        // Render the buttons
        hostgame.render(gameContainer, graphics);
        joingame.render(gameContainer, graphics);
        settings.render(gameContainer, graphics);
        quitgame.render(gameContainer, graphics);

        // Render the textinputs
        // port.render(gc, graphics);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();

        // Particle System
        system.update(delta);
        system2.update(delta);
    }

    /**
     * Main Menu particle effect
     * @throws SlickException
     */
    public void MainMenuParticleSystem() throws SlickException {
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
