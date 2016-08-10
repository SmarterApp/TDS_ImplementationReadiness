package org.cresst.sb.irp.auto.student;

import org.cresst.sb.irp.auto.student.data.LoginInfo;
import org.cresst.sb.irp.common.data.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
@author Ernesto De La Luz Martinez
*/

@Service
public class StudentLoginImpl implements StudentLogin {
	private final static Logger logger = LoggerFactory.getLogger(StudentLoginImpl.class);

	@Autowired
	private RestTemplate studentRestTemplate;

	@Override
	public ResponseData<LoginInfo> login(String url, String sessionID, String keyValues, String forbiddenApps) {
		logger.info("login started");

		MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
		form.add("sessionID", sessionID);
		form.add("keyValues", keyValues);
		form.add("forbiddenApps", forbiddenApps);

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(form,headers);

		//LoginInfo class needs to be completed
		ResponseEntity<ResponseData<LoginInfo>> response = studentRestTemplate.exchange(url, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<ResponseData<LoginInfo>>() {
				});

		return response.getBody();
	}

}
