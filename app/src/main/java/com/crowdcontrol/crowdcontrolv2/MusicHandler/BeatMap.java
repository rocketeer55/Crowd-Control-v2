package MusicHandler;

import android.content.Context;
import android.content.res.Resources;

import com.crowdcontrolv2.GameRenderer.Note;

import java.io.InputStream;
import java.util.Scanner;

public class BeatMap
{
    public enum NOTE_LENGTH
    {
        WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH,
        DOTTED_WHOLE, DOTTED_HALF, DOTTED_QUARTER, DOTTED_EIGHTH, DOTTED_SIXTEENTH,
        QUARTER_TIE_SIXTEENTH, QUARTER_TIE_DOTTED_EIGHTH,
        HALF_TIE_SIXTEENTH, DOTTED_WHOLE_TIE_EIGHTH, WHOLE_TIE_EIGHTH, DOTTED_HALF_TIE_EIGHTH,
        HALF_TIE_EIGHTH,
        QUARTER_TRIPLET, EIGHTH_TRIPLET, SIXTEENTH_TRIPLET,
        NOTE_OFF, UNKNOWN_LENGTH
    }

    private static class Node
    {
        public com.crowdcontrolv2.GameRenderer.Note note;
        public Node next;
        public int val;

        public Node(com.crowdcontrolv2.GameRenderer.Note note)
        {
            this.note = note;
        }

        public Node(int val)
        {
            this.val = val;
        }

        public Node next()
        {
            return next;
        }
    }

    public float offset;
    public int beatsPerMeasure;
    public float bpm;

    public Node head;
    public Node tail;

    public BeatMap(int resID, Context context)
    {
        Resources resources = context.getResources();
        InputStream readStream = resources.openRawResource(resID);

        Scanner scanner = new Scanner(readStream);

        createMap(scanner);
    }

    public BeatMap()
    {
        // Default constructor
    }

    private float noteLengthToFloat(NOTE_LENGTH length)
    {
        float returnVal;

        switch(length)
        {
            case WHOLE: returnVal = 4.0f / beatsPerMeasure;
                break;
            case HALF: returnVal = 2.0f / beatsPerMeasure;
                break;
            case QUARTER: returnVal = 1.0f / beatsPerMeasure;
                break;
            case EIGHTH: returnVal = (0.5f) / beatsPerMeasure;
                break;
            case SIXTEENTH: returnVal = (0.25f) / beatsPerMeasure;
                break;
            case DOTTED_WHOLE: returnVal = 6.0f / beatsPerMeasure;
                break;
            case DOTTED_HALF: returnVal = 3.0f / beatsPerMeasure;
                break;
            case DOTTED_QUARTER: returnVal = 1.5f / beatsPerMeasure;
                break;
            case DOTTED_EIGHTH: returnVal = 0.75f / beatsPerMeasure;
                break;
            case DOTTED_SIXTEENTH: returnVal = (0.25f + 0.25f/2.0f) / beatsPerMeasure;
                break;
            case QUARTER_TIE_SIXTEENTH: returnVal = 1.25f / beatsPerMeasure;
                break;
            case QUARTER_TIE_DOTTED_EIGHTH: returnVal = 1.75f / beatsPerMeasure;
                break;
            case HALF_TIE_SIXTEENTH: returnVal = 2.25f / beatsPerMeasure;
                break;
            case DOTTED_WHOLE_TIE_EIGHTH: returnVal = 6.5f /beatsPerMeasure;
                break;
            case WHOLE_TIE_EIGHTH: returnVal = 4.5f / beatsPerMeasure;
                break;
            case DOTTED_HALF_TIE_EIGHTH: returnVal = 3.5f / beatsPerMeasure;
                break;
            case HALF_TIE_EIGHTH: returnVal = 2.5f / beatsPerMeasure;
                break;
            case QUARTER_TRIPLET: returnVal = (2.0f/3.0f) / beatsPerMeasure ;
                break;
            case EIGHTH_TRIPLET: returnVal = (1.0f/3.0f) / beatsPerMeasure;
                break;
            case SIXTEENTH_TRIPLET: returnVal = (1.0f/6.0f) / beatsPerMeasure;
                break;
            default: returnVal = 1.0f;
                break;
        }

        return returnVal;
    }

    NOTE_LENGTH stringToNoteLength(String string)
    {
        NOTE_LENGTH returnVal;

        switch(string)
        {
            case "WHOLE": returnVal = NOTE_LENGTH.WHOLE;
                break;
            case "HALF": returnVal = NOTE_LENGTH.HALF;
                break;
            case "QUARTER": returnVal = NOTE_LENGTH.QUARTER;
                break;
            case "EIGHTH": returnVal = NOTE_LENGTH.EIGHTH;
                break;
            case "SIXTEENTH": returnVal = NOTE_LENGTH.SIXTEENTH;
                break;
            case "DOTTED_WHOLE": returnVal = NOTE_LENGTH.DOTTED_WHOLE;
                break;
            case "DOTTED_HALF": returnVal = NOTE_LENGTH.DOTTED_HALF;
                break;
            case "DOTTED_QUARTER": returnVal = NOTE_LENGTH.DOTTED_QUARTER;
                break;
            case "DOTTED_EIGHTH": returnVal = NOTE_LENGTH.DOTTED_EIGHTH;
                break;
            case "DOTTED_SIXTEENTH": returnVal = NOTE_LENGTH.DOTTED_SIXTEENTH;
                break;
            case "QUARTER_TIE_SIXTEENTH": returnVal = NOTE_LENGTH.QUARTER_TIE_SIXTEENTH;
                break;
            case "QUARTER_TIE_DOTTED_EIGHTH": returnVal = NOTE_LENGTH.QUARTER_TIE_DOTTED_EIGHTH;
                break;
            case "HALF_TIE_SIXTEENTH": returnVal = NOTE_LENGTH.HALF_TIE_SIXTEENTH;
                break;
            case "DOTTED_WHOLE_TIE_EIGHTH": returnVal = NOTE_LENGTH.DOTTED_WHOLE_TIE_EIGHTH;
                break;
            case "WHOLE_TIE_EIGHTH": returnVal = NOTE_LENGTH.WHOLE_TIE_EIGHTH;
                break;
            case "DOTTED_HALF_TIE_EIGHTH": returnVal = NOTE_LENGTH.DOTTED_HALF_TIE_EIGHTH;
                break;
            case "HALF_TIE_EIGHTH": returnVal = NOTE_LENGTH.HALF_TIE_EIGHTH;
                break;
            case "QUARTER_TRIPLET": returnVal = NOTE_LENGTH.DOTTED_SIXTEENTH;
                break;
            case "EIGHTH_TRIPLET": returnVal = NOTE_LENGTH.EIGHTH_TRIPLET;
                break;
            case "SIXTEENTH_TRIPLET": returnVal = NOTE_LENGTH.SIXTEENTH_TRIPLET;
                break;
            default: returnVal = NOTE_LENGTH.QUARTER;
                break;
        }

        return returnVal;
    }

    com.crowdcontrolv2.GameRenderer.Note.POSITION stringToNotePos(String string)
    {
        Note.POSITION returnVal;

        switch(string)
        {
            case "LEFT_BLUE": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.LEFT_BLUE;
                break;
            case "RIGHT_BLUE": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.RIGHT_BLUE;
                break;
            case "LEFT_YELLOW": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.LEFT_YELLOW;
                break;
            case "RIGHT_YELLOW": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.RIGHT_YELLOW;
                break;
            case "LEFT_RED": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.LEFT_RED;
                break;
            case "RIGHT_RED": returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.LEFT_RED;
                break;
            default: returnVal = com.crowdcontrolv2.GameRenderer.Note.POSITION.LEFT_RED;
                break;
        }

        return returnVal;
    }

    public Note generateNote(Note.POSITION pos, NOTE_LENGTH length, int barNo)
    {
        Note returnVal;
        switch(pos)
        {
            case LEFT_BLUE: returnVal = new com.crowdcontrolv2.GameRenderer.LeftBlueNote(barNo, 35, noteLengthToFloat(length));
                break;
            case RIGHT_BLUE: returnVal = new com.crowdcontrolv2.GameRenderer.RightBlueNote(barNo, 35, noteLengthToFloat(length));
                break;
            case LEFT_YELLOW: returnVal = new com.crowdcontrolv2.GameRenderer.LeftYellowNote(barNo, 35, noteLengthToFloat(length));
                break;
            case RIGHT_YELLOW: returnVal = new com.crowdcontrolv2.GameRenderer.RightYellowNote(barNo, 35, noteLengthToFloat(length));
                break;
            case LEFT_RED: returnVal = new com.crowdcontrolv2.GameRenderer.LeftRedNote(barNo, 35, noteLengthToFloat(length));
                break;
            case RIGHT_RED: returnVal = new com.crowdcontrolv2.GameRenderer.RightRedNote(barNo, 35, noteLengthToFloat(length));
                break;
            default: returnVal = new com.crowdcontrolv2.GameRenderer.LeftBlueNote(barNo, 35, noteLengthToFloat(length));
                break;
        }

        return returnVal;
    }

    public void createMap(Scanner scanner)
    {
        String line;

        scanner.nextLine();

        offset = scanner.nextFloat();
        scanner.nextLine();
        beatsPerMeasure = scanner.nextInt();
        scanner.nextLine();
        bpm = scanner.nextFloat();
        scanner.nextLine();

        while(scanner.hasNext())
        {
            line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);

            if(lineScanner.hasNextInt())
            {
                int tempBarNo = lineScanner.nextInt();
                NOTE_LENGTH tempNoteLength = stringToNoteLength(lineScanner.next());
                Note.POSITION tempNotePos = stringToNotePos(lineScanner.next());

                //Note tempNote = new Note(tempBarNo, tempNoteLength, tempArrowDir);
                Note tempNote = generateNote(tempNotePos, tempNoteLength, tempBarNo);
                tempNote.noteLength = tempNoteLength;
                Node tempNode = new Node(tempNote);

                enqueue(tempNode);
            }

            lineScanner.close();
        }
    }

    public void enqueue(Node node)
    {
        if(tail == null)
        {
            tail = node;
            head = node;
        }

        else
        {
            if(head.next == null)
            {
                head.next = tail;
            }

            tail.next = node;
            tail = node;
        }
    }

    public Note dequeue()
    {
        if(head == null)
        {
            return null;
        }

        Node returnNode = head;

        head = head.next;

        if(head == null)
        {
            tail = null;
        }

        return returnNode.note;
    }
}
