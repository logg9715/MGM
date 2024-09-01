package com.example.Flower.controller;

import com.example.Flower.dto.CommentResponse;
import com.example.Flower.dto.UserForm;
import com.example.Flower.entity.Friend;
import com.example.Flower.entity.User;
import com.example.Flower.entity.UserRole;
import com.example.Flower.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UserController extends SessionCheckController {

    @Autowired
    private UserService userService; // UserService 의존성 주입

    @ModelAttribute
    public void addAttributes(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        if (userId != null) {
            User loginUser = userService.findUserById(userId);
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
    }

    @ModelAttribute
    public void adminUserCheck(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        if (userId != null) {
            User loginUser = userService.findUserById(userId);
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
                model.addAttribute("isAdmin", loginUser.getRole().equals(UserRole.ADMIN));
            }
        }
    }

    @GetMapping("/user/new")
    public String newUserForm() {
        return "users/new"; // 새 사용자 폼 페이지로 이동
    }

    @GetMapping("/user")
    public String userIndex(Model model) {
        List<User> userEntityList = userService.findAllUsers(); // 모든 사용자 목록을 모델에 추가
        model.addAttribute("userList", userEntityList);
        return "users/index"; // 사용자 인덱스 페이지로 이동
    }

    @GetMapping("/user/inactive")
    public String inactiveUserIndex(Model model) {
        List<User> inactiveUserList = userService.findInactiveUsers(); // 비활성화된 사용자 목록을 모델에 추가
        model.addAttribute("inactiveUserList", inactiveUserList);
        return "users/inactive_index"; // 비활성화된 사용자 목록 페이지로 이동
    }

    @GetMapping("/user/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User userEntity = userService.findUserById(id); // 특정 사용자의 정보를 가져오기
        model.addAttribute("user", userEntity);
        return "users/edit"; // 사용자 편집 페이지로 이동
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model, @SessionAttribute(name = "userId", required = false) Long loggedInUserId) {
        log.info("id = " + id);

        User userEntity = userService.findUserById(id);

        List<User> friends = new ArrayList<>();
        for (Friend friend : userEntity.getFriends()) {
            friends.add(friend.getFriend());
        }

        List<User> allUsers = userService.findAllUsers();
        // 필터링: 로그인한 사용자와 이미 친구인 사용자 제외
        List<User> availableFriends = allUsers.stream()
                .filter(u -> !u.getId().equals(id) && !friends.contains(u))
                .collect(Collectors.toList());

        model.addAttribute("user", userEntity);
        model.addAttribute("allUsers", availableFriends);
        model.addAttribute("friends", friends);

        if (userEntity.getActive() == 1) {
            return "users/show"; // 활성화된 사용자 페이지로 이동
        } else {
            return "users/inactive_show"; // 비활성화된 사용자 페이지로 이동
        }
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes rttr) {
        userService.deactivateUser(id); // 사용자 비활성화 메서드 호출
        rttr.addFlashAttribute("msg", "계정이 삭제되었습니다!"); // 메시지 추가
        return "redirect:/user"; // 사용자 목록 페이지로 리다이렉트
    }

    @GetMapping("/user/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes rttr) {
        User user = userService.activateUser(id);
        if (user != null) {
            rttr.addFlashAttribute("msg", "계정을 되살렸습니다!");
        } else {
            rttr.addFlashAttribute("msg", "계정을 되살릴 수 없습니다.");
        }
        return "redirect:/user"; // 사용자 목록 페이지로 리다이렉트
    }

    @GetMapping("/user/{id}/guestbook")
    public String getUserGuestbook(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            return "redirect:/"; // 유저가 없으면 홈으로 리다이렉트
        }

        List<CommentResponse> comments = userService.getUserCommentsAndRecomments(id);

        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        return "guestbook/guestbookpage"; // 게스트북 페이지로 이동
    }

    @PostMapping("/user/{id}/add-friend")
    public String addFriend(@PathVariable Long id, @RequestParam Long friendId, RedirectAttributes redirectAttributes) {
        try {
            userService.addFriend(id, friendId); // 친구 추가 메서드 호출
            redirectAttributes.addFlashAttribute("msg", "친구가 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            log.error("친구 추가 중 오류 발생: " + e.getMessage());
            redirectAttributes.addFlashAttribute("msg", "친구를 추가하는 중 오류가 발생했습니다.");
        }
        return "redirect:/user/" + id; // 유저 페이지로 리다이렉트
    }

    @PostMapping("/user/{id}/remove-friend")
    public String removeFriend(@PathVariable Long id, @RequestParam Long friendId, RedirectAttributes redirectAttributes) {
        try {
            userService.removeFriend(id, friendId); // 친구 삭제 메서드 호출
            redirectAttributes.addFlashAttribute("msg", "친구가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("친구 삭제 중 오류 발생: " + e.getMessage());
            redirectAttributes.addFlashAttribute("msg", "친구를 삭제하는 중 오류가 발생했습니다.");
        }
        return "redirect:/user/" + id; // 유저 페이지로 리다이렉트
    }
}
