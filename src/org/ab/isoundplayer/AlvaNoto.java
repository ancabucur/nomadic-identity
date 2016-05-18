package org.ab.isoundplayer;

import ddf.minim.*;
import ddf.minim.analysis.BeatDetect;

public class AlvaNoto
{
    //private Minim minim;
    private BeatDetect beat;
    private String NOTO = "../noto/noto3.mp3";

    public AudioPlayer player;

    private void init(final Minim minim)
    {
        // minim = new Minim(p);
        player = minim.loadFile(NOTO);
        beat = new BeatDetect();
        // beat = new BeatDetect(player.bufferSize(), player.sampleRate());
        // beat.detectMode(beat.FREQ_ENERGY);
    }

    public AlvaNoto(final Minim minim)
    {
        init(minim);
    }

    public AlvaNoto(final Minim minim, final String pathToNoto)
    {
        NOTO = pathToNoto;
        init(minim);
    }

    public void play()
    {
        player.play();
    }

    public void pause()
    {
        player.pause();
    }

    public void stop()
    {
        player.pause();
        player.rewind();
    }

    public boolean isOnset()
    {
        beat.detect(player.mix);
        return beat.isOnset();
    }

    public boolean isOnset(final Integer i)
    {
        beat.detect(player.mix);
        return beat.isOnset(i);
    }

    public boolean isHat()
    {
        beat.detect(player.mix);
        return beat.isHat();
    }

    public boolean isKick()
    {
        beat.detect(player.mix);
        return beat.isKick();
    }

}
