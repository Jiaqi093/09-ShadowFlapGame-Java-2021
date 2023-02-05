import bagel.Image;

public class LifeBar {
    private final int level_0_Heart = 3;
    private final int level_1_Heart = 6;
    private final int WIDTH = 40;
    private final int X = 100;
    private final int Y = 15;
    private final Image FULL_LIFE = new Image("res/level/fullLife.png");
    private final Image NO_LIFE = new Image("res/level/noLife.png");
    private final int DIFFERENCE = 50;


    public LifeBar() {}

    /**
     * @return the x axis of the first life bar
     */
    public int getX() {return X;}

    /**
     * @return the y axis of all the life bar
     */
    public int getY() {return Y;}


    /**
     * @return 3 life bar for level 0
     */
    public int getLevel0Heart(){return level_0_Heart;}

    /**
     * @return 6 life bar for level 1
     */
    public int getLevel1Heart(){return level_1_Heart;}

    /**
     * draw full life bar
     * @param x full life bar x axis
     * @param y full life bar y axis
     */
    public void renderFullLife(int x, int y) {FULL_LIFE.draw(x, y);}

    /**
     * draw empty life bar
     * @param x empty life bar x axis
     * @param y empty life bar y axis
     */
    public void renderNoLife(int x, int y) {NO_LIFE.draw(x, y);}

    /**
     * draw full life bar first, if lose one heart number, draw one empty life instead at the end of life bar
     * @param heartNum this tells that how many heart left
     * @param level tells what the current level it is
     */
    public void drawLifeBar(int heartNum, int level) {

        // create the location for the first heart
        int location = getX() + WIDTH / 2;

        // draw full life bar
        for(int i=0; i<heartNum; i++) {
            renderFullLife(location, getY());
            location += DIFFERENCE;
        }

        // draw no life bar
        int maxHeartNum;
        if (level == 0) {
            maxHeartNum = level_0_Heart;
        } else {
            maxHeartNum = level_1_Heart;
        }

        // distance between each heart
        for (int i = 0; i < maxHeartNum - heartNum; i++) {
            renderNoLife(location, getY());
            location += DIFFERENCE;
        }
    }
}
