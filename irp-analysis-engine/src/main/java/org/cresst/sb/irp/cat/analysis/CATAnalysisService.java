package org.cresst.sb.irp.cat.analysis;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.domain.analysis.PoolItemCAT;
import org.cresst.sb.irp.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.domain.analysis.TrueTheta;

public interface CATAnalysisService {

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream);

    List<StudentScoreCAT> parseStudentCsv(InputStream studentStream);

    List<PoolItemCAT> parsePoolItems(InputStream poolStream);

    List<TrueTheta> parseTrueThetas(InputStream thetaStream);

}
