package com.project.resignation.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.resignation.dao.StudyDao;

@Service
public class StudyDaoImpl implements StudyDao {
	
	@Autowired
	SqlSession sqlsession;
	
	@Override
	public List<Map<String, Object>> goStudyMain() {
		List<Map<String, Object>> studyList = sqlsession.selectList("Study.getStudyInfoList");
		
		for (Map<String, Object> studyItem : studyList) {
			// DB에서 조회한 date타입의 날짜를 2021. 10. 29 형태로 formatting
			SimpleDateFormat changeDate = new SimpleDateFormat("yyyy.mm.dd");
			String tempStartDate = changeDate.format(studyItem.get("DTSTARTDATE"));
			String tempEndDate = changeDate.format(studyItem.get("DTSTARTDATE"));
			studyItem.replace("DTSTARTDATE", tempStartDate);
			studyItem.replace("DTENDDATE", tempEndDate);
		}
		
		return studyList;
	}
	
	@Override
	public Map<String, Object> getEachStudyInfo(int iStudyNo) throws Exception {
		Map<String, Object> eachStudyInfo = sqlsession.selectOne("Study.getEachStudyInfo", iStudyNo);
		
		// todo 게시글 insert시 hit 0으로 insert 시키기
		
		try {
			SimpleDateFormat changeDate = new SimpleDateFormat("yyyy-mm-dd");
			
			// 자바 날짜 계산
	        // 두 날짜를 parse()를 통해 Date형으로 변환.
	        // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
	        // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
			String tempStartDate = changeDate.format(eachStudyInfo.get("DTSTARTDATE"));
			String tempEndDate = changeDate.format(eachStudyInfo.get("DTENDDATE"));
			eachStudyInfo.replace("DTSTARTDATE", tempStartDate);
			eachStudyInfo.replace("DTENDDATE", tempEndDate);
			long duration = changeDate.parse(tempStartDate).getTime() - changeDate.parse(tempEndDate).getTime();
			
			eachStudyInfo.put("duration", duration);	
		} catch (Exception e) {
			e.printStackTrace();
			eachStudyInfo.put("duration", "-");
		}
		
		return eachStudyInfo;
	}
	
	@Override
	public List<Map<String, Object>> getStudyParticipants(int iStudyNo) {
		List<Map<String, Object>> returnMapList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> needPosition = sqlsession.selectList("Study.getStudyNeedPosition", iStudyNo);
		List<Map<String, Object>> applyPosition = sqlsession.selectList("Study.getApplyStudyPosition", iStudyNo);
		
		if (needPosition.size() < applyPosition.size()) {
			// todo 에러 띄우기
		}

		boolean isApply = false;
		for (Map<String, Object> tempNeed : needPosition) {
			Map<String, Object> positionMap = new HashMap<>();
			String needPostionName = (String)tempNeed.get("VCPOSITION");
			
			for (Map<String, Object> tempApply : applyPosition) {
				String applyPositionName = (String)tempApply.get("VCPOSITION");
				
				// 참여 예정자가 있는/없는 경우
				if (needPostionName.equals(applyPositionName)) {
					positionMap.put("APPLYSTATUS", tempApply.get("APPLYCOUNT") + "/" + tempNeed.get("NEEDCOUNT"));
					isApply = true;
					break;
				} else {
					isApply = false;
				}
			}
			
			positionMap.put("VCPOSITION", needPostionName);
			// 참여 예정자가 없는경우 0으로 put
			if (!isApply) {
				positionMap.put("APPLYSTATUS", "0/" + tempNeed.get("NEEDCOUNT"));
			}
			
			returnMapList.add(positionMap);
		}

		return returnMapList;
	}
}
