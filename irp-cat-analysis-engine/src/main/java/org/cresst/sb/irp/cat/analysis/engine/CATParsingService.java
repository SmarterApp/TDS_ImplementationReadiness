package org.cresst.sb.irp.cat.analysis.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.MathStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;

/**
 * Parsing service to convert CAT CSV Files to java objects
 *
 */
public interface CATParsingService {

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream) throws IOException;

    List<ELAStudentScoreCAT> parseStudentELACsv(InputStream studentStream) throws IOException;
    
    List<MathStudentScoreCAT> parseStudentMathCsv(InputStream studentStream) throws IOException;

    List<PoolItemMath> parsePoolItemsMath(InputStream poolStream) throws IOException;

    List<PoolItemELA> parsePoolItemsELA(InputStream poolStream) throws IOException;

    List<TrueTheta> parseTrueThetas(InputStream thetaStream) throws IOException;

    List<BlueprintStatement> parseBlueprintCsv(InputStream blueprintStream) throws IOException;

}
