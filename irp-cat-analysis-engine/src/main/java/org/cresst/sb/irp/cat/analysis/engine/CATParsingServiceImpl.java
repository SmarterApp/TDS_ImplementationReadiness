package org.cresst.sb.irp.cat.analysis.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintCsvRow;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.MathStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
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

    /**
     * 
     * @param inputStream InputStream that points to a csv
     * @param csvClass the java object that we want to parse as csv
     * @return A List with each row parsed as T, or null if failed to parse.
     */
    public static <T> List<T> parseCsv(InputStream inputStream, Class<T> csvClass) {
        try {
            return parseToMappingIterator(inputStream, csvClass).readAll();
        } catch (IOException e) {
            logger.error("Could not get all elements from MappingIterator from csv: {}", e.getMessage());
            return null;
        }
    }

    private static <T> MappingIterator<T> parseToMappingIterator(InputStream inputStream, Class<T> csvClass) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(csvClass).withHeader();

        try {
            MappingIterator<T> it = mapper.readerFor(csvClass)
                    .with(schema)
                    .readValues(inputStream);
            return it;
        } catch (IOException e) {
            logger.error("Error parsing csv for: " + csvClass.getName());
        }
        return null;
    }

    @Override
    public List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream) {
            return parseCsv(itemFileStream, ItemResponseCAT.class);
    }

    @Override
    public List<ELAStudentScoreCAT> parseStudentELACsv(InputStream studentStream) {
        return parseCsv(studentStream, ELAStudentScoreCAT.class);
    }

    @Override
    public List<PoolItemMath> parsePoolItemsMath(InputStream poolStream) {
        return parseCsv(poolStream, PoolItemMath.class);
    }

    @Override
    public List<PoolItemELA> parsePoolItemsELA(InputStream poolStream) {
        return parseCsv(poolStream, PoolItemELA.class);
    }

    @Override
    public List<TrueTheta> parseTrueThetas(InputStream thetaStream) {
        return parseCsv(thetaStream, TrueTheta.class);
    }

    @Override
    public List<MathStudentScoreCAT> parseStudentMathCsv(InputStream studentStream) {
        return parseCsv(studentStream, MathStudentScoreCAT.class);
    }

    @Override
    public List<BlueprintStatement> parseBlueprintCsv(InputStream blueprintStream) {
        MappingIterator<BlueprintCsvRow> mappingIter = parseToMappingIterator(blueprintStream, BlueprintCsvRow.class);
        List<BlueprintStatement> statements = new ArrayList<>();
        BlueprintStatement statement = null;
        while(mappingIter.hasNext()) {
            final BlueprintCsvRow row = mappingIter.next();
            final int claim = row.getClaim();
            final List<String> targets = splitToList(row.getTarget());
            statement = new BlueprintStatement();
            statement.setMin(row.getMin());
            statement.setMax(row.getMax());
            statement.setGrade(row.getGrade());
            String spec = String.format("Claim %d: %s", claim, row.getDescription());
            statement.setSpecification(spec);

            statement.setCondition(new BlueprintCondition() {
                final String strClaim = String.valueOf(claim);

                @Override
                public boolean test(PoolItem item) {
                    // Currently only check against claim number and targets
                    boolean targetResults = targetResults(item.getTarget(), targets);
                    return item.getClaim().equals(strClaim) && targetResults;
                }
            });

            statements.add(statement);
        }
        return statements;
    }

    // Checks if target is any of the values in targets list
    private boolean targetResults(String target, List<String> targets) {
        if (targets == null || targets.size() == 0)
            return true;
        
        for (String t : targets) {
            if (target.equalsIgnoreCase(t))
                return true;
        }
        return false;
    }

    private List<String> splitToList(String str) {
        List<String> results = new ArrayList<>();
        for (String val : str.split("\\s*,\\s*")) {
            if(!val.isEmpty()) {
                results.add(val);
            }
        }
        return results;
    }
}
