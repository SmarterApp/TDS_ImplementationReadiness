package org.cresst.sb.irp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to generate PDF")
public class PDFGenerationException extends RuntimeException {

}
