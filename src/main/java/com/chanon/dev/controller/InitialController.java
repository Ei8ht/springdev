package com.chanon.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class InitialController {
	
	@RequestMapping("/index")
	public String initForm() {
		return "index";
	}
}
