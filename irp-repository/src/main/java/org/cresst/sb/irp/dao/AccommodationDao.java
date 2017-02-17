package org.cresst.sb.irp.dao;

import java.util.List;

import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.exceptions.NotFoundException;

/**
 * Accommodation Data Repository which loads accommodation data from irp-package
 * excel spreadsheet.
 */
public interface AccommodationDao {

	List<Accommodation> getAccommodations();
	
	Accommodation getAccommodationByStudentIdentifier(String studentSSID) throws NotFoundException;
	
	Accommodation getAccommodationByStudentIdentifier(List<Accommodation> accommodations, String studentSSID);
	
}
