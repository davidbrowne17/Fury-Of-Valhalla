package net.davidbrowne.furyofvalhalla.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import net.davidbrowne.furyofvalhalla.Game;
import net.davidbrowne.furyofvalhalla.Items.ItemDef;
import net.davidbrowne.furyofvalhalla.Screens.PlayScreen;

public class Player extends Sprite {
    public enum State {FALLING,JUMPING,RUNNING,STANDING,ATTACKING,DEAD}
    public State currentState,previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStanding;
    private Animation<TextureRegion> playerRun,playerJump,playerStand,playerAttack;
    private float stateTimer;
    private Boolean runningRight;
    private boolean isDead=false;
    private PlayScreen screen;
    private int axes=0;
    private int spawnX,spawnY;
    private boolean attacking=false,canFire=true,firing=false;
    private int score=0;
    private Fixture fix;
    private FixtureDef attackdef = new FixtureDef();
    public Player(PlayScreen screen,int spawnX,int spawnY){
        super(screen.getAtlas().findRegion("viking_walk"));
        this.screen=screen;
        this.spawnX=spawnX;
        this.spawnY=spawnY;
        runningRight = true;
        world=screen.getWorld();
        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.clear();
        frames.add(screen.getAtlas().findRegion("viking_walk"));
        frames.add(screen.getAtlas().findRegion("viking_walk_1"));
        frames.add(screen.getAtlas().findRegion("viking_walk_2"));
        playerRun = new Animation(0.1f,frames);
        frames.clear();
        frames.add(screen.getAtlas().findRegion("viking_attack1"));
        frames.add(screen.getAtlas().findRegion("viking_attack2"));
        frames.add(screen.getAtlas().findRegion("viking_attack3"));
        frames.add(screen.getAtlas().findRegion("viking_attack4"));
        playerAttack = new Animation(0.1f,frames);
        frames.clear();
        frames.add(screen.getAtlas().findRegion("viking_jump_1"));
        frames.add(screen.getAtlas().findRegion("viking_jump_2"));
        playerJump = new Animation(0.1f,frames);
        frames.clear();
        frames.add(screen.getAtlas().findRegion("viking"));
        playerStand = new Animation(0.1f,frames);
        definePlayer();
        playerStanding = new TextureRegion(screen.getAtlas().findRegion("viking"));
        setBounds(0,0,16 / Game.PPM,16 / Game.PPM);
        setRegion(playerStanding);
    }

    public Player() {
    }

    public void jump(){
        b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x,4f));
        currentState = State.JUMPING;
    }

    public Boolean getRunningRight() {
        return runningRight;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void attack(){
        if(!attacking&& currentState!=State.DEAD) {
            attacking = true;
            Timer timer = new Timer();
            currentState = State.ATTACKING;
            Timer.Task task = timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    currentState = State.STANDING;
                }
            }, 0.5f);
            fix = createAttack();
            if (isAttacking() == true) {
                Timer.Task task1 = timer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {

                        while (b2body.getFixtureList().size > 1) {
                            b2body.destroyFixture(b2body.getFixtureList().pop());
                        }
                        attacking = false;
                    }
                }, 0.5f);
            }
        }
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x  - getWidth()/2 ,b2body.getPosition().y - getHeight()/2 );
        setRegion(getFrame(dt));
        b2body.setAwake(true);
    }

    public void fire(){
        if(canFire) {
            if (axes > 0) {
                canFire=false;
                screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x + (!isFlipX() ? -1 / Game.PPM : -1 / Game.PPM), b2body.getPosition().y + (!isFlipX() ? 1 / Game.PPM : 1 / Game.PPM)), net.davidbrowne.furyofvalhalla.Items.Bullet.class));
                axes--;
            }
        }
    }


    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case ATTACKING:
                region = playerAttack.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
                region = playerStand.getKeyFrame(stateTimer,true);
                break;
            default:
                region = playerStand.getKeyFrame(stateTimer,true);
                break;
        }
        if( !runningRight && !region.isFlipX()){
            region.flip(true, false);
        }
        else if(runningRight && region.isFlipX()){
            region.flip(true, false);
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void setRunningRight(Boolean runningRight) {
        this.runningRight = runningRight;
    }

    public State getState(){
        if(isDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y <0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x !=0 & !attacking)
            return State.RUNNING;
        else if(attacking)
            return State.ATTACKING;
        else
            return State.STANDING;
    }
    public Fixture createAttack(){
        if(!isFlipX())
            b2body.applyLinearImpulse(new Vector2(1.0f,0f),b2body.getWorldCenter(),true);
        else
            b2body.applyLinearImpulse(new Vector2(-1.0f,0f),b2body.getWorldCenter(),true);
        b2body.setAwake(true);
        //create line for head collision detection
        EdgeShape head = new EdgeShape();
        if(!isFlipX())
            head.set(new Vector2(-3/Game.PPM,0 / Game.PPM),new Vector2(9/Game.PPM,0 / Game.PPM));
        else
            head.set(new Vector2(-9/Game.PPM,0 / Game.PPM),new Vector2(-3/Game.PPM,0 / Game.PPM));
        attackdef.shape = head;
        attackdef.isSensor = false;
        attackdef.density=1;
        attackdef.filter.categoryBits = Game.ATTACK_BIT;
        attackdef.filter.maskBits = Game.ENEMY_BIT |
                        Game.BOX_BIT;
        Fixture fix1 = b2body.createFixture(attackdef);
        fix1.setUserData("attack");
        screen.getManager().get("audio/sounds/swing.mp3", Sound.class).play(screen.getGame().getVolume());
        head.dispose();
        return fix1;
    }



    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX/ Game.PPM,spawnY/ Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ Game.PPM);
        fdef.filter.categoryBits = Game.PLAYER_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT
                | Game.HORN_BIT
                | Game.BRICK_BIT
                | Game.SPIKE_BIT
                | Game.BOX_BIT
                | Game.AXE_BIT
                | Game.ITEM_BIT
                | Game.ENEMY_HEAD_BIT
                | Game.LEVEL_END_BIT
                | Game.NPC_INTERACT_BIT
                | Game.ENEMY_BIT;
        fdef.shape = shape;
        shape.dispose();
        b2body.createFixture(fdef);

    }

    public int getAxes() {
        return axes;
    }

    public void setAxes(int axes) {
        this.axes = axes;
    }

    public void die() {
        if (!isDead) {
            screen.getManager().get("audio/music/music1.wav", Music.class).stop();
            screen.getManager().get("audio/sounds/splat.wav", Sound.class).play(screen.getGame().getSoundVolume());
            isDead=true;
            Filter filter = new Filter();
            filter.maskBits = Game.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            currentState=State.DEAD;
        }
    }
    public void burn() {
        if (!isDead) {
            screen.getManager().get("audio/music/music1.wav", Music.class).stop();
            screen.getManager().get("audio/sounds/burn.wav", Sound.class).play(screen.getGame().getSoundVolume());
            isDead=true;
            Filter filter = new Filter();
            filter.maskBits = Game.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            currentState=State.DEAD;
        }
    }

}
