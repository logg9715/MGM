package com.example.Flower.service;

import com.example.Flower.dto.CommentResponse;
import com.example.Flower.dto.JoinRequest;
import com.example.Flower.dto.LoginRequest;
import com.example.Flower.dto.UserForm;
import com.example.Flower.entity.CMComment;
import com.example.Flower.entity.CMRecomment;
import com.example.Flower.entity.Friend;
import com.example.Flower.entity.User;
import com.example.Flower.repository.CMCommentRepository;
import com.example.Flower.repository.CMRecommentRepository;
import com.example.Flower.repository.FriendRepository;
import com.example.Flower.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service // 서비스 클래스임을 선언
@Transactional // 트랜잭션 관리를 위한 어노테이션
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성해주는 Lombok 어노테이션
public class UserService {
    @Autowired
    private UserRepository userRepository; // UserRepository 의존성 주입

    public User getLoginUserById(Long userId) {
        if (userId == null) return null; // userId가 null인 경우 null 반환

        Optional<User> optionalUser = userRepository.findById(userId); // ID로 사용자 조회
        if (optionalUser.isEmpty()) return null; // 사용자가 존재하지 않으면 null 반환

        return optionalUser.get(); // 사용자가 존재하면 User 객체 반환
    }

     //로그인 처리 메서드
     public User login(LoginRequest req) {
         Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId()); // 로그인 ID로 사용자 조회
         if (optionalUser.isEmpty()) {
             return null; // 사용자가 존재하지 않으면 null 반환
         }

         User user = optionalUser.get();

         // 계정이 비활성화 상태인지 확인
         if (user.getActive() == 0) {
             log.info("User account is deactivated: " + user.getLoginId());
             return null; // 계정이 비활성화 상태이면 null 반환
         }

         if (!user.getPassword().equals(req.getPassword())) {
             return null; // 비밀번호가 일치하지 않으면 null 반환
         }

         return user; // 로그인 성공 시 User 객체 반환
     }

    //회원 가입 메서드
    public void join(JoinRequest req) {
        userRepository.save(req.toEntity()); // JoinRequest 객체를 엔티티로 변환하여 저장
    }

    //활성화된 사용자만 조회
    public List<User> findAllUsers() {
        return userRepository.findAllActiveUsers();
    }

    //비활성화된 사용자만 조회
    @Transactional
    public List<User> findInactiveUsers() {
        // 활성화 상태(active가 0인) 사용자를 조회
        return userRepository.findByActive(0);
    }

    public User findUserById(Long id) {
        // 특정 ID의 사용자 정보를 조회
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        // 새로운 사용자를 저장
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserForm form) {
        log.info("Updating user with ID: " + id);  // 로그 추가
        // 특정 ID의 사용자 정보를 업데이트
        User userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            userEntity.setLoginId(form.getLoginId());
            userEntity.setPassword(form.getPassword());
            userEntity.setNickname(form.getNickname());
            userEntity.setName(form.getName());
            userEntity.setAge(form.getAge());
            userEntity.setPhonenumber(form.getPhonenumber());
            userEntity.setEmail(form.getEmail());
            userEntity.setRole(form.getRole());
            log.info("User updated: " + userEntity);  // 로그 추가
            return userRepository.save(userEntity);
        }
        log.info("User not found with ID: " + id);  // 로그 추가
        return null;
    }

    //사용자 삭제
//    @Transactional
//    public void deleteUser(Long id) {
//        // 특정 ID의 사용자를 삭제
//        User target = userRepository.findById(id).orElse(null);
//        if (target != null) {
//            userRepository.delete(target);
//        }
//    }

    //사용자 비활성화
    @Transactional
    public void deactivateUser(Long id) {
        User target = userRepository.findById(id).orElse(null);
        if (target != null) {
            target.setActive(0); // 비활성화
            userRepository.save(target); // 변경된 사용자 정보를 저장
        }
    }

    //사용자 활성화
    @Transactional
    public User activateUser(Long id) {
        // 특정 ID의 사용자 정보를 조회
        User userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null && userEntity.getActive() == 0) {
            // 사용자가 비활성화 상태일 때만 활성화
            userEntity.setActive(1);
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



    private final CMCommentRepository commentRepository;
    private final CMRecommentRepository recommentRepository;

    //사용자의 댓글과 대댓글을 가져와 하나의 리스트로 변환
    public List<CommentResponse> getUserCommentsAndRecomments(Long userId) {
        // 사용자의 댓글을 가져오기
        List<CMComment> comments = commentRepository.findByUser_Id(userId);

        // 사용자의 대댓글을 가져오기
        List<CMRecomment> recomments = recommentRepository.findByUser_Id(userId);

        // 댓글과 대댓글을 하나의 리스트로 병합
        List<CommentResponse> responses = new ArrayList<>();

        comments.forEach(comment -> {
            CommentResponse response = new CommentResponse();
            response.setId(comment.getId());
            response.setContent(comment.getContent());
            response.setRegdate(comment.getRegdate());
            response.setLikeCount(comment.getLikeCount());
            response.setType("comment");
            responses.add(response);
        });

        recomments.forEach(recomment -> {
            CommentResponse response = new CommentResponse();
            response.setId(recomment.getId());
            response.setContent(recomment.getContent());
            response.setRegdate(recomment.getRegdate());
            response.setLikeCount(recomment.getLikeCount());
            response.setType("recomment");
            responses.add(response);
        });

        // regdate 기준으로 내림차순 정렬
        return responses.stream()
                .sorted((r1, r2) -> r2.getRegdate().compareTo(r1.getRegdate()))
                .collect(Collectors.toList());
    }
    @Autowired
    private FriendRepository friendRepository;

    //사용자와 친구 데이터를 미리 로드하여 컨트롤러로 전달
    @Transactional
    public User findUserWithFriends(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // friends를 초기화하여 지연 로딩을 방지
        user.getFriends().size();
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // 이미 친구인지 확인
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new RuntimeException("Already friends");
        }

        // 친구 추가
        Friend newFriend = new Friend();
        newFriend.setUser(user); // 로그인한 사용자
        newFriend.setFriend(friend); // 추가할 친구
        friendRepository.save(newFriend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // 친구 삭제
        Optional<Friend> existingFriendOpt = Optional.ofNullable(friendRepository.findByUserAndFriend(user, friend));
        if (!existingFriendOpt.isPresent()) {
            throw new RuntimeException("Friend relationship not found");
        }

        friendRepository.delete(existingFriendOpt.get());
    }

}
