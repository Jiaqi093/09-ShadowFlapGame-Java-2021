import bagel.util.Rectangle;


public abstract class PipeSet {

    private boolean passPipe = false;


    /**
     * constructor
     */
    public PipeSet(){}

    /**
     * abstract method, details explanation of methods will be shown in sub class
     */

    public abstract void update(double timeSpeed);

    public abstract Rectangle getTopBox();

    public abstract Rectangle getBottomBox();

    public abstract int randomPipeHeight();

    public abstract boolean isPipeSteel();

    public abstract void renderPipe();

    /**
     * check if the bird has passed the pipe
     * @return boolean
     */
    public boolean getPassPipe() {return this.passPipe;}

    /**
     * if the bird passed pipe, will change the state of passPipe from false to true
     */
    public void birdPassPipe() {passPipe = true;}



}
