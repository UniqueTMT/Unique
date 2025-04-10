package com.unique.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UsersController {

	// 1. 기본 로그인 폼
	// http://localhost:5000/users/login_form
	@RequestMapping(value = "/login_form", method = RequestMethod.GET)
	public String ctlUsersLoiginForm() {
		return "th_user/login_form";
	}


}
