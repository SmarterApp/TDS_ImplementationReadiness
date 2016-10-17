package org.cresst.sb.irp.cat.analysis;

import java.io.IOException;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.domain.analysis.StudentScoreCAT;
import org.springframework.web.multipart.MultipartFile;

public interface CATAnalysisService {

    boolean validateItemCsv(MultipartFile itemFile);

    boolean validateStudentCsv(MultipartFile studentFile);

    List<ItemResponseCAT> parseItemCsv(MultipartFile itemFile) throws IOException;

    List<StudentScoreCAT> parseStudentCsv(MultipartFile studentFile) throws IOException;

}
