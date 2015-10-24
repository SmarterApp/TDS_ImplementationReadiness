package org.cresst.sb.irp.itemscoring.rubric;

import com.google.common.io.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Loads the contents of machine rubrics from the file system
 */
@Service
public class MachineRubricFileSystemLoader implements MachineRubricLoader {
    private final static Logger logger = LoggerFactory.getLogger(MachineRubricFileSystemLoader.class);

    private String basePath;

    @Autowired
    public MachineRubricFileSystemLoader(@Value("${machine.rubric.base.path}") String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String getContents(String fileName) {
        String content = null;
 
        if (!StringUtils.isBlank(fileName) && Files.exists(Paths.get(basePath, fileName))) {
            try {
                InputStream inputStream = Files.newInputStream(Paths.get(basePath, fileName));
                content = CharStreams.toString(new InputStreamReader(inputStream));
            } catch (IOException e) {
                logger.error("Could not get machine rubric content from file system", e);
            }
        }

        return content;
    }
}
