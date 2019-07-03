package com.example.android.androidskeletonapp.data.service.forms;

import com.example.android.androidskeletonapp.data.Sdk;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.common.Coordinates;
import org.hisp.dhis.android.core.enrollment.EnrollmentCreateProjection;
import org.hisp.dhis.android.core.enrollment.EnrollmentObjectRepository;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueObjectRepository;

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
        if (instance == null) instance = new EnrollmentFormService();
        return instance;
    }

    public boolean init(D2 d2, String teiUid, String programUid, String ouUid) {
        this.d2 = d2;
        try {
            String enrollmentUid = d2.enrollmentModule().enrollments.add(
                    EnrollmentCreateProjection.builder()
                            .organisationUnit(ouUid)
                            .program(programUid)
                            .trackedEntityInstance(teiUid)
                            .build()
            );
            enrollmentRepository = d2.enrollmentModule().enrollments.uid(enrollmentUid);
            enrollmentRepository.setEnrollmentDate(new Date());
            enrollmentRepository.setIncidentDate(new Date());
            return true;
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
            return false;
        }
    }


    public Flowable<Map<String, FormField>> getEnrollmentFormFields() {

        return Flowable.fromCallable(() -> {
                    return Sdk.d2().programModule().programs
                            .uid(enrollmentRepository.get().program())
                            .withAllChildren()
                            .get()
                            .programTrackedEntityAttributes(); //TODO: replace with program attributes
                }
        ).map(programAttributeList -> {

            //TODO for each programAttribute create and store a FormField Object into the fieldMap object
            for (ProgramTrackedEntityAttribute attribute: programAttributeList) {
                TrackedEntityAttribute trackedEntityAttribute = attribute.trackedEntityAttribute();
                TrackedEntityDataValueObjectRepository dataValueObjectRepository = Sdk.d2().trackedEntityModule()
                        .trackedEntityDataValues
                        .value(attribute.uid(), enrollmentRepository.get().trackedEntityInstance());
                String value = null;

                if (dataValueObjectRepository.exists()) value = dataValueObjectRepository.get().value();

                if ((trackedEntityAttribute != null ? trackedEntityAttribute.generated() : null) && value == null) value = d2.trackedEntityModule()
                        .reservedValueManager.getValue(trackedEntityAttribute.uid(), null);

                FormField field = new FormField(
                        trackedEntityAttribute.uid(),
                        trackedEntityAttribute.optionSet() != null ? trackedEntityAttribute.optionSet().uid() : null,
                        trackedEntityAttribute.valueType(),
                        trackedEntityAttribute.formName(),
                        value,
                        null,
                        !trackedEntityAttribute.generated(),
                        null
                );
                fieldMap.put(attribute.uid(), field);
            }
            return fieldMap;
        });
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
