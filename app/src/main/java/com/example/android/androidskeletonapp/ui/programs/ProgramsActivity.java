package com.example.android.androidskeletonapp.ui.programs;

import android.os.Bundle;
import android.view.View;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.Sdk;
import com.example.android.androidskeletonapp.ui.base.ListActivity;

import org.hisp.dhis.android.core.arch.helpers.UidsHelper;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.program.Program;
import org.hisp.dhis.android.core.program.ProgramType;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import java.util.List;
import java.util.Set;

public class ProgramsActivity extends ListActivity implements OnProgramSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp(R.layout.activity_programs, R.id.programsToolbar, R.id.programsRecyclerView);
        observePrograms();
    }

    private void observePrograms() {
        ProgramsAdapter adapter = new ProgramsAdapter(this);
        recyclerView.setAdapter(adapter);

        // TODO Filter and sort Programs by orgUnit and displayName
        List<OrganisationUnit> orgUnits = Sdk.d2().organisationUnitModule().organisationUnits
                .byOrganisationUnitScope(OrganisationUnit.Scope.SCOPE_DATA_CAPTURE)
                .get();

        LiveData<PagedList<Program>> programs = Sdk.d2().programModule().programs
                .byOrganisationUnitList(UidsHelper.getUidsList(orgUnits))
                .withStyle()
                .getPaged(20);

        programs.observe(this, programPagedList -> {
            adapter.submitList(programPagedList);
            findViewById(R.id.programsNotificator).setVisibility(
                    programPagedList.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onProgramSelected(String programUid, ProgramType programType) {

    }
}
