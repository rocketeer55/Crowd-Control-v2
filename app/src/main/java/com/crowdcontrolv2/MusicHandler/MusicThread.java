package com.crowdcontrolv2.MusicHandler;

import android.content.Context;
import android.media.MediaPlayer;

import com.crowdcontrolv2.GameRenderer.Note;

import java.util.LinkedList;

public class MusicThread implements Runnable
{
    private MediaPlayer musicPlayer;
    private Sequencer sequencer;
    private Thread thread;
    private boolean running = false;
    private com.crowdcontrolv2.GameRenderer.GameRenderer gameRenderer;
    private LinkedList<Note> notes = new LinkedList<>();

    public MusicThread(int songID, BeatMap beatMap,
                       com.crowdcontrolv2.GameRenderer.GameRenderer gameRenderer,
                       Context context)
    {
        createMusicPlayer(context, songID);
        this.gameRenderer = gameRenderer;
        this.sequencer = new Sequencer(beatMap, this);
        thread = new Thread(this);
    }

    //Creates a MediaPlayer to play music
    public void createMusicPlayer(Context context, int songID)
    {
        this.musicPlayer = MediaPlayer.create(context, songID);
    }

    @Override
    public void run() {
        //Might need to sleep() here for a second to give prep time.
        musicPlayer.start();
        sequencer.start();

        while (running)
        {
            gameRenderer.setSongPos(musicPlayer.getCurrentPosition());

            sequencer.update(musicPlayer.getCurrentPosition());

            for (Note n : notes) {
                n.updatePosition(musicPlayer.getCurrentPosition());
//                if(n.getBarNo() == 1)
//                {
//                    System.out.println("POSITION IS: " + n.getRelativePosition() + "LENGTH IS: " + n.getLength());
//                }
            }

            //Used for testing/debug
            //playLevel.currentBeat = sequencer.currentBeat;
        }

        //Stops playing music when thread is killed. This is very important.
        musicPlayer.stop();
        musicPlayer.release();
    }

    public void start()
    {
        running = true;
        thread.start();
    }

    public void addNote(com.crowdcontrolv2.GameRenderer.Note n)
    {
        //System.out.println("ADDING NOTE: " + n);
        LinkedList<Note> newNotes = new LinkedList<>(notes);
        newNotes.add(n);
        notes = newNotes;
    }

    public Snapshot getSnapshot()
    {
        return new Snapshot(notes);
    }
}
