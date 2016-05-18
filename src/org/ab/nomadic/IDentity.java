package org.ab.nomadic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;

import processing.core.*;
import ddf.minim.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class IDentity extends PApplet
{
    private static final Logger log = Logger.getLogger(IDentity.class
            .getName());

    private Minim minim = new Minim(this);
    private static final boolean WITH_EXCLUDE = true;
    private static final int PLAY_NOTO_AFTER = 3;
    private static final int START_AFTER = 4;
    private static final String SOUNDS = "../sounds/";
    private int currentIDX = 0;
    private int firstKey = 0;

    private int[] idx;
    private String[] text;
    private AudioSample[] samples;
    private File[] files;

    private ArrayList<TextObject> textObjects = new ArrayList<TextObject>();
    private ArrayList<Float> excludeY = new ArrayList<Float>();
    private ArrayList<Float> excludeX = new ArrayList<Float>();
    private AlvaNoto noto = new AlvaNoto(minim);

    private static int WIDTH = 600, HEIGHT = 400;

    private void loadSounds()
    {
        final File folder = new File(SOUNDS);
        files = folder.listFiles();
        samples = new AudioSample[files.length];
        idx = new int[files.length];
        text = new String[files.length];

        for (int i = 0; i < files.length; ++i)
        {
            samples[i] = minim.loadSample(files[i].getPath(), 2048);
            idx[i] = i;
            final String fisName = files[i].getName();
            final String[] parts = fisName.split("_");
            text[i] = parts[0];
        }
    }

    @SuppressWarnings("unused")
    private TextObject[] loadSprites()
    {
        TextObject[] textSprites = new TextObject[files.length];
        for (int i = 0; i < files.length; ++i)
        {
            textSprites[i] = new TextObject(this, getTextAt(i));
        }
        return textSprites;
    }

    private void setFullScreenWidthHeight()
    {
        GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();
        // log.info(Arrays.toString(devices));
        if (devices.length > 1)
        { // we have a 2nd display/projector
          // learn the true dimensions of the secondary display
            WIDTH = devices[1].getDisplayMode().getWidth();
            HEIGHT = devices[1].getDisplayMode().getHeight();
            println("Adjusting animation size to " + WIDTH + "x" + HEIGHT
                    + " b/c of 2ndary display");
        } else
        { // no 2nd screen but make it fullscreen anyway
            WIDTH = devices[0].getDisplayMode().getWidth();
            HEIGHT = devices[0].getDisplayMode().getHeight();
            println("Adjusting animation size to " + WIDTH + "x" + HEIGHT
                    + " to fit primary display");
        }
    }

    public void init()
    {
        if (frame != null)
        {
            frame.removeNotify();// make the frame not displayable
            frame.setResizable(false);
            frame.setUndecorated(true);
            frame.setAlwaysOnTop(true);
            println("frame is at " + frame.getLocation());
            frame.addNotify();
        }
        super.init();
    }

    public void setup()
    {
        setFullScreenWidthHeight();
        size(WIDTH, HEIGHT);
        loadSounds();
    }

    private void handleIndex()
    {
        if (currentIDX < files.length - 1)
            currentIDX += 1;
        else
        {
            currentIDX = 0;
            shuffle(idx);
        }
    }

    public void shuffle(final int[] indexes)
    {
        final Random rnd = ThreadLocalRandom.current();
        for (int i = indexes.length - 1; i >= 0; i--)
        {
            final int index = rnd.nextInt(i + 1);
            final int other = indexes[index];
            indexes[index] = indexes[i];
            indexes[i] = other;
        }
    }

    public AudioSample getCurrentSample()
    {
        return samples[idx[currentIDX]];
    }

    public String getCurrentFileName()
    {
        return files[idx[currentIDX]].getPath();
    }

    public TextObject getCurrentSprite(final TextObject[] textSprites)
    {
        return textSprites[idx[currentIDX]];
    }

    public String getTextAt(final Integer i)
    {
        String txt = text[idx[i]];
        if (!("I".equals(txt) || "Я".equals(txt)))
            txt = txt.toLowerCase();
        return txt;
    }

    public String getCurrentText()
    {
        String txt = text[idx[currentIDX]];
        if (!("I".equals(txt) || "Я".equals(txt)))
            txt = txt.toLowerCase();
        return txt;
    }

    private float[] toFloatArray(final ArrayList<Float> floatList)
    {
        float[] floatArray = new float[floatList.size()];
        int i = 0;
        for (Float f : floatList)
            floatArray[i++] = (f != null ? f : Float.NaN);

        return floatArray;
    }

    private TextObject createTextObject(final String text,
            final boolean withExclude)
    {
        TextObject sp = new TextObject(this, getCurrentText());
        sp.setSizeRandom();
        if (withExclude)
        {
            final float[] exX = toFloatArray(excludeX);
            final float[] exY = toFloatArray(excludeY);

            sp.setCoordRandomExc(exX, exY);

            excludeX.add(sp.x);
            excludeY.add(sp.y);
        } else
        {
            sp.setCoordRandom();
        }

        return sp;
    }

    public void keyPressed()
    {
        if (firstKey < START_AFTER)
        {
            ++firstKey;
            if (PLAY_NOTO_AFTER == firstKey)
                noto.play();
            return;
        }
        handleIndex();

        final AudioSample samp = getCurrentSample();
        samp.trigger();
        final TextObject sp = createTextObject(getCurrentText(), WITH_EXCLUDE);
        textObjects.add(sp);

        log.info("Current text: " + getCurrentText());
        log.info("Current index: " + currentIDX);
    }

    public void draw()
    {
        background(0);
        stroke(255);
        if (mousePressed)
        {
            // TODO: add more events here
        }

        for (TextObject to : textObjects)
            to.display();
    }

    public static void main(String args[])
    {
        final int primary_display = 0;
        int primary_width;
        GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();
        String location;
        if (devices.length > 1)
        { 
            primary_width = devices[0].getDisplayMode().getWidth();
            location = "--location=" + primary_width + ",0";

        } else
        {
            location = "--location=0,0";
        }

        String display = "--display=" + primary_display + 1; 
                                                             
        PApplet.main(new String[]
        { location, "--hide-stop", display, "org.ab.nomadic.IDentity" });

    }

}
