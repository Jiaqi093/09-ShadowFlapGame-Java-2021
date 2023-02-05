import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Weapon {

    private boolean pickedUp = false;
    private double y;
    private final int MIN_Y = 100;
    private final int MAX_Y = 500;
    private boolean shoot = false;

    /**
     * constructor
     */
    public Weapon() {}


    /**
     * abstract method, details explanation will be shown in subclass
     */
    public abstract void update(Input input, double birdX, double birdY, double birdWidth, double timeSpeed);

    public abstract void render();

    public abstract boolean isBomb();

    public abstract Rectangle getWeaponBox();

    /**
     * @return randomly get a number between 100 and 500, which will be the y axis of the weapon
     */
    public int randomNum(){
        return ThreadLocalRandom.current().nextInt(MIN_Y, MAX_Y + 1);
    }


    /**
     * if bird collide with weapon, pickedUp will change to true
      */
    public boolean hasPickedUp(boolean withWeapon) {
        return pickedUp = withWeapon;
    }


    /**
     * check whether the bird picked up the weapon
     */
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * if S is pressed, then will return true to change the shoot state
     */
    public boolean hasShoot() {
        return shoot = true;
    }

    /**
     * check whether weapon is shoot or not
     */
    public boolean isShoot() {
        return shoot;
    }

    /**
     * each weapon has different shooting range
     */
    public abstract int getShootRange();
}
