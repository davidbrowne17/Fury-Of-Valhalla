package net.davidbrowne.furyofvalhalla.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Tools.TransitionScreen;

public class LevelSelectScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    public Stage stage;
    private Label label;
    private Preferences prefs;
    private Skin mySkin;
    private int level,maxLevel;
    public LevelSelectScreen(final Game game, final AssetManager manager){
        this.game=game;
        this.manager=manager;
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,new OrthographicCamera());
        Gdx.input.setInputProcessor(stage);
        prefs = Gdx.app.getPreferences("ValhallaGamePrefs");
        maxLevel = prefs.getInteger("level");
        background=new Texture(Gdx.files.internal("bg.png"));
        stage = new Stage(viewport,game.batch);
        if(maxLevel!=0)
            level=maxLevel;
        else
            level=1;
        if(maxLevel==21){
            maxLevel--;
        }
        if(level==21){
            level--;
        }
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button = new TextButton("Next",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(level<maxLevel)
                    level++;
                manager.get("audio/sounds/click.wav", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        // Text Button
        Button button1 = new TextButton("Previous",mySkin,"default");
        button1.setTransform(true);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(level!=1)
                    level--;
                manager.get("audio/sounds/click.wav", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        // Text Button
        Button button2 = new TextButton("Play",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TransitionScreen(game.getScreen(),new CutsceneScreen(game,manager,level),game));
                dispose();
                manager.get("audio/sounds/click.wav", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });

        // Text Button
        Button button3 = new TextButton("Exit",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game,manager));
                dispose();
                manager.get("audio/sounds/click.wav", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });

        Window window = new Window(" ",mySkin);
        label = new Label(String.format("Level: %02d",level), mySkin);
        label.setFontScale(3.6f);
        window.add(label);
        table.add(window).center().padTop(200).expandX();
        table.row();
        table.add(button).center().padTop(200).expandX();
        table.row();
        table.add(button1).center().padTop(50).expandX();
        table.row();
        table.add(button2).center().padTop(50).expandX();
        table.row();
        table.add(button3).center().padTop(50).expandX();

        stage.addActor(table);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, V_WIDTH,V_HEIGHT);
        game.batch.end();
        stage.draw();
    }

    public void update(){
        label.setText(String.format("Level: %02d",level));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        mySkin.dispose();
    }
}
