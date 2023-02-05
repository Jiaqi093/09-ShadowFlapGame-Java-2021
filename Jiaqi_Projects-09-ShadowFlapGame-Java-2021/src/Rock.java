import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Rock extends Weapon{
    private final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private final double randNum = randomNum();
    private double rockY = randNum;
    private double rockX = Window.getWidth();
    private final int ROCK_SPEED = 5;
    private final int SHOOT_RANGE = 25;
    private int countShoot = 0;
    private boolean shoot = false;



    public Rock() {}


    /**
     * update the x axis changes by different situation
     * @param input whether 'S' is pressed
     * @param birdX after weapon collide with bird, bird x axis will not change until shoot
     * @param birdY after weapon collide with bird, weapon y axis will follow the movement of the bird y axis
     * @param birdWidth so that weapon can stick on bird's beak
     */
    public void update(Input input, double birdX, double birdY, double birdWidth, double timeSpeed) {
        // after pressing 'S'
        if (isPickedUp() && input.wasPressed(Keys.S)) {
            // tell shadowFlap that this weapon is shoot
            shoot = true;
            hasShoot();
        }

        // bird picked up weapon and not shooting yet
        if(isPickedUp() && !shoot) {
            rockX = birdX + birdWidth / 2;
            rockY = birdY;

            // weapon x axis changed after shoot
        } else if(shoot){
            rockX += ROCK_SPEED;

            // weapon moves from window's right to left
        } else {
            rockX -= (ROCK_SPEED * timeSpeed);
        }
        render();
    }


    /**
     * draw the rock weapon with current x and y axis
     */
    public void render() {ROCK_IMAGE.draw(rockX, rockY);}

    /**
     * check whether this item is a rock
     * @return rock class will tell shadowFlap that this item is a rock
     */
    public boolean isBomb() {return false;}

    /**
     * @return the box of the rock which will be used to detect collision
     */
    public Rectangle getWeaponBox() {return ROCK_IMAGE.getBoundingBoxAt(new Point(rockX, rockY)); }

    /**
     * @return the maximum shoot range of tock weapon
     */
    public int getShootRange() {return SHOOT_RANGE;}
}
