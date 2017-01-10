package org.cresst.sb.irp.itemscoring.rubric;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;

/**
 * Loads the contents of machine rubrics from the file system
 */
@Service
public class MachineRubricFileSystemLoader implements MachineRubricLoader {
    private final static Logger logger = LoggerFactory.getLogger(MachineRubricFileSystemLoader.class);

    @Value("${irp.package.location}/IrpContentPackage/Items")
    private Resource irpContentPackagePath;

    private boolean IS_WINDOWS = System.getProperty( "os.name" ).contains( "indow" );
    
    @Override
    public String getContents(String fileName) {
        String content = null;

        try {
        	/*
        	 * using IS_WINDOWS to handle leading slash and backslash/forward slash inconsistent in path issue
        	 * java.nio.file.InvalidPathException: Illegal char <:> at index 2: 
        	 * /C:/Users/xx/IRP/irp-webapp/target/classes/irp-package/IrpContentPackage/Items\item-187-1975/Item_1975_v4.qrx
        	 */
			if (IS_WINDOWS) {
				if (!StringUtils.isBlank(fileName)
						&& Files.exists(Paths.get(irpContentPackagePath.getURI().getPath().replaceFirst("^/(.:/)", "$1"), fileName))) {
					InputStream inputStream =
							Files.newInputStream(Paths.get(irpContentPackagePath.getURI().getPath().replaceFirst("^/(.:/)", "$1"),
									fileName));
					content = CharStreams.toString(new InputStreamReader(inputStream));
					return content;
				}
			}
        	
            if (!StringUtils.isBlank(fileName) && Files.exists(Paths.get(irpContentPackagePath.getURI().getPath(), fileName))) {
                InputStream inputStream = Files.newInputStream(Paths.get(irpContentPackagePath.getURI().getPath(), fileName));
                content = CharStreams.toString(new InputStreamReader(inputStream));
            }
        } catch (IOException e) {
            logger.error("Could not get machine rubric content from file system", e);
        }

        return content;
    }
}
