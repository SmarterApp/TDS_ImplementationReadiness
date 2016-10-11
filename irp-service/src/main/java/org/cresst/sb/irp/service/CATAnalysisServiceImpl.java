package org.cresst.sb.irp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    @Override
    public boolean validateItemCsv(MultipartFile itemFile) {
        logger.info("Validating CSV definition for: " + itemFile.getName());
        return false;
    }

}
