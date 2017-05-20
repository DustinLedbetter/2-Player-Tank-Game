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



package TanksGame;

//imports
import java.awt.Graphics;
import java.awt.Image;

public class Player extends GameObject{

    //variables
    private static final int SPEED = 5;
    private static final int START_HEALTH = 10;
    public boolean up;
    public boolean down;
    public int health;
    public int startX;
    public int startY;

    
    
    //constructor for players
    public Player(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
        //set health at start of game
        this.health = START_HEALTH;
        //set starting X and Y coordinates of players
        this.startX = x;
        this.startY = y;
    }

    
    
    @Override
    public void update() {
       //moving player object up
       if(up) {
           if(y != 17){
           y -= SPEED;
           rect.y -= SPEED;
           }
       }
       //moving player object down
       if(down) {
           if(y != 480){
           y += SPEED;
           rect.y += SPEED;
           }
       }
    }

    
    
    @Override
    public void draw(Graphics g) {
        //add image for player
       g.drawImage(img, x, y, width, height, null);
    }
    
    
    
    //used to reset game back to starting point
    public void reset(){
        //reset players to starting positions
        this.x = startX;
        this.y = startY;
        this.rect.x = startX;
        this.rect.y = startY;
        this.health = START_HEALTH;
    }
    
    
    
}//end line
