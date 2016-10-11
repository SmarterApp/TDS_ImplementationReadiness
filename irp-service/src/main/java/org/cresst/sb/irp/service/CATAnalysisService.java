package org.cresst.sb.irp.service;

import org.springframework.web.multipart.MultipartFile;

public interface CATAnalysisService {
    boolean validateItemCsv(MultipartFile itemFile);
}
