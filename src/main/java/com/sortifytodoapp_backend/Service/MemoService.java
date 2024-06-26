package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.MemoDTO;
import com.sortifytodoapp_backend.Model.Memo;
import com.sortifytodoapp_backend.Repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    //this logic use for save memo data using memoDTO assign parameters
    public Memo saveMemo(MemoDTO memoDTO) {
        Memo memo = new Memo();
        memo.setUserId(memoDTO.getUserId());
        memo.setContent(memoDTO.getContent());
        return memoRepository.save(memo);
    }

    //this logic get all memo data as list and pass to frontend
    public List<Memo> getAllMemosByUserId(Integer userId) {
        return memoRepository.findAllByUserId(userId);
    }

    //this logic delete all data based on the memo id
    public void deleteMemoById(Integer id) {
        memoRepository.deleteById(id);
    }
}
