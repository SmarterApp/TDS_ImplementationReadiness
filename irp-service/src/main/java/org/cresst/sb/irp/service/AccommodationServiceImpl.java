package org.cresst.sb.irp.service;

import java.util.List;

import org.cresst.sb.irp.dao.AccommodationDao;
import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccommodationServiceImpl implements AccommodationService {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationServiceImpl.class);

	@Autowired
	private AccommodationDao accommodationDao;
	
	@Override
	public List<Accommodation> getAccommodations() {
		return accommodationDao.getAccommodations();
	}

	@Override
	public Accommodation getAccommodationByStudentIdentifier(long studentSSID) throws NotFoundException {
		return accommodationDao.getAccommodationByStudentIdentifier(studentSSID);
	}

}
