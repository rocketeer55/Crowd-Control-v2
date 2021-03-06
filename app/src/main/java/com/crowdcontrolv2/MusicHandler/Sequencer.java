package com.crowdcontrolv2.MusicHandler;

public class Sequencer
{
    int quarterNotesPerBar;
    public float bpm;
    public float quarterNote;
    public float eighthNote;
    public float sixteenthNote;
    public float lengthOfMeasure;
    public float offset;
    public int beatNumber = 0;
    public int barNumber = 0;
    public float lastBeat;

    public int currentBeat;
    public int currentBar;

    public String currentSyllable;
    public float lastBeatLength;

    public BeatMap beatMap;
    public float triggerTime;
    public com.crowdcontrolv2.GameRenderer.Note currentNote;

    //com.crowdcontrol.crowdcontrolv2.GameRenderer.GameGLSurfaceView playLevel;

    //com.crowdcontrolv2.GameRenderer.GameRenderer gameRenderer;

    private MusicThread musicThread;

    public Sequencer(BeatMap beatMap) {
        this.beatMap = beatMap;
        quarterNotesPerBar = beatMap.beatsPerMeasure;
        bpm = beatMap.bpm;
        offset = beatMap.offset * 1000;
    }

    public Sequencer(BeatMap beatMap, MusicThread musicThread)
    {
        //this.gameRenderer = gameRenderer;
        this.musicThread = musicThread;
        this.beatMap = beatMap;
        quarterNotesPerBar = beatMap.beatsPerMeasure;
        bpm = beatMap.bpm;
        offset = beatMap.offset * 1000;
    }

    public void start()
    {
        lastBeat = 0;
        currentBeat = 0;
        currentBar = 1;
        currentSyllable = "PREP TIME";

        quarterNote = (60/bpm) * 1000;
        eighthNote = quarterNote/2;
        sixteenthNote = quarterNote/4;

        lengthOfMeasure = (quarterNotesPerBar * quarterNote);

        triggerTime = offset;
        lastBeatLength = offset;

        currentNote = beatMap.dequeue();
    }

//    public String dirToString(Arrow.DIRECTION arrowDir)
//    {
//        String returnVal;
//
//        switch(arrowDir)
//        {
//            case UP: returnVal = "UP";
//                break;
//            case DOWN: returnVal = "DOWN";
//                break;
//            case LEFT: returnVal = "LEFT";
//                break;
//            case RIGHT: returnVal = "RIGHT";
//                break;
//            default: returnVal = "UP";
//                break;
//        }
//
//        return returnVal;
//    }

    public float lengthToFloat(BeatMap.NOTE_LENGTH noteLength)
    {
        float returnVal;

        //System.out.println(noteLength);
        switch(noteLength)
        {
            case WHOLE: returnVal = (4 * quarterNote);
                break;
            case HALF: returnVal = (2 * quarterNote);
                break;
            case QUARTER: returnVal = quarterNote;
                break;
            case EIGHTH: returnVal = eighthNote;
                break;
            case SIXTEENTH: returnVal = sixteenthNote;
                break;
            case DOTTED_WHOLE: returnVal = 6 * quarterNote;
                break;
            case DOTTED_HALF: returnVal = 3 * quarterNote;
                break;
            case DOTTED_QUARTER: returnVal = quarterNote + eighthNote;
                break;
            case DOTTED_EIGHTH: returnVal = eighthNote + sixteenthNote;
                break;
            case DOTTED_SIXTEENTH: returnVal = sixteenthNote + (sixteenthNote /2);
                break;
            case QUARTER_TIE_SIXTEENTH: returnVal = quarterNote + sixteenthNote;
                break;
            case QUARTER_TIE_DOTTED_EIGHTH: returnVal = quarterNote + eighthNote + sixteenthNote;
                break;
            case HALF_TIE_SIXTEENTH: returnVal = (2*quarterNote) + sixteenthNote;
                break;
            case DOTTED_WHOLE_TIE_EIGHTH: returnVal = (6*quarterNote) + eighthNote;
                break;
            case WHOLE_TIE_EIGHTH: returnVal = (4*quarterNote) + eighthNote;
                break;
            case DOTTED_HALF_TIE_EIGHTH: returnVal = (3*quarterNote) + eighthNote;
                break;
            case HALF_TIE_EIGHTH: returnVal = (2*quarterNote) + eighthNote;
                break;
            case QUARTER_TRIPLET: returnVal = ((quarterNote * 2) / 3);
                break;
            case EIGHTH_TRIPLET: returnVal = (quarterNote / 3);
                break;
            case SIXTEENTH_TRIPLET: returnVal = (eighthNote /3);
                break;
            default: returnVal = quarterNote;
                break;
        }

        return returnVal;
    }

    public void update(float songPosition)
    {
        if(songPosition > triggerTime && currentNote != null)
        {
            currentBar = currentNote.measure;
            //currentSyllable = dirToString(currentNote.arrowDirection);
            lastBeat += lastBeatLength;
            //System.out.println("Adding note: " + currentNote);
            lastBeatLength = lengthToFloat(currentNote.noteLength);

            //playLevel.spawnArrow(currentNote.arrowDirection, songPosition, (songPosition + lengthOfMeasure));
            currentNote.setTarget(songPosition, (songPosition + lengthOfMeasure));
            //gameRenderer.addNote(currentNote);
            musicThread.addNote(currentNote);
            currentNote = beatMap.dequeue();

            triggerTime = lastBeatLength + lastBeat;
        }
    }
}
