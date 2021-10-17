package com.project.resignation.controller.joinController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.resignation.service.JoinService;
import com.project.resignation.vo.joinStepVO.JoinStep01VO;

@Controller
@RequestMapping(value="/join")
public class joinController {

	@Autowired
	JoinService joinService;
	
	@RequestMapping(value="/step01",
							   method=RequestMethod.POST,
							   produces="application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> joinStep01(@RequestBody JoinStep01VO joinStep01VO, Model model) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JoinStep01VO checkResult = joinService.checkMember(joinStep01VO);
		
		// 아이디가 존재하면 
		if (checkResult != null) {
			System.out.println("아이디가 존재하네? 존재하면 이미 아이디가 존재한다고 뱉어야지");
			map.put("success", "N");
		// 아이디가 존재하지 않으면 회원가입시켜준다.
		} else {
			System.out.println("아이디가 존재하지 않으니 회원가입시켜라.");
			int insertResult = joinService.insertMemberInfo(joinStep01VO);
			// 회원가입이 성공하면
			if (insertResult == 1) {
				map.put("success", "Y");
			}
		}
		
		
		map.put("email", joinStep01VO.getEmail());
		
		//model.addAttribute(attributeName, attributeValue);
		return map;
	}
	
}
