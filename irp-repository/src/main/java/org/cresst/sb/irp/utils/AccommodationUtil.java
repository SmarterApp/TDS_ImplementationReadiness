package org.cresst.sb.irp.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utilities for the processing of Accommodations from excel files.
 */
@Service
public class AccommodationUtil {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationUtil.class);

    /**
     * 
     * @param list
     *            results from processing excel sheet
     * @param headerMap
     *            map that contains column index keys to header name values
     * @param sheet
     *            spreadsheet to process
     * @param excelUtil
     *            excel utility service
     * @param startIndex
     *            index to start processing from. Iterates until last row.
     */
	public void processSheet(List<Accommodation> list,
			Map<Integer, String> headerMap, XSSFSheet sheet,
			ExcelUtil excelUtil,
			int startIndex) {
		try {
			for (int rowCount = startIndex; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !excelUtil.isEmptyRow(detailRow)) {
					Accommodation accommodation = createObject(detailRow, headerMap);
					if (accommodation != null)
						list.add(accommodation);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}
	}
	
    /**
     * 
     * @param row
     *            excel row to create accommodation from
     * @param headerMap
     *            <column index, column name> map to process row
     * @return
     */
	private Accommodation createObject(Row row, Map<Integer, String> headerMap) {
		Accommodation accommodation = null;
		try {
			accommodation = new Accommodation();
			int headerSize = headerMap.size();
			for (int cellCount = 0; cellCount < headerSize; cellCount++) {
				Cell cell = row.getCell(cellCount);
				String columnName = headerMap.get(cellCount);
				if (cell != null) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					}
					setFieldData(accommodation, columnName, cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			logger.error("createObject(); exception: ", e);
		}

		return accommodation;
	}
	
    /**
     * 
     * @param accommodation
     *            existing Accommodation that will have a new property set
     *            according to `columnName`, with the value of `cellStringValue`
     * @param columnName
     *            selects the method of setting the value in `cellStringValue`.
     *            The string will make `cellStringValue` to the corresponding
     *            property in `accommodation`.
     * @param cellStringValue
     *            value to set in `accommodation`
     */
	private void setFieldData(Accommodation accommodation, String columnName, String cellStringValue) {

	    columnName = StringUtils.deleteWhitespace(columnName);
        switch (columnName.toLowerCase()) {
            case "studentidentifier":
                accommodation.setStudentIdentifier(cellStringValue);
                break;
            case "stateabbreviation":
                accommodation.setStateAbbreviation(cellStringValue);
                break;
            case "subject":
                accommodation.setSubject(cellStringValue);
                break;
            case "accommodationcodes":
                // Set some defaults
                accommodation.setLanguage("ENU");
                accommodation.setPrintSize("TDS_PS_L0");

                String[] codes = StringUtils.split(cellStringValue, '|');
                for (int i = 0; i < codes.length; i++) {
                    switch (codes[i]) {
                        case "TDS_ASL0":
                        case "TDS_ASL1":
                            accommodation.setAmericanSignLanguage(codes[i]);
                            break;
                        case "TDS_CC0":
                        case "TDS_CCInvert":
                        case "TDS_CCMagenta":
                        case "TDS_CCMedGrayLtGray":
                        case "TDS_CCYellowB":
                            accommodation.setColorContrast(codes[i]);
                            break;
                        case "TDS_ClosedCap0":
                        case "TDS_ClosedCap1":
                            accommodation.setClosedCaptioning(codes[i]);
                            break;
                        case "ENU":
                        case "ENU-Braille":
                        case "ESN":
                            accommodation.setLanguage(codes[i]);
                            break;
                        case "TDS_Masking0":
                        case "TDS_Masking1":
                            accommodation.setMasking(codes[i]);
                            break;
                        case "TDS_PM0":
                        case "TDS_PM1":
                            accommodation.setPermissiveMode(codes[i]);
                            break;
                        case "TDS_PoD0":
                        case "TDS_PoD_Stim":
                            accommodation.setPrintOnDemand(codes[i]);
                            break;
                        case "TDS_PS_L0":
                        case "TDS_PS_L1":
                        case "TDS_PS_L2":
                        case "TDS_PS_L3":
                        case "TDS_PS_L4":
                            accommodation.setPrintSize(codes[i]);
                            break;
                        case "TDS_TS_Modern":
                        case "TDS_TS_Accessibility":
                            accommodation.setStreamlinedInterface(codes[i]);
                            break;
                        case "TDS_TTS0":
                        case "TDS_TTS_Item":
                        case "TDS_TTS_Stim":
                        case "TDS_TTS_Stim&TDS_TTS_Item":
                            accommodation.setTexttoSpeech(codes[i]);
                            break;
                        case "TDS_WL_Glossary":
                        case "TDS_WL_ArabicGloss":
                        case "TDS_WL_CantoneseGloss":
                        case "TDS_WL_ESNGlossary":
                        case "TDS_WL_KoreanGloss":
                        case "TDS_WL_MandarinGloss":
                        case "TDS_WL_PunjabiGloss":
                        case "TDS_WL_RussianGloss":
                        case "TDS_WL_TagalGloss":
                        case "TDS_WL_UkrainianGloss":
                        case "TDS_WL_VietnameseGloss":
                        case "TDS_WL_Glossary&TDS_WL_ArabicGloss":
                        case "TDS_WL_Glossary&TDS_WL_CantoneseGloss":
                        case "TDS_WL_Glossary&TDS_WL_ESNGlossary":
                        case "TDS_WL_Glossary&TDS_WL_KoreanGloss":
                        case "TDS_WL_Glossary&TDS_WL_MandarinGloss":
                        case "TDS_WL_Glossary&TDS_WL_PunjabiGloss":
                        case "TDS_WL_Glossary&TDS_WL_RussianGloss":
                        case "TDS_WL_Glossary&TDS_WL_TagalGloss":
                        case "TDS_WL_Glossary&TDS_WL_UkrainianGloss":
                        case "TDS_WL_Glossary&TDS_WL_VietnameseGloss":
                        case "TDS_WL0":
                            accommodation.setTranslation(codes[i]);
                            break;
                        case "NEDS0":
                        case "NEDS_BD":
                        case "NEDS_CC":
                        case "NEDS_CO":
                        case "NEDS_Mag":
                        case "NEDS_RA_Items":
                        case "NEDS_SC_Items":
                        case "NEDS_SS":
                        case "NEDS_TArabic":
                        case "NEDS_TCantonese":
                        case "NEDS_TFilipino":
                        case "NEDS_TKorean":
                        case "NEDS_TMandarin":
                        case "NEDS_TPunjabi":
                        case "NEDS_TRussian":
                        case "NEDS_TSpanish":
                        case "NEDS_TUkrainian":
                        case "NEDS_TVietnamese":
                        case "NEDS_TransDirs":
                            accommodation.setNonEmbeddedDesignatedSupports(codes[i]);
                            break;
                        case "NEA0":
                        case "NEA_AR":
                        case "NEA_RA_Stimuli":
                        case "NEA_SC_WritItems":
                        case "NEA_STT":
                        case "NEA_Abacus":
                        case "NEA_Calc":
                        case "NEA_MT":
                        case "NEA_NoiseBuf":
                            accommodation.setNonEmbeddedAccommodations(codes[i]);
                            break;
                        default:
                            accommodation.setOther(codes[i]);
                            break;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid column name: " + columnName);
        }
	}
	
}
