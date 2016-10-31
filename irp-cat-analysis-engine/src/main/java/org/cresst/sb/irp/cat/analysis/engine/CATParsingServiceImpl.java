package org.cresst.sb.irp.cat.analysis.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.Blueprint;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Service
public class CATParsingServiceImpl implements CATParsingService {
    private final static Logger logger = LoggerFactory.getLogger(CATParsingServiceImpl.class);

    private <T > List<T> parseCATCsv(InputStream inputStream, Class<T> csvClass) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(csvClass).withHeader();

        try {
            MappingIterator<T> it = mapper.readerFor(csvClass)
                    .with(schema)
                    .readValues(inputStream);
            return it.readAll();
        } catch (IOException e) {
            logger.error("Error parsing csv for: " + csvClass.getName());
        }
        return null;
    }

    @Override
    public List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream) {
            return parseCATCsv(itemFileStream, ItemResponseCAT.class);
    }

    @Override
    public List<StudentScoreCAT> parseStudentCsv(InputStream studentStream) {
        return parseCATCsv(studentStream, StudentScoreCAT.class);
    }

    @Override
    public List<PoolItemMath> parsePoolItemsMath(InputStream poolStream) {
        return parseCATCsv(poolStream, PoolItemMath.class);
    }

    @Override
    public List<PoolItemELA> parsePoolItemsELA(InputStream poolStream) {
        return parseCATCsv(poolStream, PoolItemELA.class);
    }

    @Override
    public List<TrueTheta> parseTrueThetas(InputStream thetaStream) {
        return parseCATCsv(thetaStream, TrueTheta.class);
    }

    @Override
    public List<Blueprint> parseBlueprint(InputStream blueprintStream) {
        return parseCATCsv(blueprintStream, Blueprint.class);
    }
}
