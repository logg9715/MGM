package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FreeBoardPictureENT;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeBoardPictureREP extends CrudRepository<FreeBoardPictureENT, Long> {
    // 사진 데이터를 관리하는 리포지토리
}
