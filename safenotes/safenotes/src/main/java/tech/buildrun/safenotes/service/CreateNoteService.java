package tech.buildrun.safenotes.service;

import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.controller.dto.NoteResponse;
import tech.buildrun.safenotes.entity.Note;
import tech.buildrun.safenotes.repository.NoteRepository;
import tech.buildrun.safenotes.repository.UserRepository;

@Service
public class CreateNoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public CreateNoteService(NoteRepository noteRepository,
                             UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public NoteResponse createNote(Long userId, String title, String content) {

        var user = userRepository.getReferenceById(userId);

        var note = new Note(title, content, user);

        noteRepository.save(note);

        return NoteResponse.fromEntity(note);
    }
}
