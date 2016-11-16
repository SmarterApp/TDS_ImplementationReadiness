package org.cresst.sb.irp.cat.analysis.engine;

import java.io.InputStream;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.Blueprint;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;

/**
 * Parsing service to convert CAT CSV Files to java objects
 *
 */
public interface CATParsingService {

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream);

    List<StudentScoreCAT> parseStudentCsv(InputStream studentStream);

    List<PoolItemMath> parsePoolItemsMath(InputStream poolStream);

    List<PoolItemELA> parsePoolItemsELA(InputStream poolStream);

    List<TrueTheta> parseTrueThetas(InputStream thetaStream);

    List<Blueprint> parseBlueprint(InputStream blueprintStream);

}
