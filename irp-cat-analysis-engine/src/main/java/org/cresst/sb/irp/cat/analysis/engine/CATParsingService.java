package org.cresst.sb.irp.cat.analysis.engine;

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

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream);

    List<ELAStudentScoreCAT> parseStudentELACsv(InputStream studentStream);
    
    List<MathStudentScoreCAT> parseStudentMathCsv(InputStream studentStream);

    List<PoolItemMath> parsePoolItemsMath(InputStream poolStream);

    List<PoolItemELA> parsePoolItemsELA(InputStream poolStream);

    List<TrueTheta> parseTrueThetas(InputStream thetaStream);

    List<BlueprintStatement> parseBlueprintCsv(InputStream blueprintStream);

}
