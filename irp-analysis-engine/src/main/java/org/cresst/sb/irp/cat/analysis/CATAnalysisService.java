package org.cresst.sb.irp.cat.analysis;

import java.io.InputStream;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.domain.analysis.StudentScoreCAT;
import org.springframework.web.multipart.MultipartFile;

public interface CATAnalysisService {

    List<ItemResponseCAT> parseItemCsv(InputStream itemFileStream);

    List<StudentScoreCAT> parseStudentCsv(InputStream studentStream);

}
