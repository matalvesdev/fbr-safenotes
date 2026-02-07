package tech.buildrun.safenotes.service;

import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.controller.dto.NoteResponse;
import tech.buildrun.safenotes.entity.Note;
import tech.buildrun.safenotes.repository.NoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListNoteService {

    private final NoteRepository noteRepository;

    public ListNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteResponse> listNotes(Long userId) {
        return noteRepository.findAllByOwnerId(userId)
                .stream()
                .map(NoteResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
