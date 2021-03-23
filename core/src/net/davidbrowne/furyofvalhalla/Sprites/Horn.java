package net.davidbrowne.furyofvalhalla.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Screens.PlayScreen;

public class Horn extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    public Horn(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tiles");
        this.manager = manager;
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(Game.HORN_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        getObjectCell().setTile(null);
        manager.get("audio/sounds/coin.wav", Sound.class).play(screen.getGame().getSoundVolume());
        screen.getPlayer().addScore(5);
    }

    @Override
    public void onHeadHit() {

    }

}
