package org.cresst.sb.irp.dao;

import java.util.List;

import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.exceptions.NotFoundException;

public interface AccommodationDao {

	List<Accommodation> getAccommodations();
	
	Accommodation getAccommodationByStudentIdentifier(long studentSSID) throws NotFoundException;
	
	Accommodation getAccommodationByStudentIdentifier(List<Accommodation> accommodations, long studentSSID);
	
}
