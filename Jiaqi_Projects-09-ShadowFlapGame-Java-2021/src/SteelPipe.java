import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public class SteelPipe extends PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final int PIPE_GAP = 168;
    private final int PIPE_SPEED = 5;
    private final double pipeLength = 768;
    private final double TOP_PIPE_Y;
    private final double BOTTOM_PIPE_Y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private final int GAP_START = randomPipeHeight();
    private int countFrame = 0;
    private int flameOnScreen = 30;
    private boolean flameOn = false;
    private final double TOP_FLAME_Y;
    private final double BOTTOM_FLAME_Y;
    private final double FLAME_Y = 39;
    private final int THIRTY_FRAMES = 30;
    private final int TWEENTY_FRAMES = 20;

    /**
     * get y axis of the pipe
     * get y axis of the flame
     */
    public SteelPipe() {
        TOP_PIPE_Y = GAP_START - pipeLength / 2;
        BOTTOM_PIPE_Y = GAP_START + PIPE_GAP + pipeLength / 2;

        TOP_FLAME_Y = GAP_START + FLAME_Y / 2;
        BOTTOM_FLAME_Y = GAP_START + PIPE_GAP - FLAME_Y / 2;
    }

    /**
     * frequently spawns the flame
     * flame x axis is changing same as the pipe
     */
    public void update(double timeSpeed){
        renderPipe();

        // flame is off and satisfy the condition, will render the flame
        if (countFrame % TWEENTY_FRAMES == 0 && !flameOn) {
            flameOn = true;
        }

        // flame is on, not render the flame
        if(flameOn && flameOnScreen > 0) {
            renderFlame();
            flameOnScreen--;
            if(flameOnScreen == 0) {
                flameOn = false;
                flameOnScreen = THIRTY_FRAMES;
            }
        }
        pipeX -= (PIPE_SPEED * timeSpeed);
        countFrame++;
    }


    /**
     * @return the top steel pipe box which will be used to detect collision
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
    }


    /**
     * @return the bottom steel pipe box which will be used to detect collision
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }


    /**
     * @return randomly get the height of the top pipe between 100 and 500
     */
    public int randomPipeHeight() {
        return ThreadLocalRandom.current().nextInt(100, 501);
    }

    /**
     * @return always return true since this class is steel pipe, which will be called in shadowFlap
     */
    public boolean isPipeSteel() {return true;}

    /**
     * draw the top and bottom steel pipe by current x and y axis
     */
    public void renderPipe() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }


    /**
     * draw the top and bottom flame by current x and y axis
     */
    public void renderFlame() {
        FLAME_IMAGE.draw(pipeX, TOP_FLAME_Y - 10);
        FLAME_IMAGE.draw(pipeX, BOTTOM_FLAME_Y + 10, ROTATOR);
    }


    /**
     * @return the top pipe flame box which will be used to detect collision
     */
    public Rectangle getTopFlameBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_FLAME_Y));
    }


    /**
     * @return the bottom pipe flame box which will be used to detect collision
     */
    public Rectangle getBottomFlameBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_FLAME_Y));
    }

    /**
     * @return whether the flame is on
     */
    public boolean getFlameOn() {return flameOn;}

}
