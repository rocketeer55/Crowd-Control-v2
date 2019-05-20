package MusicHandler;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicThread implements Runnable
{
    private MediaPlayer musicPlayer;
    private Sequencer sequencer;
    private Thread thread;
    private boolean running = false;
    private com.crowdcontrol.crowdcontrolv2.GameRenderer.GameRenderer gameRenderer;

    public MusicThread(int songID, BeatMap beatMap,
                       com.crowdcontrol.crowdcontrolv2.GameRenderer.GameRenderer gameRenderer,
                       Context context)
    {
        createMusicPlayer(context, songID);
        this.gameRenderer = gameRenderer;
        this.sequencer = new Sequencer(beatMap, gameRenderer);
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

            //Used for testing/debug
            //playLevel.currentBeat = sequencer.currentBeat;
        }

        //Stops playing music when thread is killed. This is very important.
        musicPlayer.stop();
        musicPlayer.release();
    }
}
