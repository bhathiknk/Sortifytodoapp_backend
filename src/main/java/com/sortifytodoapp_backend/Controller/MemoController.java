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


    // this api cal to addMomo logic to save memo data
    @PostMapping
    public ResponseEntity<Memo> addMemo(@RequestBody MemoDTO memoDTO) {
        Memo savedMemo = memoService.saveMemo(memoDTO);
        return new ResponseEntity<>(savedMemo, HttpStatus.CREATED);
    }

    //this api call to getMemosByUserId logic to retrive al memo details base on the userId
    @GetMapping("/{userId}")
    public ResponseEntity<List<Memo>> getMemosByUserId(@PathVariable Integer userId) {
        List<Memo> memos = memoService.getAllMemosByUserId(userId);
        return new ResponseEntity<>(memos, HttpStatus.OK);
    }

    //this api call to deleteMemoById login to delete user created memos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemoById(@PathVariable Integer id) {
        memoService.deleteMemoById(id);
        return ResponseEntity.ok().build();
    }
}
