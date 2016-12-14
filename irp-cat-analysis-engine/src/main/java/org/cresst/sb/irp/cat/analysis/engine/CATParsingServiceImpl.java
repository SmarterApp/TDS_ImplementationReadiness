package org.cresst.sb.irp.cat.analysis.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
     * @param inputStream
     *            InputStream that points to a csv
     * @param csvClass
     *            the java object that we want to parse as csv
     * @return A List with each row parsed as T, or null if failed to parse.
     * @throws IOException
     */
    public static <T> List<T> parseCsv(InputStream inputStream, Class<T> csvClass) throws IOException {
        return parseToMappingIterator(inputStream, csvClass).readAll();
    }

    private static <T> MappingIterator<T> parseToMappingIterator(InputStream inputStream, Class<T> csvClass) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(csvClass).withSkipFirstDataRow(true);

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
    public List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream) throws IOException {
            return parseCsv(itemFileStream, ItemResponseCAT.class);
    }

    @Override
    public List<ELAStudentScoreCAT> parseStudentELACsv(InputStream studentStream) throws IOException {
        return parseCsv(studentStream, ELAStudentScoreCAT.class);
    }

    @Override
    public List<PoolItemMath> parsePoolItemsMath(InputStream poolStream) throws IOException {
        return parseCsv(poolStream, PoolItemMath.class);
    }

    @Override
    public List<PoolItemELA> parsePoolItemsELA(InputStream poolStream) throws IOException {
        return parseCsv(poolStream, PoolItemELA.class);
    }

    @Override
    public List<TrueTheta> parseTrueThetas(InputStream thetaStream) throws IOException {
        return parseCsv(thetaStream, TrueTheta.class);
    }

    @Override
    public List<MathStudentScoreCAT> parseStudentMathCsv(InputStream studentStream) throws IOException {
        return parseCsv(studentStream, MathStudentScoreCAT.class);
    }

    @Override
    public List<BlueprintStatement> parseBlueprintCsv(InputStream blueprintStream) throws IOException {
        MappingIterator<BlueprintCsvRow> mappingIter = parseToMappingIterator(blueprintStream, BlueprintCsvRow.class);
        List<BlueprintStatement> statements = new ArrayList<>();
        BlueprintStatement statement = null;
        while(mappingIter.hasNext()) {
            final BlueprintCsvRow row = mappingIter.next();
            final String claim = row.getClaim();
            final List<String> targets = splitToList(row.getTarget());
            final String dok = row.getDok();
            final String dokGte = row.getDokGte();
            final String subject = row.getSubject();
            final String passage = row.getPassage();
            final String shortAnswer = row.getShortAnswer();
            String max = row.getMax();
            statement = new BlueprintStatement();
            statement.setSubject(subject);
            statement.setMin(Integer.parseInt(row.getMin()));
            if (max.isEmpty()) {
                statement.setMax(Integer.MAX_VALUE);
            } else {
                statement.setMax(Integer.parseInt(max));
            }
            statement.setGrade(row.getGrade());
            String spec = createSpecification(row);
            statement.setSpecification(spec);

            statement.setCondition(new BlueprintCondition() {

                @Override
                public boolean test(PoolItem item) {
                    // Currently checks against claim number and targets and
                    // dok, and passage/shortanswer for ELA subject
                    boolean claimResults = testClaim(item.getClaim(), claim);
                    boolean targetResults = orTest(item.getTarget(), targets);
                    boolean dokResults = testDok(item.getDok(), dok, dokGte);
                    // Passage and short answer only applies to ela
                    boolean passageResults = true;
                    boolean shortAnswerResults = true;
                    if (subject.equalsIgnoreCase("ela")) {
                        PoolItemELA elaItem = (PoolItemELA) item;
                        passageResults = testPassage(elaItem.getPassage(), passage);
                        shortAnswerResults = testShortAnswer(elaItem.getShortAnswer(), shortAnswer);
                    }

                    return claimResults && targetResults && dokResults && passageResults && shortAnswerResults;
                }
            });

            statements.add(statement);
        }
        return statements;
    }

    private String createSpecification(BlueprintCsvRow row) {
        StringBuilder builder = new StringBuilder();
        builder.append("Claim ");
        builder.append(row.getClaim());

        builder.append(' ');
        if (!row.getDescription().isEmpty())
            builder.append(", ");
            builder.append(row.getDescription());
        if (!row.getDok().isEmpty()) {
            builder.append(", ");
            builder.append("DOK = ");
            builder.append(row.getDok());
            builder.append(' ');
        }
        if (!row.getDokGte().isEmpty()) {
            builder.append(", ");
            builder.append("DOK >= ");
            builder.append(row.getDokGte());
            builder.append(' ');
        }

        if (!row.getTarget().isEmpty()) {
            builder.append(", ");
            builder.append("Targets: ");
            builder.append(formatTargets(row.getTarget()).replaceAll("\"", ""));
        }

        return builder.toString();
    }

    private String formatTargets(String rawTargets) {
        String results = rawTargets.replaceAll("\"", "");
        List<Integer> targets = new ArrayList<>();
        for (String target : results.split(",")) {
            try {
                targets.add(Integer.parseInt(target));
            } catch (NumberFormatException e) {
                // Target is non-numeric
                break;
            }
        }
        if (targets.size() > 0) {
            Collections.sort(targets);
            return targets.toString().replace("[", "").replace("]", "");
        }
        return results;
    }

    private boolean testShortAnswer(String itemShortAnswer, String shortAnswer) {
        if (shortAnswer.isEmpty())
            return true;

        return itemShortAnswer.equalsIgnoreCase(shortAnswer);
    }

    private boolean testPassage(String itemPassage, String passage) {
        if (passage.isEmpty())
            return true;
        return itemPassage.equalsIgnoreCase(passage);
    }

    private boolean testDok(String itemDok, String dok, String dokGte) {
        if (dok.isEmpty())
            return true;

        if (!dokGte.isEmpty()) {
            int itemDokInt = Integer.parseInt(itemDok);
            int dokGteInt = Integer.parseInt(dokGte);
            return (itemDokInt >= dokGteInt);
        }

        dok = dok.replace("\"", "");
        for (String d : dok.split(",")) {
            if (d.equals(itemDok))
                return true;
        }
        return false;
    }

    private boolean testClaim(String strClaim, String claim) {
        if (strClaim.equals(claim))
            return true;
        String[] splitString = strClaim.split("&");
        for (String claimPart : splitString) {
            if (claimPart.equals(claim))
                return true;
        }
        return false;
    }

    // Checks if target is any of the values in targets list
    private boolean orTest(String target, List<String> targets) {
        if (targets == null || targets.size() == 0)
            return true;
        
        for (String t : targets) {
            if (target.equalsIgnoreCase(t))
                return true;
        }
        return false;
    }

    private List<String> splitToList(String str) {
        str = str.replace("\"", "");
        List<String> results = new ArrayList<>();
        for (String val : str.split("\\s*,\\s*")) {
            if(!val.isEmpty()) {
                results.add(val);
            }
        }
        return results;
    }
}
