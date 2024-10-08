package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FreeBoardENT;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FreeBoardREP extends CrudRepository<FreeBoardENT, Long> {
    List<FreeBoardENT> findTop4ByOrderByRegdateDesc(); // 최신 게시글 4개 조회

    // 특정 사용자가 작성한 게시글을 비활성화 상태가 아닌 경우에 한하여 내림차순으로 조회하는 쿼리 메서드
    List<FreeBoardENT> findTop3ByUserIdAndDisableFalseOrderByRegdateDesc(Long userId);

    // 게시글을 비활성화 여부에 따라 조회
    List<FreeBoardENT> findByDisableFalse();

}
