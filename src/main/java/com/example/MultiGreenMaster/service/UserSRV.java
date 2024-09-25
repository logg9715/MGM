package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM_V2;
import com.example.MultiGreenMaster.dto.LoginRequestFRM;
import com.example.MultiGreenMaster.dto.UserFRM;
import com.example.MultiGreenMaster.entity.FreeBoardCommentENT;
import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.repository.FreeBoardCommentREP;
import com.example.MultiGreenMaster.repository.FreeBoardREP;
import com.example.MultiGreenMaster.repository.FriendREP;
import com.example.MultiGreenMaster.repository.UserREP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service // 서비스 클래스임을 선언
@Transactional // 트랜잭션 관리를 위한 어노테이션
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성해주는 Lombok 어노테이션
public class UserSRV {
    @Autowired
    private UserREP userRepository; // UserRepository 의존성 주입

    @Autowired
    private FreeBoardCommentREP freeBoardCommentREP;

    public UserENT getLoginUserById(Long userId) {
        if (userId == null) return null; // userId가 null인 경우 null 반환

        Optional<UserENT> optionalUser = userRepository.findById(userId); // ID로 사용자 조회
        if (optionalUser.isEmpty()) return null; // 사용자가 존재하지 않으면 null 반환

        return optionalUser.get(); // 사용자가 존재하면 User 객체 반환
    }

     //로그인 처리 메서드
     public UserENT login(LoginRequestFRM req) {
         Optional<UserENT> optionalUser = userRepository.findByLoginId(req.getLoginId()); // 로그인 ID로 사용자 조회
         if (optionalUser.isEmpty()) {
             return null; // 사용자가 존재하지 않으면 null 반환
         }

         UserENT user = optionalUser.get();

         // 계정이 비활성화 상태인지 확인
         if (user.isDisable()) {
             log.info("User account is deactivated: " + user.getLoginId());
             return null; // 계정이 비활성화 상태이면 null 반환
         }

         if (!user.getPassword().equals(req.getPassword())) {
             return null; // 비밀번호가 일치하지 않으면 null 반환
         }

         return user; // 로그인 성공 시 User 객체 반환
     }

    //활성화된 사용자만 조회
    public List<UserENT> findAllUsers() {
        return userRepository.findByDisableFalse();
    }

    //비활성화된 사용자만 조회
    @Transactional
    public List<UserENT> findInactiveUsers() {
        // 활성화 상태(active가 0인) 사용자를 조회
        return userRepository.findByDisableFalse();
    }

    public UserENT findUserById(Long id) {
        // 특정 ID의 사용자 정보를 조회
        return userRepository.findById(id).orElse(null);
    }

    /* 회원가입 */
    @Transactional
    public UserENT saveUser(UserENT user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserENT updateUser(UserFRM form) {
        UserENT newData = form.toEntity();
        UserENT target = userRepository.findById(newData.getId()).orElse(null);

        /* 업데이트 대상이 없으면 null 반환 */
        if (target == null) return null;

        /* 중복 아이디 혹은 닉네임이면 null 반환 */
        String _loginId = newData.getLoginId();
        if(_loginId != null && !_loginId.equals(target.getLoginId()) &&userRepository.findByLoginId(_loginId).isPresent())
            return null;

        String _nickname = newData.getNickname();
        if(_nickname != null && !_nickname.equals(target.getNickname()) && userRepository.findByNickname(_nickname).isPresent())
            return null;

        /* 패치 & 저장 */
        target.patch(newData);
        return userRepository.save(target);
    }

    /* 사용자 비활성화 */
    @Transactional
    public UserENT deactivateUser(Long id) {
        UserENT target = userRepository.findById(id).orElse(null);
        if (target != null) {
            target.setDisable(true); // 비활성화
            return userRepository.save(target); // 변경된 사용자 정보를 저장
        }
        return null;
    }

    /* 사용자 활성화 */
    @Transactional
    public UserENT activateUser(Long id) {
        // 특정 ID의 사용자 정보를 조회
        UserENT userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            userEntity.setDisable(false);
            return userRepository.save(userEntity); // 사용자 정보를 업데이트하여 저장
        }
        return null; // 사용자가 없거나 이미 활성화된 상태일 경우 null 반환
    }

    //중복 아이디 체크
    public boolean isLoginIdDuplicate(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }
    //중복 닉네임 체크
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public List<FreeBoardCommentFRM_V2> getUserCommentsAndRecommentsLast3(Long userId) {
        // 사용자의 댓글을 가져오기
        List<FreeBoardCommentENT> comments = freeBoardCommentREP.findRecentCommentsByUserId(userId);

        // 댓글과 대댓글을 하나의 리스트로 병합
        List<FreeBoardCommentFRM_V2> responses = new ArrayList<>();

        comments.forEach(comment -> {
            FreeBoardCommentFRM_V2 response = new FreeBoardCommentFRM_V2();
            response.setCmPostId(comment.getCmPost().getId());
            response.setId(comment.getId());
            response.setContent(comment.getContent());
            response.setRegdate(comment.getRegdate().withNano(0));
            responses.add(response);
        });

        // regdate 기준으로 내림차순 정렬
        return responses.stream()
                .sorted((r1, r2) -> r2.getRegdate().compareTo(r1.getRegdate()))
                .collect(Collectors.toList());
    }

    //사용자와 친구 데이터를 미리 로드하여 컨트롤러로 전달
    @Transactional
    public UserENT findUserWithFriends(Long id) {
        UserENT user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // friends를 초기화하여 지연 로딩을 방지
        user.getFriends().size();
        return user;
    }
}
