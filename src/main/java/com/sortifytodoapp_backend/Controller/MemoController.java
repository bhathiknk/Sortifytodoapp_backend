package com.sortifytodoapp_backend.Controller;

import com.sortifytodoapp_backend.DTO.MemoDTO;
import com.sortifytodoapp_backend.Model.Memo;
import com.sortifytodoapp_backend.Service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
public class MemoController {

    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping
    public ResponseEntity<Memo> addMemo(@RequestBody MemoDTO memoDTO) {
        Memo savedMemo = memoService.saveMemo(memoDTO);
        return new ResponseEntity<>(savedMemo, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Memo>> getMemosByUserId(@PathVariable Integer userId) {
        List<Memo> memos = memoService.getAllMemosByUserId(userId);
        return new ResponseEntity<>(memos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemoById(@PathVariable Integer id) {
        memoService.deleteMemoById(id);
        return ResponseEntity.ok().build();
    }
}
