package org.cresst.sb.irp.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds page number to all the pages in the PDF download.
 */
public class PageStamper extends PdfPageEventHelper {
    private static final Logger log = LoggerFactory.getLogger(PageStamper.class);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        final int currentPageNumber = writer.getCurrentPageNumber();

        try {
            final Rectangle pageSize = document.getPageSize();
            final PdfContentByte directContent = writer.getDirectContent();

            Paragraph paraOne = new Paragraph(String.valueOf(currentPageNumber));

            Font fontFooter = new Font();
            fontFooter.setSize(6);
            fontFooter.setColor(BaseColor.BLACK);

            paraOne.setFont(fontFooter);

            Phrase footerPhraseOne = new Phrase(paraOne);
            ColumnText.showTextAligned(directContent, Element.ALIGN_CENTER, footerPhraseOne, pageSize.getWidth() / 2, 20, 0);

        } catch (Exception e) {
            log.error("PDF generation error", e);
        }
    }
}
