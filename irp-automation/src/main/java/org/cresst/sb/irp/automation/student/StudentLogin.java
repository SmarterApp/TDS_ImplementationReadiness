package org.cresst.sb.irp.automation.student;

import org.cresst.sb.irp.automation.student.data.LoginInfo;
import org.cresst.sb.irp.common.data.ResponseData;
 

/**
@author Ernesto De La Luz Martinez
*/

public interface StudentLogin {
	 
	ResponseData<LoginInfo> login(String url, String sessionID, String keyValues,String forbiddenApps);
}