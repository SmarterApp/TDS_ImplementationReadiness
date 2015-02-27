package builders;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;

/**
 * Builder for StudentResponse objects
 */
public class StudentResponseBuilder {
    private StudentResponse studentResponse = new StudentResponse();

    public StudentResponseBuilder(long studentId) {
        studentResponse.setStudentID(studentId);
    }

    public StudentResponseBuilder bankKey(long bankKey) {
        studentResponse.setBankKey(Long.toString(bankKey));
        return this;
    }

    public StudentResponseBuilder id(long id) {
        studentResponse.setId(Long.toString(id));
        return this;
    }

    public StudentResponse toStudentResponse() {
        return studentResponse;
    }

}