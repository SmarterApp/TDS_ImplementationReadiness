package org.cresst.sb.irp.cat;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SimulationPackage {
    private final static Logger logger = LoggerFactory.getLogger(SimulationPackage.class);

    @Value("${irp.version}")
    String irpVersion;

    @RequestMapping(value="/simupack/elag11/itempool", method = RequestMethod.GET)
    public void simulationPackageItemPool(HttpServletResponse response) throws IOException {
        String itempool = "Math.cat.full_20150423.csv";
        InputStream itempoolStream = getClass().getResourceAsStream("/CAT_simulation_packages/ELAG11/" + itempool);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", itempool));
        response.setContentType("text/csv");
        FileCopyUtils.copy(itempoolStream, response.getOutputStream());
    }

    @RequestMapping(value="/simupack/elag11/blueprint", method = RequestMethod.GET)
    public void simulationPackageBlueprint(HttpServletResponse response) throws IOException {
        String blueprint = "ELAG11_CAT_BluePrint.csv";
        InputStream blueprintStream = getClass().getResourceAsStream("/CAT_simulation_packages/ELAG11/" + blueprint);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", blueprint));
        response.setContentType("text/csv");
        FileCopyUtils.copy(blueprintStream, response.getOutputStream());
    }

    @RequestMapping(value="/simupack/elag11/studentdata", method = RequestMethod.GET)
    public void simulationPackageStudentdata(HttpServletResponse response) throws IOException {
        String studentData = "CATResponses_WeightTuningELA11N1000ASL_no_resp.csv";
        InputStream studentDataStream = getClass().getResourceAsStream("/CAT_simulation_packages/ELAG11/" + studentData);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", studentData));
        response.setContentType("text/csv");
        FileCopyUtils.copy(studentDataStream, response.getOutputStream());
    }
}
