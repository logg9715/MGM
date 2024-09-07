package com.example.MultiGreenMaster.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestFRM {

    private String loginId;
    private String password;

}