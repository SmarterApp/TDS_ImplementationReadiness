package org.cresst.sb.irp.automation.service.engine.adapterclient;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;
import java.util.List;

public interface AdapterClientView {
    Path file();
    List<Link> getLinks();
    HttpStatus httpStatus();
    boolean hasLocation();
    boolean hasCollection();
    boolean isNonDownloadMediaType();
    boolean isDownloadMediaType();
    boolean isZipFile();
    String mediaType();
    boolean hasRel(String rel);
    Link getLink(String rel);
    String getHref(String rel);
    boolean inProgress();
}
