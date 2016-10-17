package org.cresst.sb.irp.cat;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SimulationPackage {
    private final static Logger logger = LoggerFactory.getLogger(SimulationPackage.class);

    @Value("${irp.version}")
    String irpVersion;

    @Value("${cat.math.itempool}")
    private Resource itemPool;

    @Value("${cat.elag11.blueprint}")
    private Resource blueprint;

    @Value("${cat.elag11.studentdata}")
    private Resource studentData;

    @RequestMapping(value="/simupack/elag11/itempool", method = RequestMethod.GET)
    public void simulationPackageItemPool(HttpServletResponse response) throws IOException {
        InputStream itempoolStream = itemPool.getInputStream();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", itemPool.getFilename()));
        response.setContentType("text/csv");
        FileCopyUtils.copy(itempoolStream, response.getOutputStream());
    }

    @RequestMapping(value="/simupack/elag11/blueprint", method = RequestMethod.GET)
    public void simulationPackageBlueprint(HttpServletResponse response) throws IOException {
        InputStream blueprintStream = blueprint.getInputStream();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", blueprint.getFilename()));
        response.setContentType("text/csv");
        FileCopyUtils.copy(blueprintStream, response.getOutputStream());
    }

    @RequestMapping(value="/simupack/elag11/studentdata", method = RequestMethod.GET)
    public void simulationPackageStudentdata(HttpServletResponse response) throws IOException {
        InputStream studentDataStream = studentData.getInputStream();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", studentData.getFilename()));
        response.setContentType("text/csv");
        FileCopyUtils.copy(studentDataStream, response.getOutputStream());
    }
}
