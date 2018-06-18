package com.example.nguyenducke.lovenotes.Adapter;

import java.io.Serializable;

public class Note implements Serializable {
    private int noteId;
    private String noteTitle;
    private String noteContent;
    private String notePicture;
    private String noteNgayTao;

    public Note() {
    }

    public Note(String noteTitle, String noteContent, String notePicture, String noteNgayTao) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.notePicture = notePicture;
        this.noteNgayTao = noteNgayTao;
    }

    public Note(int noteId, String noteTitle, String noteContent, String notePicture, String noteNgayTao) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.notePicture = notePicture;
        this.noteNgayTao = noteNgayTao;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNotePicture() {
        return notePicture;
    }

    public void setNotePicture(String notePicture) {
        this.notePicture = notePicture;
    }

    public String getNoteNgayTao() {
        return noteNgayTao;
    }

    public void setNoteNgayTao(String noteNgayTao) {
        this.noteNgayTao = noteNgayTao;
    }

    @Override
    public String toString() {
        return this.noteTitle;
    }
}
