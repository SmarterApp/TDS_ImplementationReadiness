package org.cresst.sb.irp.cat.analysis;

import java.io.IOException;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.domain.analysis.StudentScoreCAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    @Override
    public List<ItemResponseCAT> parseItemCsv(MultipartFile itemFile) {
        logger.info("Parsing item csv for: " + itemFile.getName());

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(ItemResponseCAT.class).withHeader();

        try {
            MappingIterator<ItemResponseCAT> it = mapper.readerFor(ItemResponseCAT.class)
                    .with(schema)
                    .readValues(itemFile.getInputStream());
            return it.readAll();
        } catch (IOException e) {
            logger.error("Error parsing csv: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<StudentScoreCAT> parseStudentCsv(MultipartFile studentFile) {
        logger.info("Parsing item csv for: " + studentFile.getName());

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(StudentScoreCAT.class).withHeader();

        try {
            MappingIterator<StudentScoreCAT> it = mapper.readerFor(StudentScoreCAT.class)
                    .with(schema)
                    .readValues(studentFile.getInputStream());
            return it.readAll();
        } catch (IOException e) {
            logger.error("Error parsing csv: " + e.getMessage());
        }
        return null;
    }
}
