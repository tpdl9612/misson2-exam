package com.example.ms1.note.notebook;

import com.example.ms1.note.note.Note;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createDate;

   @OneToMany(mappedBy = "notebook")
   List<Note> noteList = new ArrayList<>();
}
