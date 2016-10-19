package org.cresst.sb.irp.cat.analysis.engine;

import java.io.InputStream;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.Blueprint;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemCAT;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;

public interface CATParsingService {

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream);

    List<StudentScoreCAT> parseStudentCsv(InputStream studentStream);

    List<PoolItemCAT> parsePoolItems(InputStream poolStream);

    List<TrueTheta> parseTrueThetas(InputStream thetaStream);

    List<Blueprint> parseBlueprint(InputStream blueprintStream);

}
