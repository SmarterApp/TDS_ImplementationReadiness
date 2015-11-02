package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.AccommodationUtil;
import org.cresst.sb.irp.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class AccommodationDaoImpl implements AccommodationDao {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationDaoImpl.class);
	
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private List<Accommodation> accommodations = new ArrayList<Accommodation>();

	@Value("classpath:irp-package/IRPStudentsDesignatedSupportsAndAccommodations.xlsx")
	private Resource accommodationResource;
	
	@Autowired
	private ExcelUtil excelUtil;
	
	@Autowired
	private AccommodationUtil accommodationUtil;
	
	public AccommodationDaoImpl() {}
	
	@PostConstruct
	public void loadData() throws Exception {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(accommodationResource.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			excelUtil.getHeaderColumn(headerMap, sheet, 0);
			accommodationUtil.processSheet(accommodations, headerMap, sheet, excelUtil, 1);
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}
	
	@Override
	public List<Accommodation> getAccommodations() {
		return accommodations;
	}

	@Override
	public Accommodation getAccommodationByStudentIdentifier(String studentSSID) throws NotFoundException {
		Accommodation accommodation = getAccommodationByStudentIdentifier(accommodations, studentSSID);
		if (accommodation == null){
			throw new NotFoundException("Could not find accommodation " + studentSSID);
		}
		return accommodation;
	}

	@Override
	public Accommodation getAccommodationByStudentIdentifier(List<Accommodation> accommodations, String studentSSID) {
		for (Accommodation accommodation : accommodations){
			if(studentSSID != null && studentSSID.equalsIgnoreCase(accommodation.getStudentIdentifier())){
				return accommodation;
			}
		}

		return null;
	}

}
