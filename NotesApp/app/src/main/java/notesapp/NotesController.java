package notesapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NotesController {

    @FXML
    private ListView<Note> notesList;

    @FXML
    private TextField noteTitle;

    @FXML
    private TextArea noteContent;

    @FXML
    private Button newBtn, saveBtn, deleteBtn;

    private NoteDAO noteDAO = new NoteDAO_SQLite();

    @FXML
    private void initialize() {
        notesList.getItems().addAll(new NoteDAO_SQLite().getAllNotes());
        notesList.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0) {
                Note note = notesList.getItems().get(index);
                noteTitle.setText(note.getTitle());
                noteContent.setText(note.getContent());
            }
        });
    }

    @FXML
    private void onNewNote() {
        Note newNote = new Note(0, "(empty note)", "");
        noteDAO.addNote(newNote);
        notesList.getItems().setAll(noteDAO.getAllNotes());
        notesList.getSelectionModel().selectLast();
        noteTitle.clear();
        noteContent.clear();
    }

    @FXML
    private void onSaveNote() {
        Note selectedNote = notesList.getSelectionModel().getSelectedItem();

        if (selectedNote == null)
            return;

        selectedNote.setTitle(noteTitle.getText());
        selectedNote.setContent(noteContent.getText());
        noteDAO.updateNote(selectedNote);
        notesList.refresh();
    }

    @FXML
    private void onDeleteNote() {
        Note selectedNote = notesList.getSelectionModel().getSelectedItem();
        if (selectedNote == null)
            return;
        noteDAO.deleteNote(selectedNote.getId());
        notesList.getItems().remove(selectedNote);
        notesList.getSelectionModel().clearSelection();
        noteTitle.clear();
        noteContent.clear();
    }

}
