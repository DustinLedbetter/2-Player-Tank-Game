/*
 * Copyright (C) 2017 PC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */



/*
 * Author: Dustin Ledbetter
 * Date: 3/25/2017-?/?/2017
 * Purpose: A game for 2 players to play with tanks on one pc together.
 * This is to be updated version to my "shooter" I made several years ago. 
 * It is my way of refreshing myself with java and of making a better game.
 * This will be version 2 out of 3. 2 will be new mechanics.
 * 3 will be enhanced and added controls for tanks and better background
 * possible version 4 with multiple levels?
 */
package TanksGame;



//imports
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;

/**
 *Class: TanksGame
 * "extends Canvas" (allows for buffering of graphics on JFrame)
 * "implements Runnable" (allows us to not use full CPU managing FPS)
 * "KeyListener" (catches user movements from keyboard)
 */
public class TanksGame extends Canvas implements Runnable, KeyListener {

    //Object of TanksGame in virtual machine
    //will give access to the object throughout all classes
    //needed to have access to players in other classes for collision testing 
    private static TanksGame INSTANCE;
    //needed when using Canvas
    private static final long serialVersionUID = 1L;
    //sets game screen width
    private static final int WIDTH = 600;
    //sets game screen height
    private static final int HEIGHT = 600;
    //creates thread for use in 'game loop'
    private Thread thread;
    //creates buffer element from Canvas
    private BufferStrategy bs = null;
    //creates graphics element from Graphics
    private Graphics graphics = null;
    //creates toggle for starting and closing game
    private boolean running = false;
    //create players
    private Player player1;
    private Player player2;
    //create list to hold bullet objects to use
    //CopyOnWriteArrayList is used because of concurrency issues 
    //when adding and removing bullets
    private List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();
    //used to test if game is over or not
    private boolean gameOver = false;
    //used to tell who is winner to players
    private int winner;
    
    
    
    //constructor for JFrame
    public TanksGame(){
        
        //creating our frame
        JFrame frame = new JFrame ("Tank Game");
        //setting the size of the frame
        frame.setSize(WIDTH, HEIGHT);
        //set default option to close game on exit
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ensure game size parameters are not changed
        frame.setResizable(false);
        //setting up objects for game
        new BasicCache();
        //create player objects
        player1 = new Player(10, 200, 20, 90, BasicCache.player1);
        player2 = new Player(565, 200, 20, 90, BasicCache.player2);
        /* adding the canvas to frame so we can 
         * then update canvas and it will update in frame
         * throughout gameplay*/
        frame.add(this);
        //adding TanksGame to thread to use in 'game loop'
        thread = new Thread(this);
        //ensuring frame is visible
        frame.setVisible(true);
        //giving focus for events called
        addKeyListener(this);
        //requests focus to 'this' window so that the keyboard events are sent to 'this' instance/window. 
        //Without this you would have to manually click on the Canvas (the game) for the keyboard events to register.
        this.requestFocus();
    }

    
    
    //buffer for game
    public void paint(Graphics g){
        //make sure buffer is empty at game runtime
        if(bs == null){
            //using BufferStrategy from Canvas
            createBufferStrategy(2);
            //putting it into our buffer
            bs = getBufferStrategy();
            //setting graphics
            graphics = bs.getDrawGraphics();
            //dont want to start looping til have graphics in bufferstrategy
            //will create issues if try to loop and render before graphics exist
            thread.start();
            //starts game
            running = true;
        }
    }
    
    
    
    //updates as game plays
    public void update(){
        //update game as long as gameOver boolean is not true
        if (!gameOver){
            //update players
            player1.update();
            player2.update();
            //draws every bullet that is added to the bullet array list
            for(Bullet bullet : bullets){
                bullet.update();
            }
        }
        //check to see if either player has won the game
        if(player1.health <= 0){
            //player 2 has won
            gameOver = true;
            winner = 2;
            
        }
        if(player2.health <= 0){
            //player 1 has won
            gameOver = true;
            winner = 1;
        }
    }
    
    
    
    //where all game image components will be created
    public void render(){
        //ensure screen gets cleared of what was there before
        graphics.clearRect(0, 0, WIDTH, HEIGHT);
        //set screen to be black (version 3 will be better background )
        graphics.setColor(Color.darkGray);
        //rectangle that fills game screen
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        //players 1 and 2 
        player1.draw(graphics);
        player2.draw(graphics);
        //add line under health and bullet counters
        graphics.setColor(Color.gray);
        graphics.drawLine(0, 16, 600, 16);
        //display health of players
        graphics.setColor(Color.red);
        graphics.drawString("P1 Health: " + player1.health, 10, 15);
        graphics.drawString("P2 Health: " + player2.health, 513, 15);
        //displays number of bullets on the screen at the moment
        //by showing number of bullets in bullet list array
        graphics.drawString("Bullets: " + bullets.size(), 275, 15);
        //draws every bullet that is added to the bullet array list
        for(Bullet bullet : bullets){
            bullet.draw(graphics);
        }
        //what will be drawn when game over has been reached
        if(gameOver){
            graphics.setColor(Color.ORANGE);
            graphics.drawString("Game Over", 270, 50);
            graphics.drawString("Player " + winner + " has won!", 252, 65);
            graphics.drawString("Press \"R\" to restart game or press \"ESCAPE\" to close game ", 135, 80);
        }
    }
    
    
    
    //'The game' repeat this over and over as game plays
    @Override 
    public void run() {
        //game loop
        while(running){
            //calls update (unfinished atm)
            update();
            //call render to draw all of game content in buffer for graphics and then show it
            render();
            //takes next buffer in list to be shown and shows it and then puts it into another
            //buffer in the background and swaps them out the next time bs.show is called
            bs.show();
            //slowing down so not full use of cpu and still smooth, but not quite 60 fps just yet. (to be continued)
            Thread.currentThread();
            try{
                //sleep for 10 milliseconds
                Thread.sleep(10);
            } catch(InterruptedException e){
                
            }
        }
    }
    
    
    
    //main
    public static void main(String[] args){
        //calling our game when run program
        //adding to INSTANCE so it isn't null at start of game
        INSTANCE = new TanksGame();
    } 
    
    
    
    //listener actions for player 1 and 2 if keys pressed
    @Override
    public void keyPressed(KeyEvent e) {
        //allow game to continue unless game over boolean is true
        if(!gameOver){
           //player 1 up and down pressed listener
           if(e.getKeyCode() == KeyEvent.VK_W){
               player1.up = true;
           } else if(e.getKeyCode() == KeyEvent.VK_S){
               player1.down = true;
           }
           //player 2 up and down pressed listener
           if(e.getKeyCode() == KeyEvent.VK_UP){
               player2.up = true;
           } else if(e.getKeyCode() == KeyEvent.VK_DOWN){
               player2.down = true;
           } 
       } else{
            //reset game
            if(e.getKeyCode() == KeyEvent.VK_R){
                resetGame();
            //close game    
            } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(1);
            }
            
       }
        
    }

    
    
    //listener actions for player 1 and 2 if keys released
    @Override
    public void keyReleased(KeyEvent e) {
        //player 1 up and down released listener
        if(e.getKeyCode() == KeyEvent.VK_W){
            player1.up = false;
        } else if(e.getKeyCode() == KeyEvent.VK_S){
            player1.down = false;
        }
        //player 2 up and down released listener
        if(e.getKeyCode() == KeyEvent.VK_UP){
            player2.up = false;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            player2.down = false;
        }
        
        //allow players to shoot bullets unless gameOver boolean is true
        if(!gameOver){
           //player 1 shooting bullet across screen when spacebar is pressed
           if(e.getKeyCode() == KeyEvent.VK_SPACE){
               Bullet bullet = new Bullet(player1.x+player1.width, (player1.y-2)+player1.height/2, 4, 4, BasicCache.bullet);
               //set speed of bullets for player 1 ([slower 1]-[10 faster]) positive number
               bullet.deltaX = 4;
               //adding the bullet object into the bullet array list
               bullets.add(bullet);
           }
           //player 2 shooting bullet across screen when enter is pressed
           if(e.getKeyCode() == KeyEvent.VK_ENTER){
               Bullet bullet = new Bullet(player2.x-4, (player2.y-2)+player2.height/2, 4, 4, BasicCache.bullet);
               //set speed of bullets for player 2 ([slower -1]-[-10 faster]) negative numbers
               bullet.deltaX = -4;
               //adding the bullet object into the bullet array list
               bullets.add(bullet);
           }
        }
    }

    
    
    //method for resetting the game
    private void resetGame(){
        //reset players back to start state
        player1.reset();
        player2.reset();
        //clear bullets in array
        bullets.clear();
        //return game to not over state
        gameOver = false;
    }
    
    
    
    //getter for the INSTANCE
    public static TanksGame getInstance(){
        //returns the current state of the game at this moment in time
        return INSTANCE;
    }
    
    
    
    //getter for player 1
    public Player getPlayer1(){
        return player1;
    }
    
    
    
    //getter for player 2
    public Player getPlayer2(){
        return player2;
    }
    
    
    
    //getter for bullet list
    public List<Bullet> getBullet(){
        return bullets;
    }
    
    
    
    //unused
    @Override
    public void keyTyped(KeyEvent e) {
    }

        

}//end line
