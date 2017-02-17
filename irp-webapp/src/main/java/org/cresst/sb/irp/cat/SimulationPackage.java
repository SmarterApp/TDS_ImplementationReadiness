package org.cresst.sb.irp.cat;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller handles the CAT simulation package downloads. Both the item
 * pool and student data are downloaded with this controller.
 */
@Controller
public class SimulationPackage {
    private final static Logger logger = LoggerFactory.getLogger(SimulationPackage.class);

    @Value("${irp.version}")
    String irpVersion;

    @Value("${cat.datafolder}")
    private String catDataFolder;

    @RequestMapping(value = "/simupack/blueprints", method = RequestMethod.GET)
    public void simulationPackageBlueprints(HttpServletResponse response)
            throws IOException {
        InputStream blueprintDataStream = ResourceSelector.getBlueprints(catDataFolder);
        String filename = "Blueprints.csv";
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        response.setContentType("text/csv");
        FileCopyUtils.copy(blueprintDataStream, response.getOutputStream());
    }
    @RequestMapping(value="/simupack/itempool/subject/{subject}", method = RequestMethod.GET)
    public void simulationPackageItemPool(
            @PathVariable("subject") String subject,
            HttpServletResponse response) throws IOException {
        InputStream itempoolStream = ResourceSelector.getItemPool(catDataFolder, subject);
        String filename = ResourceSelector.getItemPoolFilename(subject);

        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        response.setContentType("text/csv");
        FileCopyUtils.copy(itempoolStream, response.getOutputStream());
    }

    @RequestMapping(value="/simupack/studentdata/subject/{subject}/grade/{grade}", method = RequestMethod.GET)
    public void simulationPackageStudentdata(
            @PathVariable("subject") String subject,
            @PathVariable("grade") int grade,
            HttpServletResponse response) throws IOException {
        InputStream studentDataStream = ResourceSelector.getStudentResponsesGz(catDataFolder, subject, grade);
        String filename = ResourceSelector.getStudentResponsesFilename(subject, grade);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        response.setContentType("text/csv");
        FileCopyUtils.copy(studentDataStream, response.getOutputStream());
    }
}
