package com.crowdcontrolv2.MusicHandler;

import com.crowdcontrolv2.GameRenderer.Note;

import java.util.LinkedList;

public class Snapshot
{
    private final LinkedList<Note> notes;

    public Snapshot(LinkedList<Note> notes)
    {
        this.notes = notes;
    }

    public LinkedList<Note> getNotes()
    {
        return notes;
    }
}
