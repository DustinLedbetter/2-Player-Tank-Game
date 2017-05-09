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


public class Bullet extends GameObject{

    //variables
    public int deltaX;
    
    //constructor for bullets
    public Bullet(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

    @Override
    void update() {
        x += deltaX;
        rect.x += deltaX;
    }

    @Override
    void draw(Graphics g) {
        //add image for bullet
        g.drawImage(img, x, y, width, height, null);
    }
    
}
