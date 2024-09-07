package com.example.MultiGreenMaster.controller;

import com.example.MultiGreenMaster.entity.User_RoleENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.UserSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

/******************************************************************************
 *                                                                             *
 *                                                                             *
 *                                                                             *
 * 사용자 로그인 상태 확인하는 메소드                                          *
 *                                                                             *
 * 1. 모든 컨트롤러에 상속해서 사용                                            *
 * 2. 상속시키면, 해당 컨트롤러에 요청이 들어올때마다 @ModelAttribute가 붙은   *
 * 메소드들이 자동으로 실행됨.                                                 *
 * 3. 자동 실행 메소드에서 세션 정보를 읽고 모델에 담는다                      *
 * (근데 이건 머스태치 용도임)                                                 *
 *                                                                             *
 *                                                                             *
 *                                                                             *
 ******************************************************************************/

public class SessionCheckCTL {
    // 로그인 한 아이디 체크를 위한 코드
    @Autowired // UserService Bean 객체를 주입
    private UserSRV userService;
    @ModelAttribute//공통 데이터를 모델에 추가할 때 @ModelAttribute를 사용하여 컨트롤러 레벨에서 공통 처리를 할 수 있습니다.
    public void addAttributes(Model model,
                              @SessionAttribute(name="userId", required=false) Long userId) {
        if (userId != null) {
            UserENT loginUser = userService.getLoginUserById(userId); // userId로 로그인한 사용자 정보 조회
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname()); // 사용자 닉네임을 모델에 추가
                model.addAttribute("isOwner", loginUser.getId()); // 사용자 id를 모델에 추가
            }
        }
    }
    // 로그인 한 아이디가 관리자 여부인가를 체크
    @ModelAttribute
    public void adminUserCheck(Model model, @SessionAttribute(name="userId", required=false) Long userId) {
        System.out.println("@@@@@@@@@ 어드민체크 \n");
        if (userId != null) {
            UserENT loginUser = userService.getLoginUserById(userId);
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
                if (loginUser.getRole().equals(User_RoleENT.ADMIN)) {
                    model.addAttribute("isAdmin", true);
                } else {
                    model.addAttribute("isAdmin", false);
                }
            }
        }
    }
}
