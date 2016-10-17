package org.cresst.sb.irp.cat.analysis;

import org.springframework.web.multipart.MultipartFile;

public interface CATAnalysisService {

    boolean validateItemCsv(MultipartFile itemFile);

    boolean validateStudentCsv(MultipartFile studentFile);

}
