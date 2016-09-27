package org.cresst.sb.irp.automation.service.engine.adapterclient;

import org.cresst.sb.irp.automation.adapter.domain.AdapterAutomationTicket;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public enum AdapterState implements AdapterStateAction, AdapterStateTrigger {
    DISCOVERY {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            adapterClient.get();
            return true;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            if (adapterClient.isNonDownloadMediaType() && adapterClient.hasRel(TDS_REPORTS_COLLECTION_REL)) {
                // Resource is API discovery with reference to a collection, go to collections state
                return AdapterState.TDS_REPORT_COLLECTION;

            } else if (adapterClient.isNonDownloadMediaType() && !adapterClient.hasCollection()) {
                // Resource is empty collection, go to create TDSReports state
                return AdapterState.TDS_REPORT_CREATION;

            } else if (adapterClient.isNonDownloadMediaType() && adapterClient.hasRel(TDS_REPORTS_STATUS_REL)) {
                // Resource is API discovery with reference to status, go to status state
                return AdapterState.TDS_REPORT_STATUS;

            } else if (adapterClient.isNonDownloadMediaType() && adapterClient.hasCollection()) {
                // Resource is collection of TDSReports, go to download TDSReport state
                return AdapterState.TDS_REPORT_DOWNLOAD;

            } else if (adapterClient.isDownloadMediaType()) {
                // Resource is a TDSReport or zip, go to download TDSReport state
                return AdapterState.TDS_REPORT_DOWNLOAD;
            }

            return AdapterState.ERROR;
        }
    },
    TDS_REPORT_COLLECTION {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            adapterClient.get();
            return true;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            if (adapterClient.hasCollection()) {
                return AdapterState.TDS_REPORT_DOWNLOAD;
            }

            return AdapterState.TDS_REPORT_CREATION;
        }
    },
    TDS_REPORT_CREATION {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            AdapterAutomationTicket ticket = new AdapterAutomationTicket();
            ticket.setIrpResources(new ArrayList<URI>());

            if (adapterClient.hasRel(TDS_REPORTS_CREATE_REL)) {
                adapterClient.rel(TDS_REPORTS_CREATE_REL).post(ticket);
            } else {
                adapterClient.rel(Link.REL_SELF).post(ticket);
            }

            return true;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            if (adapterClient.httpStatus() == HttpStatus.CREATED && !adapterClient.hasCollection()) {
                return AdapterState.ERROR;
            } else if (adapterClient.httpStatus() == HttpStatus.CREATED && adapterClient.hasCollection()) {
                return AdapterState.TDS_REPORT_COLLECTION;
            } else if (adapterClient.httpStatus() == HttpStatus.ACCEPTED && adapterClient.hasLocation()) {
                return AdapterState.TDS_REPORT_STATUS;
            }

            return AdapterState.ERROR;
        }
    },
    TDS_REPORT_STATUS {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            if (adapterClient.httpStatus() == HttpStatus.ACCEPTED && adapterClient.hasLocation()) {
                adapterClient.location().get();
            } else if (adapterClient.httpStatus() == HttpStatus.SEE_OTHER && adapterClient.hasLocation()) {
                adapterClient.location().get();
            } else {
                adapterClient.rel(Link.REL_SELF).get();
            }

            return true;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            if (adapterClient.httpStatus() == HttpStatus.SEE_OTHER && adapterClient.hasLocation()) {
                return AdapterState.TDS_REPORT_COLLECTION;
            } else if (adapterClient.httpStatus() == HttpStatus.OK && adapterClient.inProgress()) {
                return AdapterState.TDS_REPORT_STATUS;
            }

            return AdapterState.ERROR;
        }
    },
    TDS_REPORT_DOWNLOAD {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            if (adapterClient.isDownloadMediaType()) {
                // State landed on resource that is a TDSReport or ZIP
                Path file = adapterClient.file();
                storeFile(file, adapterClient.isZipFile());
            } else {
                // Transitioned from collections state
                List<Link> tdsReportLinks = adapterClient.getLinks();
                for (Link link : tdsReportLinks) {
                    if (adapterClient.target(link.getHref()).get().isDownloadMediaType()) {
                        Path file = adapterClient.file();
                        storeFile(file, adapterClient.isZipFile());
                    }
                }
            }

            return true;
        }

        private void storeFile(Path file, boolean isZip) {
            if (isZip) {
                for (Path unzippedFile : unzip(file)) {

                }
            } else {

            }
        }

        private Iterable<Path> unzip(Path zipFile) {
            return null;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            return AdapterState.END;
        }
    },
    END {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            return false;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            return AdapterState.END;
        }
    },
    ERROR {
        @Override
        public boolean action(final AdapterClient adapterClient) {
            return false;
        }

        @Override
        public AdapterState transition(final AdapterClientView adapterClient) {
            return AdapterState.ERROR;
        }
    };

    public static final String TDS_REPORTS_COLLECTION_REL = "collection";
    public static final String TDS_REPORTS_STATUS_REL = "monitor";
    public static final String TDS_REPORTS_CREATE_REL = "irp.create";
}