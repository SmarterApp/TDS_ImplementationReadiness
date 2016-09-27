package org.cresst.sb.irp.automation.service.engine.adapterclient;

import org.cresst.sb.irp.automation.adapter.domain.AdapterAutomationTicket;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class SpringAdapterClient implements AdapterClient {

    private final static String[] NON_DOWNLOAD_MEDIA_TYPES = {
            "text/html",
            "application/json",
            "application/json+hal"
    };

    private final static String XML_MEDIA_TYPE = "text/xml";
    private final static String ZIP_MEDIA_TYPE = "application/zip";

    private final static String[] DOWNLOAD_MEDIA_TYPES = {
            XML_MEDIA_TYPE,
            ZIP_MEDIA_TYPE
    };

    private final URI adapterUri;
    private final RestOperations restOperations;

    private UriComponents currentTarget;
    private ResponseEntity<Resource<?>> currentResponseEntity;

    public SpringAdapterClient(URI adapterUri, RestOperations restOperations) {
        this.adapterUri = adapterUri;
        this.restOperations = restOperations;

        currentTarget = UriComponentsBuilder.fromUri(adapterUri).build();
    }

    @Override
    public AdapterClient get() {
        restOperations.execute(
                currentTarget.toUri(),
                HttpMethod.GET,
                
                );

        currentResponseEntity = restOperations.exchange(
                currentTarget.toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Resource<?>>() {});

        return this;
    }

    @Override
    public <T> AdapterClient post(T postObject) {

        HttpEntity<T> httpEntity = new HttpEntity<>(postObject, null);

        currentResponseEntity = restOperations.exchange(
                currentTarget.toUri(),
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<Resource<?>>() {});

        return this;
    }

    @Override
    public AdapterClient location() {
        currentTarget = UriComponentsBuilder.fromUri(adapterUri)
                .path(currentResponseEntity.getHeaders().getLocation().getPath())
                .build();

        return this;
    }

    @Override
    public AdapterClient rel(String rel) {
        currentTarget = UriComponentsBuilder.fromUri(adapterUri)
                .path(currentResponseEntity.getBody().getLink(rel).getHref())
                .build();

        return this;
    }

    @Override
    public AdapterClient target(String href){
        currentTarget = UriComponentsBuilder.fromUri(adapterUri)
                .path(href)
                .build();

        return this;
    }

    @Override
    public Path file() {
        return currentResponseEntity.getBody();
    }

    @Override
    public List<Link> getLinks() {
        return currentResponseEntity.getBody().getLinks();
    }

    @Override
    public HttpStatus httpStatus() {
        return currentResponseEntity.getStatusCode();
    }

    @Override
    public boolean hasLocation() {
        return currentResponseEntity.getHeaders().getLocation() != null;
    }

    @Override
    public boolean hasCollection() {
        List<Link> links = getLinks();
        return links != null && links.size() > 0;
    }

    @Override
    public boolean isNonDownloadMediaType() {
        return isMediaType(NON_DOWNLOAD_MEDIA_TYPES);
    }

    @Override
    public boolean isDownloadMediaType() {
        return isMediaType(DOWNLOAD_MEDIA_TYPES);
    }

    @Override
    public boolean isZipFile() {
        return ZIP_MEDIA_TYPE.equals(mediaType());
    }

    private boolean isMediaType(final String[] mediaTypes) {
        List<String> contentTypes = currentResponseEntity.getHeaders().get("Content-Type");
        for (String mediaType : mediaTypes) {
            if (contentTypes.contains(mediaType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String mediaType() {
        List<String> contentTypes = currentResponseEntity.getHeaders().get("Content-Type");
        return contentTypes != null && contentTypes.size() > 0 ? contentTypes.get(0) : null;
    }

    @Override
    public boolean hasRel(String rel) {
        return currentResponseEntity.getBody() != null && currentResponseEntity.getBody().hasLink(rel);
    }

    @Override
    public Link getLink(String rel) {
        return currentResponseEntity.getBody().getLink(rel);
    }

    @Override
    public String getHref(String rel) {
        return currentResponseEntity.getBody().getLink(rel).getHref();
    }

    @Override
    public boolean inProgress() {
        AdapterAutomationTicket ticket = (AdapterAutomationTicket)currentResponseEntity.getBody().getContent();
        return !ticket.getAdapterAutomationStatusReport().isAutomationComplete();
    }
}
