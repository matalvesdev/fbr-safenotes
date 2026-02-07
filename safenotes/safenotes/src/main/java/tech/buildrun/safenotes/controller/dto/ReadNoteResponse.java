package tech.buildrun.safenotes.controller.dto;

import tech.buildrun.safenotes.entity.Note;

public record ReadNoteResponse(Long id, String title, String content) {

    public static ReadNoteResponse fromEntity(Note note) {
        return new ReadNoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent()
        );
    }
}
