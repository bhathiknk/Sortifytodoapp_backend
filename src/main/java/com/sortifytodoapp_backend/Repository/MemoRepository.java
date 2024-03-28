package com.sortifytodoapp_backend.Repository;

import com.sortifytodoapp_backend.Model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Integer> {
    List<Memo> findAllByUserId(Integer userId);
}
