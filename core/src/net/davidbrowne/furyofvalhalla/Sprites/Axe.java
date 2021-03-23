package net.davidbrowne.furyofvalhalla.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Screens.PlayScreen;

public class Axe extends InteractiveTileObject {
    private AssetManager manager;
    public Axe(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager = manager;
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(Game.AXE_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        screen.getManager().get("audio/sounds/bop.wav", Sound.class).play(screen.getGame().getSoundVolume());
        if(!screen.getWorld().isLocked())
            screen.getWorld().destroyBody(fixture.getBody());
        getObjectCell().setTile(null);
        //screen.getManager().get("audio/sounds/break.wav", Sound.class).play(screen.getGame().getSoundVolume());
        screen.getPlayer().setAxes(screen.getPlayer().getAxes()+5);

    }

    @Override
    public void onHeadHit() {


    }
}
