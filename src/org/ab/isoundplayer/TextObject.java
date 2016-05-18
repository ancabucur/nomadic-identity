package org.ab.isoundplayer;

import processing.core.PApplet;

import java.util.Arrays;

public class TextObject
{
    float x;
    float y;
    PApplet parent; 
    String text;
    float size;

    TextObject(final PApplet p, final String txt)
    {
        parent = p;
        text = txt;
        x = 0;
        y = 0;
        size = 1;
    }

    public void setCoord(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setCoordRandom()
    {
        this.x = parent.random(0, parent.width / 1.2f);
        this.y = parent.random(20 + this.size, parent.height);
    }

    public void setCoordRandom(final float top, final float bottom, final float left, float right)
    {
        this.x = parent.random(left, parent.width - right);
        this.y = parent.random(top, parent.height - bottom);
    }

    public void setCoordRandomGauss()
    {

        float h2 = parent.height;
        float w2 = parent.width;
        float w4 = parent.width / 2;
        float r1 = w2 - 40;
        float x = 0;
        float y = 0;
        float d = 0;
        // Pick a random number inside the biggest circle centered at (w4, h2)
        do
        {
            x = parent.random(w4 - r1 / 2, w4 + r1 / 2);
            y = parent.random(h2 - r1 / 2 + 10, h2 + r1 / 2 - 10);
            d = (float) Math.sqrt((w4 - x) * (w4 - x) + (h2 - y) * (h2 - y));
        } while (d > r1);
        this.x = x;
        this.y = y;
    }

    public void setCoordRandomExc(final float[] excludeX, final float[] excludeY)
    {
        if (excludeX.length == 0 || excludeY.length == 0)
        {
            setCoordRandom();
            return;
        }
        this.x = getRandomWithExclusion(0, parent.width / 1.2f, excludeX);
        this.y = getRandomWithExclusion(100, parent.height, excludeY);
    }

    public float getRandomWithExclusion(final float start, final float end, final float[] exclude)
    {
        float rand = 0;
        Arrays.sort(exclude);
        do
        {
            rand = parent.random(start, end);
        } while (Arrays.binarySearch(exclude, rand) >= 0);
        return rand;
    }

    public void setSize(final int size)
    {
        this.size = size;
    }

    public void setSizeRandom()
    {
        this.size = parent.random(80, 200);
    }

    public void display()
    {

        parent.textSize(this.size);
        // parent.fill(parent.mouseX*360/parent.width,
        // parent.mouseY*100/parent.height, 300);
        // parent.fill(0, 0, 100);
        parent.text(this.text, this.x, this.y);
    }

}