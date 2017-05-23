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
 * Class: This file is the setup of game objects to be used in collision testing
 */
package TanksGame;

//imports
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class GameObject {
    
    //integers for positioning
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    
    
    //image
    protected Image img;
    protected Rectangle rect;
    
    
    
    //constructor
    public GameObject(int x, int y, int width, int height, Image img){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
        
        this.rect = new Rectangle(x, y, width, height);
    }
    
    
    
    abstract void update();
    abstract void draw(Graphics g);
    
    
    
}//end line
