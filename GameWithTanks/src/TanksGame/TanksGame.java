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
        player1 = new Player(10, 150, 20, 90, BasicCache.player1);
        player2 = new Player(570, 150, 20, 90, BasicCache.player2);
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
        //update players
        player1.update();
        player2.update();
        //draws every bullet that is added to the bullet array list
        for(Bullet bullet : bullets){
            bullet.update();
        }
    }
    
    
    
    //where all game image components will be created
    public void render(){
        //ensure screen gets cleared of what was there before
        graphics.clearRect(0, 0, WIDTH, HEIGHT);
        //set screen to be black (version 3 will be better background )
        graphics.setColor(Color.black);
        //rectangle that fills game screen
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        //players 1 and 2 
        player1.draw(graphics);
        player2.draw(graphics);
        //draws every bullet that is added to the bullet array list
        for(Bullet bullet : bullets){
            bullet.draw(graphics);
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
        new TanksGame();
    } 
    
    
    
    //listener actions for player 1 and 2 if keys pressed
    @Override
    public void keyPressed(KeyEvent e) {
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

    
    
    //unused
    @Override
    public void keyTyped(KeyEvent e) {
    }

        

}//end line
