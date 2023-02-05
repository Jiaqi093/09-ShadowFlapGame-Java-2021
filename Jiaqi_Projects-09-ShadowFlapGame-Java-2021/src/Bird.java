import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

public class Bird {
    private final Image WING_DOWN_LEVEL_0 = new Image("res/level-0/birdWingDown.png");
    private final Image WING_UP_LEVEL_0 = new Image("res/level-0/birdWingUp.png");
    private final Image WING_DOWN_LEVEL_1 = new Image("res/level-1/birdWingDown.png");
    private final Image WING_UP_LEVEL_1 = new Image("res/level-1/birdWingUp.png");
    private final double width = WING_DOWN_LEVEL_0.getWidth();
    private final double X = 200;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;

    public Bird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_LEVEL_0.getBoundingBoxAt(new Point(X, y));
    }

    /**
     * Every time press the space, the bird will move up
     * Bird will fall down if not doing anything
     * level 0  and level 1 would render the different pictures
     */
    public void update(Input input, int level) {
        double FLY_SIZE = 6;
        double FALL_SIZE = 0.4;

        frameCount += 1;

        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;

            if (level == 0) {
                WING_DOWN_LEVEL_0.draw(X, y);
            } else {
                WING_DOWN_LEVEL_1.draw(X, y);
            }

        }
        // fall down with accelerating speed if not pressed space until max speed
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                if(level == 0) {
                    WING_UP_LEVEL_0.draw(X, y);
                    boundingBox = WING_UP_LEVEL_0.getBoundingBoxAt(new Point(X, y));
                } else {
                    WING_UP_LEVEL_1.draw(X, y);
                    boundingBox = WING_UP_LEVEL_1.getBoundingBoxAt(new Point(X, y));
                }
            }
            else {
                if(level == 0) {
                    WING_DOWN_LEVEL_0.draw(X, y);
                    boundingBox = WING_DOWN_LEVEL_0.getBoundingBoxAt(new Point(X, y));
                } else {
                    WING_DOWN_LEVEL_1.draw(X, y);
                    boundingBox = WING_DOWN_LEVEL_1.getBoundingBoxAt(new Point(X, y));
                }
            }
        }
        y += yVelocity;
    }

    /**
     * @return y axis of the bird
     */
    public double getY() { return y; }

    /**
     * @return initial y axis of the bird
     */
    public double setY() {return y = INITIAL_Y;}

    /**
     * set y velocity to zero
     * @return y velocity
     */
    public double setVelocityY() {return yVelocity = 0;}

    /**
     * @return x axis of the bird
     */
    public double getX() { return X; }

    /**
     * @return the box of the bird picture will be used for detect collision
     */
    public Rectangle getBox() { return boundingBox; }

    /**
     * return the width of the bird
     */
    public double getWidth() {return width;}

}