package org.cresst.sb.irp.service;

import java.util.List;

import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.exceptions.NotFoundException;

public interface AccommodationService {
	
	List<Accommodation> getAccommodations();

	Accommodation getAccommodationByStudentIdentifier(String studentSSID) throws NotFoundException;
	
}
