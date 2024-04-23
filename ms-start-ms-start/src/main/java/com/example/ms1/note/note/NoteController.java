package com.example.ms1.note.note;

import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{notebookId}/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final NotebookRepository notebookRepository;
    private final NoteService noteService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

//    @RequestMapping("/")
//    public String main(Model model) {
//        //1. DB에서 데이터 꺼내오기
//        List<Note> noteList = noteRepository.findAll();
//
//        //2. 꺼내온 데이터를 템플릿으로 보내기
//        model.addAttribute("noteList", noteList);
//        model.addAttribute("targetNote", noteList.get(0));
//
//        return "main";
//    }

    @PostMapping("/write")
    public String write(@PathVariable("notebookId")Long notebookId){
        Notebook notebook = notebookRepository.findById(notebookId).orElseThrow();
        Note newNote = noteService.saveDefault(notebook);
        return "redirect:/books/%d/notes/%d".formatted(notebookId, newNote.getId());
    }

    @GetMapping("/{id}")
    public String detail(Model model,
                         @PathVariable("notebookId") Long notebookId,
                         @PathVariable("id") Long id) {
//        Notebook notebook = notebookRepository.findById(notebookId).orElseThrow();
        Note note = noteRepository.findById(id).get();
        List <Notebook> notebookList = notebookRepository.findAll();
        Notebook targetNotebook = notebookRepository.findById(notebookId).get();
        List <Note> noteList = noteRepository.findByNotebook(targetNotebook);

        if(noteList.isEmpty()){
            Note newNote = noteService.saveDefault(targetNotebook);
            noteList.add(newNote);
            return"redirect:/";
        }

        model.addAttribute("targetNotebook", targetNotebook);
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteList);

        return "main";
    }
    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("notebookId")Long notebookId,
                             @PathVariable("id")Long id, Model model){

        Note note = noteRepository.findById(id).get();
        List<Notebook> notebookList = notebookRepository.findAll();
        Notebook targetNotebook = notebookRepository.findById(notebookId).get();
        List <Note> noteList = noteRepository.findByNotebook(targetNotebook);

        model.addAttribute("targetNote", note);
        model.addAttribute("targetNotebook", targetNotebook);
        model.addAttribute("noteList", noteList);
        model.addAttribute("notebookList", notebookList);
        return "main";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @PathVariable("notebookId") Long notebookId,
                         String title, String content) {
        Note note = noteRepository.findById(id).orElseThrow();
        if(title.trim().length() == 0){
            title = "제목 없음";
        }
        note.setTitle(title);
        note.setContent(content);
        noteRepository.save(note);
        return "redirect:/books/%d/notes/%d".formatted(notebookId,id);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable ("notebookId")Long notebookId,
                         @PathVariable("id")Long id){
        noteRepository.deleteById(id);
        return "redirect:/books/"+notebookId;
    }
}
