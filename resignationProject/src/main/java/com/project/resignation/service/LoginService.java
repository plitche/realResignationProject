package com.project.resignation.service;

import com.project.resignation.vo.loginStepVO.LoginStep01VO;

public interface LoginService {

	// 회원인지 아닌지 확인
	public LoginStep01VO loginInfoCheck(LoginStep01VO loginStep01VO);
		
	
}