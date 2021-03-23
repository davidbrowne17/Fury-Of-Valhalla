package net.davidbrowne.furyofvalhalla.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Screens.PlayScreen;
import net.davidbrowne.furyofvalhalla.Sprites.AttackingEnemy;
import net.davidbrowne.furyofvalhalla.Sprites.Axe;
import net.davidbrowne.furyofvalhalla.Sprites.Box;
import net.davidbrowne.furyofvalhalla.Sprites.Brick;
import net.davidbrowne.furyofvalhalla.Sprites.Enemy;
import net.davidbrowne.furyofvalhalla.Sprites.Fireball;
import net.davidbrowne.furyofvalhalla.Sprites.Frank;
import net.davidbrowne.furyofvalhalla.Sprites.Horn;
import net.davidbrowne.furyofvalhalla.Sprites.Jotun;
import net.davidbrowne.furyofvalhalla.Sprites.LevelEnd;
import net.davidbrowne.furyofvalhalla.Sprites.Monk;
import net.davidbrowne.furyofvalhalla.Sprites.Nord;
import net.davidbrowne.furyofvalhalla.Sprites.Player;
import net.davidbrowne.furyofvalhalla.Sprites.Spike;

public class B2WorldCreator {
    private Array<AttackingEnemy> attackingEnemies;
    private Array<Jotun> jotunEnemies;
    private Array<Nord> nordEnemies;
    private Array<Frank> frankEnemies;
    private Array<Monk> monkEnemies;
    private Array<Fireball> fireballs;
    private Array<Enemy> enemies;
    private PlayScreen screen;

    public B2WorldCreator(PlayScreen screen){
        this.screen=screen;
        enemies = new Array<Enemy>();
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        AssetManager manager = screen.getManager();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //create ground
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX()+rect.getWidth()/2)/ Game.PPM),(rect.getY()+rect.getHeight()/2)/ Game.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / Game.PPM,rect.getHeight()/2 / Game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Horn(screen,object,manager);
        }
        //Axe
        for(MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Axe(screen,object,manager);
        }
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Box(screen,object,manager);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Brick(screen,object,manager);
        }
        //create Attacking Enemy
        attackingEnemies = new Array<AttackingEnemy>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            attackingEnemies.add(new AttackingEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //spawn player
        for(MapObject object : screen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            Player player = new Player(screen,(int)rect.getX(),(int)rect.getY());
            screen.setPlayer(player);
        }
        //finish level
        for(MapObject object : screen.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new LevelEnd(screen,object,manager);
        }
        //Jotun
        jotunEnemies = new Array<Jotun>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            jotunEnemies.add(new Jotun(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //Nord
        nordEnemies = new Array<Nord>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            nordEnemies.add(new Nord(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //Spikes
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Spike(screen,object,manager);
        }
        //Franks
        frankEnemies = new Array<Frank>();
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            frankEnemies.add(new Frank(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //Monks
        monkEnemies = new Array<Monk>();
        for(MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            monkEnemies.add(new Monk(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //Fireballs
        fireballs = new Array<Fireball>();
        for(MapObject object : map.getLayers().get(16).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            fireballs.add(new Fireball(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

    }
    public Array<Enemy> getEnemies() {
        if(enemies.isEmpty()){
            enemies.addAll(attackingEnemies);
            enemies.addAll(jotunEnemies);
            enemies.addAll(nordEnemies);
            enemies.addAll(frankEnemies);
            enemies.addAll(monkEnemies);
            enemies.addAll(fireballs);
        }
        return enemies;
    }
}
