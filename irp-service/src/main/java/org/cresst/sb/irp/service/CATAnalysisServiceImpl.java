package org.cresst.sb.irp.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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

        Set<String> expectedHeaders = new HashSet<>();
        expectedHeaders.add("SID");
        expectedHeaders.add("ItemID");
        expectedHeaders.add("Score");

        try {
            CSVParser parser = CSVParser.parse(new String(itemFile.getBytes()), CSVFormat.EXCEL.withHeader());
            Set<String> headers = parser.getHeaderMap().keySet();
            return headers.containsAll(expectedHeaders);
        } catch (IOException e) {
            return false;
        }
    }
}
