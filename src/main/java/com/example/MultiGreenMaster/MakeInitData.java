package com.example.MultiGreenMaster;

import com.example.MultiGreenMaster.entity.*;
import com.example.MultiGreenMaster.repository.AnnounceREP;
import com.example.MultiGreenMaster.repository.CMCommentREP;
import com.example.MultiGreenMaster.repository.CMPostREP;
import com.example.MultiGreenMaster.repository.UserREP;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

//시험문제는 아니다.
@Component //스프링 컨테이너에게 자바 객체를 생성하라는 명령.
@RequiredArgsConstructor
public class MakeInitData {
    private final UserREP userRepository;
    private final AnnounceREP announceRepository;
    private final CMPostREP cmPostRepository;
    private final CMCommentREP cmCommentRepository;

    @Transactional
    @PostConstruct //이 어노테이션은 객체가 생성된 뒤 단 한 번만 실행이 된다.
    public void makeAdminAndUserAndCMPostAndCMComment() {
        UserENT admin1 = UserENT.builder()
                .loginId("admin1")
                .password("1234")
                .nickname("관리자")
                .name("김동현")
                .phonenumber("010-5656-1541")
                .email("hayasdan@naver.com")
                .role(User_RoleENT.ADMIN)
                .active(1)
                .build();
        userRepository.save(admin1);

        UserENT user1 = UserENT.builder()
                .loginId("user1")
                .password("1234")
                .nickname("밀양 박씨")
                .name("박덕수")
                .phonenumber("010-1234-1234")
                .email("user1@naver.com")
                .role(User_RoleENT.USER)
                .active(1)
                .build();
        userRepository.save(user1);

        UserENT user2 = UserENT.builder()
                .loginId("user2")
                .password("1234")
                .nickname("황소그림")
                .name("이중섭")
                .phonenumber("010-5432-1234")
                .email("user2@naver.com")
                .role(User_RoleENT.USER)
                .active(1)
                .build();
        userRepository.save(user2);

        UserENT user3 = UserENT.builder()
                .loginId("user3")
                .password("1234")
                .nickname("AR리따움")
                .name("황진이")
                .phonenumber("016-1432-1234")
                .email("hwang2@naver.com")
                .role(User_RoleENT.USER)
                .active(1)
                .build();
        userRepository.save(user3);

        CMPostENT cmpost1 = CMPostENT.builder()
                .title("게시글 1")
                .content("이 글은 게시글 1입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 7, 12, 10, 30))
                .user(user1)
                .build();
        cmPostRepository.save(cmpost1);

        CMPostENT cmpost2 = CMPostENT.builder()
                .title("게시글 2")
                .content("이 글은 게시글 2입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 7, 12, 10, 30))
                .user(user1)
                .build();
        cmPostRepository.save(cmpost2);

        CMPostENT cmpost3 = CMPostENT.builder()
                .title("게시글 3")
                .content("이 글은 게시글 3입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 7, 13, 10, 30))
                .user(user3)
                .build();
        cmPostRepository.save(cmpost3);

        CMPostENT cmpost4 = CMPostENT.builder()
                .title("게시글 4")
                .content("이 글은 게시글 4입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 7, 13, 12, 40))
                .user(user2)
                .build();
        cmPostRepository.save(cmpost4);

        CMPostENT cmpost5 = CMPostENT.builder()
                .title("게시글 5")
                .content("이 글은 게시글 5입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 8, 15, 11, 36))
                .user(user1)
                .build();
        cmPostRepository.save(cmpost5);

        CMPostENT cmpost6 = CMPostENT.builder()
                .title("게시글 6")
                .content("이 글은 게시글 6입니다.")
                .count(0)
                .likeCount(0)
                .regdate(LocalDateTime.of(2024, 8, 22, 5, 20))
                .user(user3)
                .build();
        cmPostRepository.save(cmpost6);

        //댓글
        CMCommentENT cmcomment1 = CMCommentENT.builder()
                .content("이 글은 1번 댓글입니다.")
                .likeCount(1)
                .regdate(LocalDateTime.of(2024, 8, 1, 4, 30))
                .user(user1)
                .cmPost(cmpost6)
                .build();
        cmCommentRepository.save(cmcomment1);

        CMCommentENT cmcomment2 = CMCommentENT.builder()
                .content("이 글은 2번 댓글입니다.")
                .likeCount(2)
                .regdate(LocalDateTime.of(2024, 8, 2, 5, 45))
                .user(user3)
                .cmPost(cmpost6) // 댓글이 달릴 게시글을 설정
                .build();
        cmCommentRepository.save(cmcomment2);

        CMCommentENT cmcomment3 = CMCommentENT.builder()
                .content("이 글은 3번 댓글입니다.")
                .likeCount(4)
                .regdate(LocalDateTime.of(2024, 8, 3, 15, 45))
                .user(user2)
                .cmPost(cmpost1) // 댓글이 달릴 게시글을 설정
                .build();
        cmCommentRepository.save(cmcomment3);

        CMCommentENT cmcomment4 = CMCommentENT.builder()
                .content("이 글은 4번 댓글입니다.")
                .likeCount(2)
                .regdate(LocalDateTime.of(2024, 8, 3, 15, 55))
                .user(user1)
                .cmPost(cmpost6) // 댓글이 달릴 게시글을 설정
                .build();
        cmCommentRepository.save(cmcomment4);

    }

    @Transactional
    @PostConstruct //이 어노테이션은 객체가 생성된 뒤 단 한 번만 실행이 된다.
    public void makeAnnounce() {
        AnnounceBoardENT announce1 = AnnounceBoardENT.builder()
                .title("공지사항 1")
                .content("이 글은 공지사항 1번입니다.")
                .build();
        announceRepository.save(announce1);

        AnnounceBoardENT announce2 = AnnounceBoardENT.builder()
                .title("공지사항 2")
                .content("이 글은 공지사항 2번입니다.")
                .build();
        announceRepository.save(announce2);

        AnnounceBoardENT announce3 = AnnounceBoardENT.builder()
                .title("공지사항 3")
                .content("이 글은 공지사항 3번입니다.")
                .build();
        announceRepository.save(announce3);

        AnnounceBoardENT announce4 = AnnounceBoardENT.builder()
                .title("공지사항 4")
                .content("이 글은 공지사항 4번입니다.")
                .build();
        announceRepository.save(announce4);

        AnnounceBoardENT announce5 = AnnounceBoardENT.builder()
                .title("공지사항 5")
                .content("이 글은 공지사항 5번입니다.")
                .build();
        announceRepository.save(announce5);
    }

}
