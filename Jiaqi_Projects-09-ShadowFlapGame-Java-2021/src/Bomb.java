import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import bagel.*;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bomb extends Weapon{
    private final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private final double randNum = randomNum();
    private double bombY = randNum;
    private double bombX = Window.getWidth();
    private final int BOMB_SPEED = 5;
    private final int SHOOT_RANGE = 50;
    private int countShoot = 0;
    private boolean shoot = false;


    public Bomb() {}


    /**
     * @param input whether 'S' is pressed
     * @param birdX after weapon collide with bird, bird x axis will not change until shoot
     * @param birdY after weapon collide with bird, weapon y axis will follow the movement of the bird y axis
     * @param birdWidth so that weapon can stick on bird's beak
     */
    public void update(Input input, double birdX, double birdY, double birdWidth, double timeSpeed) {
        // after pressing 'S'
        if (isPickedUp() && input.wasPressed(Keys.S)) {
            shoot = true;
            // tell shadowFlap that this weapon is shoot
            hasShoot();
        }

        // bird picked up weapon and not shooting yet
        if(isPickedUp() && !shoot) {
            bombX = birdX + birdWidth / 2;
            bombY = birdY;

            // weapon x axis changed after shoot
        } else if(shoot){
            bombX += BOMB_SPEED;

            // weapon moves from window's right to left
        } else {
            bombX -= (BOMB_SPEED * timeSpeed);
        }
        render();
    }


    /**
     * draw the bomb picture with current x and y axis
     */
    public void render() {BOMB_IMAGE.draw(bombX, bombY);}

    /**
     * check whether this item is a bomb
     * @return bomb class will tell shadowFlap that this item is a bomb
     */
    public boolean isBomb() {return true;}

    /**
     * @return get the box of the bomb picture, will be used to detect collision
     */
    public Rectangle getWeaponBox() {return BOMB_IMAGE.getBoundingBoxAt(new Point(bombX, bombY)); }

    /**
     * @return get shooting range of each weapon
     */
    public int getShootRange() {return SHOOT_RANGE;}
}

