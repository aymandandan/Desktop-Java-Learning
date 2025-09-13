package notesapp;

import java.util.List;

public interface NoteDAO {
    void addNote(Note note);

    void updateNote(Note note);

    void deleteNote(int id);

    Note getNotebyId(int id);

    List<Note> getAllNotes();
}
