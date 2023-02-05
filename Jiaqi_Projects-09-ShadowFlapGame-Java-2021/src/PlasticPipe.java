import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Plastic pipe.
 */
public class PlasticPipe extends PipeSet {
    private static final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");
    private final int PIPE_GAP = 168;
    private final int PIPE_SPEED = 5;
    private final double PIPE_LENGTH = 768;
    private final double TOP_PIPE_Y;
    private final double BOTTOM_PIPE_Y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private final int GAP_START_LEVEL_0 = randomPipeHeight();
    private final int GAP_START_LEVEL_1 = randomPipeHeightLevel1();
    private final int HIGH_GAP_START = 100;
    private final int MID_PIPE_START = 300;
    private final int LOW_PIPE_START = 500;


    /**
     * get y axis of the top pipe y axis and bottom y axis
     * different level has the different y axis
     * @param level tells which current level it is
     */
    public PlasticPipe(int level){
        if (level == 0) {
            TOP_PIPE_Y = GAP_START_LEVEL_0 - PIPE_LENGTH / 2;
            BOTTOM_PIPE_Y = GAP_START_LEVEL_0 + PIPE_GAP + PIPE_LENGTH / 2;
        } else {
            TOP_PIPE_Y = GAP_START_LEVEL_1 - PIPE_LENGTH / 2;
            BOTTOM_PIPE_Y = GAP_START_LEVEL_1 + PIPE_GAP + PIPE_LENGTH / 2;
        }
    }

    /**
     * y axis remains the same and x axis constantly reduces so pipe moves left
     */
    public void update(double timeSpeed) {
        renderPipe();
        pipeX -= (PIPE_SPEED * timeSpeed);
    }


    /**
     * @return the box of the top plastic pipe, will be used to detect collision
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
    }


    /**
     * @return the box of the bottom plastic pipe, will be used to detect collision
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }


    /**
     * @return a random number will be returned between 100, 300, 500 for level 0 only
     */
    public int randomPipeHeight() {
        int gapStart;
        int randomNum;

        randomNum = ThreadLocalRandom.current().nextInt(0, 3);
        if (randomNum == 0) {
            gapStart = HIGH_GAP_START;
        } else if (randomNum == 1) {
            gapStart = MID_PIPE_START;
        } else {
            gapStart = LOW_PIPE_START;
        }
        return gapStart;
    }

    /**
     * @return a random number between 100 and 500, for level 1 only
     */
    public int randomPipeHeightLevel1() {
        return ThreadLocalRandom.current().nextInt(HIGH_GAP_START, LOW_PIPE_START + 1);
    }

    /**
     * @return always return false since this class is plastic pipe, which will be called in shadowFlap
     */
    public boolean isPipeSteel() {
        return false;
    }


    /**
     * draw he pipe with current x and y aixs
     */
    public void renderPipe() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }
}

