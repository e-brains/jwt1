package com.kye.jwt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController 
public class RestApiController {


	@GetMapping("home")
	public String home() {

		return "<h1>home</h1>";  
	}
	
	
//	@GetMapping("/user")
//	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//		//@AuthenticationPrincipal은 
//		System.out.println("principalDetails ==== " + principalDetails.getUser());
//		return "user";
//	}
	
	
}
