package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.CMPostENT;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CMPostREP extends CrudRepository<CMPostENT, Long> {
    List<CMPostENT> findTop4ByOrderByRegdateDesc(); // 내림차순 정렬
    Object findAll(Sort regdate);

    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 쿼리 메서드
    List<CMPostENT> findByUserIdOrderByRegdateDesc(Long userId);

}
