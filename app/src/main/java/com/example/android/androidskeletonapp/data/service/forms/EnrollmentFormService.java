package com.example.android.androidskeletonapp.data.service.forms;

import com.example.android.androidskeletonapp.data.Sdk;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.common.Coordinates;
import org.hisp.dhis.android.core.enrollment.EnrollmentCreateProjection;
import org.hisp.dhis.android.core.enrollment.EnrollmentObjectRepository;
import org.hisp.dhis.android.core.maintenance.D2Error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

public class EnrollmentFormService {

    private D2 d2;
    private EnrollmentObjectRepository enrollmentRepository;
    private static EnrollmentFormService instance;
    private final Map<String, FormField> fieldMap;

    private EnrollmentFormService() {
        fieldMap = new HashMap<>();
    }

    public static EnrollmentFormService getInstance() {
        if (instance == null)
            instance = new EnrollmentFormService();

        return instance;
    }

    public boolean init(D2 d2, String teiUid, String programUid, String ouUid) throws D2Error {
        this.d2 = d2;
        // TODO Create enrollment

        String enrolmentUid = Sdk.d2().enrollmentModule().enrollments
                .add(EnrollmentCreateProjection
                        .builder()
                        .organisationUnit(ouUid)
                        .program(programUid)
                        .trackedEntityInstance(teiUid)
                        .build());

        EnrollmentObjectRepository repository = Sdk.d2().enrollmentModule().enrollments.uid(enrolmentUid);

        repository.setEnrollmentDate(new Date());

        repository.setIncidentDate(new Date());

        return false;
    }

    public Flowable<Map<String, FormField>> getEnrollmentFormFields() {
        return Flowable.empty();
    }

    public void saveCoordinates(double lat, double lon) {
        try {
            enrollmentRepository.setCoordinate(Coordinates.create(lat, lon));
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
    }

    public void saveEnrollmentDate(Date enrollmentDate) {
        try {
            enrollmentRepository.setEnrollmentDate(enrollmentDate);
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
    }

    public void saveEnrollmentIncidentDate(Date incidentDate) {
        try {
            enrollmentRepository.setIncidentDate(incidentDate);
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
    }

    public String getEnrollmentUid() {
        return enrollmentRepository.get().uid();
    }

    public void delete() {
        try {
            enrollmentRepository.delete();
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
    }

    public static void clear() {
        instance = null;
    }

}
