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
 * Date: 3/25/2017-5/19/2017
 * Purpose: A game for 2 players to play with tanks on one pc together.
 * This is the updated version to my "shooter" I made several years ago. 
 * It is my way of refreshing myself with java and of making a better game.
 * This will be version 2 out of 3. 2 will be new mechanics.
 * 3 will be enhanced and added controls for tanks and better background
 * possible version 4 with multiple levels?
 * Class: This file is the setup of bullets objects to be used in the game
 */
package TanksGame;

//imports
import java.awt.Graphics;
import java.awt.Image;


public class Bullet extends GameObject{

    //variables
    public int deltaX;
    
    
    
    //constructor for bullets
    public Bullet(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

    
    
    @Override
    public void update() {
        x += deltaX;
        rect.x += deltaX;
        
        //testing collision for if bullets have hit players. 
        //If they have then they need to be removed from bullet list
        //players health has to be lowered as well
        
        //(get INSTANCE of game, check if player 1 has bullet in it's rect)
        if(TanksGame.getInstance().getPlayer1().rect.contains(this.rect)){
            //if hit player 1 we need to remove the bullet from list
            removeBullet();
            //System.out.println("Player 2 bullet has hit Player 1");
            //lower the player 1's health by 1
            TanksGame.getInstance().getPlayer1().health--; 
        //(get INSTANCE of game, check if player 1 has bullet in it's rect)
        } else if (TanksGame.getInstance().getPlayer2().rect.contains(this.rect)){
            //if hit player 1 we need to remove the bullet from list
            removeBullet();
            //System.out.println("Player 1 bullet has hit Player 2");
            //lower the player 2's health by 1
            TanksGame.getInstance().getPlayer2().health--;
        }
        
        
        
        //checking to see if bullets have gone off of screen
        //if it is greater than zero player 1 is shooting
        if(deltaX > 0){
            if(x > TanksGame.getInstance().getWidth() + 50){
                removeBullet();
            }
        }
        //if it is less than zero player 2 is shooting
        else if(deltaX < 0){
            if(x < -50){
                removeBullet();
            }
        }
    }

    
    
    @Override
    public void draw(Graphics g) {
        //add image for bullet
        g.drawImage(img, x, y, width, height, null);
    }
    
    
    
    //method to remove bullet from game
    private void removeBullet(){
        TanksGame.getInstance().getBullet().remove(this);
    }
    
    
    
}//end line
