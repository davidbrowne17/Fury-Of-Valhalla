package net.davidbrowne.furyofvalhalla.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Screens.PlayScreen;

public class Spike extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    private final int BLUE_SKY =6;

    public Spike(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager=manager;
        this.map = screen.getMap();
        tileSet = map.getTileSets().getTileSet("tiles");
        fixture.setUserData(this);
        setCategoryFilter(Game.SPIKE_BIT);
    }

    @Override
    public void onHit() {
        screen.getPlayer().die();
    }

    @Override
    public void onHeadHit() {

    }
}
