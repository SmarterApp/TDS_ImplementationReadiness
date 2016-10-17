package org.cresst.sb.irp.cat.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.cresst.sb.irp.domain.analysis.ItemCAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    /**
     *
     * @param expectedHeaders expected in the csv file
     * @param file csv file to check headers against
     * @return true if csv contains headers in `headers`, false otherwise
     */
    private boolean validateCsv(Collection<String> expectedHeaders, MultipartFile file) {
        try {
            CSVParser parser = CSVParser.parse(new String(file.getBytes()), CSVFormat.EXCEL.withHeader());
            Set<String> csvHeaders = parser.getHeaderMap().keySet();
            return csvHeaders.containsAll(expectedHeaders);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean validateItemCsv(MultipartFile itemFile) {
        logger.info("Validating CSV definition for: " + itemFile.getName());

        Set<String> expectedHeaders = new HashSet<>();
        expectedHeaders.add("SID");
        expectedHeaders.add("ItemID");
        expectedHeaders.add("Score");

        return validateCsv(expectedHeaders, itemFile);
    }

    @Override
    public boolean validateStudentCsv(MultipartFile studentFile) {
        logger.info("Validating CSV definition for: " + studentFile.getName());
        Set<String> expectedHeaders = new HashSet<>();
        expectedHeaders.add("SID");
        expectedHeaders.add("Overall");
        expectedHeaders.add("Overall_SEM");
        expectedHeaders.add("Claim1");
        expectedHeaders.add("Claim1_SEM");
        expectedHeaders.add("Claim2");
        expectedHeaders.add("Claim2_SEM");
        expectedHeaders.add("Claim3");
        expectedHeaders.add("Claim3_SEM");
        expectedHeaders.add("Claim4");
        expectedHeaders.add("Claim4_SEM");

        return validateCsv(expectedHeaders, studentFile);
    }

    public List<ItemCAT> parseItemCsv(MultipartFile itemFile) throws IOException {
        logger.info("Parsing csv for: " + itemFile.getName());

        CSVParser parser = CSVParser.parse(new String(itemFile.getBytes()), CSVFormat.EXCEL.withHeader());
        Set<String> expectedHeaders = new HashSet<>();
        expectedHeaders.add("SID");
        expectedHeaders.add("ItemID");
        expectedHeaders.add("Score");
        Set<String> csvHeaders = parser.getHeaderMap().keySet();

        if (!csvHeaders.containsAll(expectedHeaders)) {
            return null;
        }

        List<ItemCAT> parsedItems = new ArrayList<>();
        for (CSVRecord record : parser.getRecords()) {
            parsedItems.add(new ItemCAT(record.get("SID"), record.get("ItemID"), Integer.parseInt((record.get("Score")))));
        }
        return parsedItems;
    }
}
