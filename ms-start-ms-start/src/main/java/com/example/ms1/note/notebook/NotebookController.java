package com.example.ms1.note.notebook;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class NotebookController {
    private final NotebookRepository notebookRepository;
    private final NoteService noteService;

    @PostMapping("/books/write")
    public String write(){
        Notebook notebook = new Notebook();
        notebook.setName("새 노트북");
        notebookRepository.save(notebook);
        return "redirect:/books/"+notebook.getId();
    }

    @GetMapping("/books/{id}")
    public String detail(@PathVariable ("id")Long id){
        Notebook notebook = notebookRepository.findById(id).orElseThrow();
        if(notebook.getNoteList().isEmpty()){
            noteService.saveDefault(notebook);
            return "redirect:/books/"+id;
        }
        Note note = notebook.getNoteList().get(0);
        return "redirect:/books/%d/notes/%d".formatted(id, note.getId());

    }

}
