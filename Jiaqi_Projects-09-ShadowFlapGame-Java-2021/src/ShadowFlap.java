import bagel.*;
import bagel.util.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 2, 2021
 * used Betty's assignment 1 solution as based code
 * @author Jiaqi Zhuang student number: 1122155
 */
public class ShadowFlap extends AbstractGame {

    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final Bird bird;
    private final LifeBar lifeBar;
    private int score;
    private boolean gameOn;
    private boolean collision;
    private boolean win;
    private int frameCount = 0;
    private final double HUNDRED_FRAMES = 100;
    private ArrayList<PipeSet> pipeSetArrayList;
    private final ArrayList<Weapon> weaponArrayList;
    private int level = 0;
    private int countLevelUp = 0;
    private int level_0_Heart;
    private int level_1_Heart;
    private boolean detectedCollision = false;
    private boolean withWeapon = false;
    private int countRange = 0;
    private int timeScale = 1;
    private double timeSpeed = 1;


    /**
     * Instantiates a new Shadow flap.
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        lifeBar = new LifeBar();
        level_0_Heart = lifeBar.getLevel0Heart();   //life = 3
        level_1_Heart = lifeBar.getLevel1Heart();   //life = 6
        pipeSetArrayList = new ArrayList<>();   // arraylist used to store each pipes on the window
        weaponArrayList = new ArrayList<>();
        score = 0;
        gameOn = false;
        collision = false;
        win = false;
    }

    /**
     * The entry point for the program.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update about game not start, game start, game level up, game lose, game win, exit game etc.
     * allows weapons, pipes to be created and they will move from right to left
     * create bird and background also, get 10 points would level up, and another 30 points to win game
     * time controls the game speed
     * weapon can be picked up by the bird and shoot to expolde the pipe
     * lose life when out of window or bird collide with pipe
     */
    @Override
    public void update(Input input) {

        timeScale(input);

        renderBackground(level, countLevelUp);

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started for both level
        if (!gameOn && level == 0) {
            renderInstructionScreen(input);
        }

        if (!gameOn && level == 1 && countLevelUp == -1) {
            String SHOOT_MSG = "PRESS 'S' TO SHOOT";
            renderInstructionScreen(input);
            FONT.drawString(SHOOT_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(SHOOT_MSG) / 2.0)),
                    (Window.getHeight() / 2.0 + 68 + (FONT_SIZE / 2.0)));
            frameCount = 0;
            pipeSetArrayList = new ArrayList<>();
        }


        // message level up is shown
        if (!gameOn && level == 1 && countLevelUp >= 0) {
            String LEVEL_UP_MSG = "LEVEL-UP!";
            FONT.drawString(LEVEL_UP_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(LEVEL_UP_MSG) / 2.0)),
                    (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)));
            countLevelUp++;
        }
        // "press space to start" message after "level up" message
        if (level == 1 && countLevelUp == 150) {
            countLevelUp = -1;
        }


        // game over
        if (level_0_Heart == 0 || level_1_Heart == 0) {
            renderGameOverScreen();
        }

        // out of bound, will do respawn (original location with no speed)
        if(birdOutOfBound()) {
            loseLife();
            bird.setY();
            bird.setVelocityY();
        }

        // collision would lose life bar
        if (collision) {
            loseLife();
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && level_0_Heart != 0 && level_1_Heart != 0 && !win) {

            // create the bird and life bar
            drawLifeBar();
            bird.update(input, level);
            Rectangle birdBox = bird.getBox();

            // create pipe and update pipe
            drawPipe();
            updatePipe(birdBox);

            // create weapon and update weapon
            createWeapon();

            // create weapon and detect pick up, detect collision
            pickUpWeapon(input, birdBox);
            weaponShootOutOfRange();
            weaponCollidePipe();

            updateScore();
        }
        frameCount++;
    }


    /**
     * simply draw the background of level 0 and level 1
     *
     * @param level        tells the current game level, level 0 and level 1 only
     * @param countLevelUp the game will not immediately start at level 1, until countLevelUp to be set -1
     */
    public void renderBackground(int level, int countLevelUp) {
        Image backgroundImage;
        if (level == 0 || level == 1 && countLevelUp >= 0) {
            backgroundImage = new Image("res/level-0/background.png");
            backgroundImage.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        } else if (level == 1 && countLevelUp == -1){
            backgroundImage = new Image("res/level-1/background.png");
            backgroundImage.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }
    }

    /**
     * lose one life method
     */
    public void loseLife() {
        if (level == 0) {
            level_0_Heart -= 1;
        } else if (level == 1) {
            level_1_Heart -= 1;
        }
        collision = false;
    }

    /**
     * drawing full life and empty life
     */
    public void drawLifeBar(){
        if (level == 0) {
            lifeBar.drawLifeBar(level_0_Heart, level);
        }
        if (level == 1) {
            lifeBar.drawLifeBar(level_1_Heart, level);
        }
    }

    /**
     * create plastic pipe for level 0
     * create either plastic pipe or steel pipe for level 1
     */
    public void drawPipe() {
        // create pipe array to store plastic pipes for level 0 only
        if (frameCount % (Math.round(HUNDRED_FRAMES / timeSpeed)) == 0 && level == 0) {
            pipeSetArrayList.add(new PlasticPipe(level));
        }

        // create pipe array to store both plastic pipes and steel pipes for level 1 only
        if (frameCount % (Math.round(HUNDRED_FRAMES / timeSpeed)) == 0 && level == 1) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
            if (randomNum == 0) {
                pipeSetArrayList.add(new PlasticPipe(level));
            } else if (randomNum == 1) {
                pipeSetArrayList.add(new SteelPipe());
            }
        }
    }

    /**
     * update pipe in every frame, so constantly moves left
     *
     * @param birdBox check intersection with the pipe lose life if bird intersect with pipe, and the pipe will be removed immediately
     */
    public void updatePipe(Rectangle birdBox) {

        int pipeIndex = 0;
        // update every pipe in the arraylist
        for (PipeSet item : pipeSetArrayList) {
            item.update(timeSpeed);

            Rectangle topPipeBox = item.getTopBox();
            Rectangle bottomPipeBox = item.getBottomBox();

            // detect that the bird is collided with flame
            if (item.isPipeSteel() && ((SteelPipe) item).getFlameOn()) {
                Rectangle topFlameBox = ((SteelPipe) item).getTopFlameBox();
                Rectangle bottomFlameBox = ((SteelPipe) item).getBottomFlameBox();
                if (detectFlameCollision(birdBox, topFlameBox, bottomFlameBox)) {
                    collision = detectFlameCollision(birdBox, topFlameBox, bottomFlameBox);
                    detectedCollision = true;
                    break;
                }
            }

            // check whether the bird would collide with any pipes from the arraylist.
            if (detectCollision(birdBox, topPipeBox, bottomPipeBox)) {
                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
                detectedCollision = true;
                break;
            }
            pipeIndex++;
        }
        // delete the pipe which has been collided
        if(detectedCollision) {
            pipeSetArrayList.remove(pipeIndex);
            detectedCollision = false;
        }
    }


    /**
     * randomly create weapon(either bomb or rock) in level 1
     */
    public void createWeapon() {

        if ((frameCount + Math.round(50 / timeSpeed)) %
                (2 * Math.round(HUNDRED_FRAMES / timeSpeed)) == 0 && level == 1) {
            int randomNumber = ThreadLocalRandom.current().nextInt(0, 2);
            if (randomNumber == 0) {
                weaponArrayList.add(new Rock());
            } else if (randomNumber == 1) {
                weaponArrayList.add(new Bomb());
            }
        }
    }


    /**
     * Pick up weapon.
     *
     * @param input   if 'S' is pressed, bird would shoot the weapon
     * @param birdBox used to detect whether bird collide the weapon, so bird will pick up weapon
     */
    public void pickUpWeapon(Input input, Rectangle birdBox) {
        // update weapon and detect whether pick up weapon
        for (Weapon item : weaponArrayList) {
            item.update(input, bird.getX(), bird.getY(), bird.getWidth(), timeSpeed);

            // bird collide with weapon will pick up the weapon
            Rectangle weaponBox = item.getWeaponBox();
            if (detectWeaponCollision(birdBox, weaponBox) && !withWeapon) {
                withWeapon = true;
                item.hasPickedUp(true);
            }
        }
    }


    /**
     * different weapons has different shooting range
     * if the shooting range is reached, the weapon would automatically being removed
     */
    public void weaponShootOutOfRange() {
        int weaponIndex = 0;
        boolean removeWeapon = false;
        for (Weapon item : weaponArrayList) {

            // press s to shoot weapon
            if (item.isShoot()) {
                withWeapon = false; // tells the shadowFlap that bird is currently not holding the weapon
                item.hasPickedUp(false); // weapon is not picked up, will be used in weapon class
                countRange += 1;

                // make sure the weapon shooting range is different
                if (countRange == item.getShootRange()) { // shoot range is reached
                    countRange = 0;
                    removeWeapon = true;
                    break;
                }
            }
            weaponIndex++;
        }
        if(removeWeapon) {
            weaponArrayList.remove(weaponIndex);
        }
    }


    /**
     * first case, bomb collide with any types of pipes, bomb and pipe would removed
     * second case, rock collide with plastic pipe, so rock and plastic pipe would be removed
     * third case, rock collide with steel pipe, only rock would be removed
     */
    public void weaponCollidePipe() {
        int weaponIndex;
        int pipeIndex = 0;
        boolean removeWeapon = false;

        for (PipeSet pipe : pipeSetArrayList) {
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();

            weaponIndex = 0;
            for (Weapon item : weaponArrayList) {
                Rectangle weaponBox = item.getWeaponBox();

                // when bomb collide with any pipe or rock collide with plastic pipe, remove both
                if (item.isBomb() || !item.isBomb() && !pipe.isPipeSteel()) {
                    if(item.isShoot() && detectCollision(weaponBox, topPipeBox, bottomPipeBox)) {
                        countRange = 0;
                        detectedCollision = true;
                        removeWeapon = true;
                        score += 1;
                        break;
                    }
                }

                // when rock collide with steel pipe, only remove rock
                if (!item.isBomb() && pipe.isPipeSteel()) {
                    if(item.isShoot() && detectCollision(weaponBox, topPipeBox, bottomPipeBox)) {
                        countRange = 0;
                        removeWeapon = true;
                        break;
                    }
                }
                weaponIndex++;
            }
            // remove weapon if collide with any pipe
            if(removeWeapon) {
                weaponArrayList.remove(weaponIndex);
                break;
            }

            pipeIndex++;
        }
        // make sure remove the pipe if collide with weapon
        if(detectedCollision) {
            pipeSetArrayList.remove(pipeIndex);
            detectedCollision = false;
        }
    }


    /**
     * press L to increases the weapon and pipes speed moving from right to left
     * press K to decreases the speed
     *
     * @param input to see whether L or K being pressed
     */
    public void timeScale(Input input) {
        double SPEED_RATIO = 1.5;

        if (input.wasPressed(Keys.L) && timeScale < 5) {
            timeScale += 1;
            timeSpeed *= SPEED_RATIO;
        }
        if (input.wasPressed(Keys.K) && timeScale > 1) {
            timeScale -= 1;
            timeSpeed /= SPEED_RATIO;
        }
    }

    /**
     * Bird out of bound boolean.
     *
     * @return either bird is out of the window or not, gives the boolean values
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }


    /**
     * game is not started yet, render instructions
     *
     * @param input make sure press space would start the game
     */
    public void renderInstructionScreen(Input input) {
        String INSTRUCTION_MSG = "PRESS SPACE TO START";
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }


    /**
     * game is finished is there is no (full) life bar left
     */
    public void renderGameOverScreen() {
        String FINAL_SCORE_MSG = "FINAL SCORE: ";
        String GAME_OVER_MSG = "GAME OVER!";
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /**
     * game is finished as the maximum score is reached
     */
    public void renderWinScreen() {
        String CONGRATS_MSG = "CONGRATULATIONS!";
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
    }


    /**
     * 1.bird collide with pipe
     * 2.bird collide with flame
     * 3.weapon collide with pipe
     *
     * @param currBox   can be bird box, or can be weapon box
     * @param topBox    can be top pipe box, or can be top flame box
     * @param bottomBox can be bottom pipe box, or can be bottom flame box
     * @return boolean variable, either detected collision or not
     */
    public boolean detectCollision(Rectangle currBox, Rectangle topBox, Rectangle bottomBox) {
        // check for collision
        return currBox.intersects(topBox) ||
                currBox.intersects(bottomBox);
    }


    /**
     * bird collide with flame
     *
     * @param birdBox        bird
     * @param topFlameBox    top pipe flame
     * @param bottomFlameBox bottom pipe flame
     * @return whether bird collide with flame, and return boolean variable
     */
    public boolean detectFlameCollision(Rectangle birdBox, Rectangle topFlameBox, Rectangle bottomFlameBox) {
        // check for collision
        return birdBox.intersects(topFlameBox) ||
                birdBox.intersects(bottomFlameBox);
    }


    /**
     * bird collide with weapon
     *
     * @param birdBox   bird
     * @param weaponBox weapon
     * @return boolean variable, whether bird pick up weapon
     */
    public boolean detectWeaponCollision(Rectangle birdBox, Rectangle weaponBox) {
        // check for collision
        return birdBox.intersects(weaponBox);
    }


    /**
     * change the score if a bird pass a pipe or a bird use weapon to destroy the pipe
     * 10 points to win level 0 and level up
     * 30 points to win level 1 and game finish.
     */
    public void updateScore() {
        int FINAL_SCORE_LEVEL_0 = 10;
        int FINAL_SCORE_LEVEL_1 = 30;
        String SCORE_MSG = "SCORE: ";

        for (PipeSet item: pipeSetArrayList) {
            if (bird.getX() > item.getTopBox().right() && !item.getPassPipe()) {
                // when the bird pass one pipe, this pipe will be set to "passed pipe" and score plus one
                item.birdPassPipe();
                score += 1;
            }

            String scoreMsg = SCORE_MSG + score;
            FONT.drawString(scoreMsg, 100, 100);

            // detect level up
            if (score == FINAL_SCORE_LEVEL_0 && level == 0) {
                level = 1;
                score = 0;
                bird.setY();
                gameOn = false;
            }

            // win game
            if (level == 1 && score == FINAL_SCORE_LEVEL_1) {
                win = true;
                renderWinScreen();
            }
        }
    }
}
