package tech.buildrun.safenotes.controller.dto;

import tech.buildrun.safenotes.entity.Note;

public record NoteResponse(Long id, String title) {

    public static NoteResponse fromEntity(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle()
        );
    }
}
