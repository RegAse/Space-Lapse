package com.spacelapse;


import java.io.*;
import java.util.ArrayList;

import com.spacelapse.UI.TextInput;
import com.spacelapse.UI.TextLabel;
import com.spacelapse.entities.Asteroid;
import com.spacelapse.entities.Enforcer;
import com.spacelapse.entities.Entity;
import com.spacelapse.entities.Ship;
import com.spacelapse.resourcemanager.Fonts;
import com.spacelapse.resourcemanager.Textures;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.font.effects.OutlineEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import sun.misc.IOUtils;

public class MainMenu extends BasicGameState {

    // Positions on screen variables
    public Vector2f midScreen;
    public int menu = 150;
    public int playerStats = 500;

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
    private Font font24;
    private Font font20;
    private Font font18;

    Player player = Player.loadPlayer();

    /** Fighter entities tests **/
    public static ArrayList<Entity> entities = new ArrayList<>();

    public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {
        midScreen = new Vector2f(gc.getWidth() / 2, gc.getHeight() / 2);
        font24 = Fonts.getImpact(24);
        font20 = Fonts.getImpact(20);
        font18 = Fonts.getImpact(18);

        //entities.add(new Enforcer(100, 100, 0.5f, 40f));
        //entities.add(new Asteroid(100, 100, 0.7f, 30f));

        MainMenuParticleSystem();

        // Buttons
        Color color = Color.white;
        Color hoverColor = Color.darkGray;
        //hostgame = new TextButton("Host Game", menu, 140, 120, 40, color, hoverColor);
        joingame = new TextButton("Join Game", menu, 140, 120, 40, color, hoverColor);
        settings = new TextButton("Settings", menu, 180, 120, 40, color, hoverColor);
        quitgame = new TextButton("Quit Game", menu, 220, 120, 40, color, hoverColor);

        // Inputs
        port = new TextInput();
        /*
        hostgame.addOnClickEventListener(new ClickEventListener() {
            @Override
            public void onClickEvent(ClickEvent evt) {
                // Run a java app in a separate system process
                /*Process proc = null;
                try {
                    proc = Runtime.getRuntime().exec("java -jar builds/Space_Lapse_Server_Build_2.jar");
                } catch (IOException e) {
                    System.out.println("Error starting server executable file");
                }
                System.out.println("Started server jar file");

                GameClient gameclient = new GameClient();
                gameclient.JoinGame("localhost", 8976);
                sbg.enterState(2); // Enter Join State
            }
        });
        */
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
        graphics.drawImage(Textures.getSpaceBackground(), - 500, - 500);
        Fonts.setFontSize(30);
        graphics.setFont(Fonts.getImpact());
        system2.render();
        system.render();
        Input input = gameContainer.getInput();
        graphics.setBackground(Color.darkGray.darker(0.6f));

        // Render the entities
        //entities.render(gc, graphics, texture, bulletTexture);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(gameContainer, graphics);
        }

        graphics.setColor(Color.white);
        graphics.drawString("Space Lapse", menu - 60, 80);

        // Render the buttons
        //hostgame.setFont(Fonts.getImpact());
        //hostgame.render(gameContainer, graphics);
        joingame.setFont(Fonts.getImpact());
        joingame.render(gameContainer, graphics);
        settings.render(gameContainer, graphics);
        settings.setFont(Fonts.getImpact());
        quitgame.render(gameContainer, graphics);
        quitgame.setFont(Fonts.getImpact());

        //displayPlayerInfo(gameContainer, graphics);
        // Render the textinputs
        // port.render(gc, graphics);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();
        //Ship ship = (Ship)entities.get(0);
        //ship.Controller(gameContainer, delta);


        // Particle System
        system.update(delta);
        system2.update(delta);
    }

    /**
     * Displays the xp and level
     */
    public void displayPlayerInfo(GameContainer gameContainer, Graphics graphics) {
        // Start x: 400
        graphics.setColor(Color.decode("#292929"));

        graphics.fillRect(playerStats - 10, 70, 250, 300);
        graphics.setColor(Color.white);

        graphics.drawString(player.screenName, playerStats, 80);

        //TextLabel.render(playerStats, 120, "Level " + player.level, Color.decode("#DE8D1B"), font24, graphics);

        TextLabel.render(playerStats, 150, "XP to level up " + player.xp + " XP", Color.gray, font18, graphics);

        TextLabel.render(playerStats, 190, "Stats", Color.white, font20, graphics);
        TextLabel.render(playerStats + 5, 215, "Damage  " + player.getDamage(), Color.lightGray, font18, graphics);
        TextLabel.render(playerStats + 5, 235, "Fire Rate  " + player.getFireRate(), Color.lightGray, font18, graphics);
        TextLabel.render(playerStats + 5, 255, "Health  " + player.getHealth(), Color.lightGray, font18, graphics);
        TextLabel.render(playerStats + 5, 275, "Dew power  " + player.getDamage() * player.getFireRate(), Color.lightGray, font18, graphics);
        TextLabel.render(playerStats + 5, 295, "Shield  " + player.getHealth() * 0.2f, Color.lightGray, font18, graphics);
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
