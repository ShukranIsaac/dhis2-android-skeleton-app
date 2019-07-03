package com.example.android.androidskeletonapp.ui.tracked_entity_instances.search;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.Sdk;
import com.example.android.androidskeletonapp.ui.base.ListActivity;
import com.example.android.androidskeletonapp.ui.tracked_entity_instances.TrackedEntityInstanceAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.hisp.dhis.android.core.arch.helpers.UidsHelper;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitMode;
import org.hisp.dhis.android.core.program.Program;
import org.hisp.dhis.android.core.program.ProgramType;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.trackedentity.search.QueryFilter;
import org.hisp.dhis.android.core.trackedentity.search.QueryItem;
import org.hisp.dhis.android.core.trackedentity.search.QueryOperator;
import org.hisp.dhis.android.core.trackedentity.search.TrackedEntityInstanceQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackedEntityInstanceSearchActivity extends ListActivity {

    private ProgressBar progressBar;
    private TextView downloadDataText;
    private TextView notificator;
    private TrackedEntityInstanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp(R.layout.activity_tracked_entity_instance_search, R.id.trackedEntityInstancesToolbar, R.id.trackedEntityInstanceRecyclerView);

        notificator = findViewById(R.id.dataNotificator);
        downloadDataText = findViewById(R.id.downloadDataText);
        progressBar = findViewById(R.id.trackedEntityInstanceProgressBar);
        FloatingActionButton downloadButton = findViewById(R.id.downloadDataButton);

        adapter = new TrackedEntityInstanceAdapter();

        downloadButton.setOnClickListener(view -> {
            view.setEnabled(Boolean.FALSE);
            view.setVisibility(View.GONE);
            downloadDataText.setVisibility(View.GONE);
            Snackbar.make(view, "Searching data...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            notificator.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            searchTrackedEntityInstances();
        });
    }

    private void searchTrackedEntityInstances() {
        recyclerView.setAdapter(adapter);

        // TODO Get list of SEARCH root organisation units
        List<OrganisationUnit> organisationUnits = Sdk.d2().organisationUnitModule().organisationUnits
                .byOrganisationUnitScope(OrganisationUnit.Scope.SCOPE_TEI_SEARCH)
                .byRootOrganisationUnit(true).get();
        // TODO Get first program with registration
        Program program = Sdk.d2().programModule().programs
                .byProgramType().eq(ProgramType.WITH_REGISTRATION)
                .one()
                .get();

        // TODO Get TrackedEntityAttribute with name equal to "Malaria patient id"
        TrackedEntityAttribute attribute = Sdk.d2().trackedEntityModule().trackedEntityAttributes
                .byName().eq("Malaria patient name")
                .one()
                .get();

        List<String> organisationUids = new ArrayList<>();
        if (!organisationUnits.isEmpty()) {
            organisationUids = UidsHelper.getUidsList(organisationUnits);
        }

        TrackedEntityInstanceQuery query = TrackedEntityInstanceQuery.builder()
                // TODO Filter by organisationUnits in DESCENDANT mode
                .orgUnits(organisationUids)
                .orgUnitMode(OrganisationUnitMode.DESCENDANTS)
                // TODO Filter by program
                .program(program.uid())
                // TODO Use "filter" property to filter the previous attribute by "like=a"
                .filter(Collections.singletonList(QueryItem.create(attribute.uid(), QueryFilter.builder().operator(QueryOperator.LIKE).filter(attribute.name()).build())))
                .pageSize(15)
                .paging(true)
                .page(1)
                .build();

        getTrackedEntityInstanceList(query).observe(this, trackedEntityInstancePagedList -> {
            adapter.submitList(trackedEntityInstancePagedList);
            downloadDataText.setVisibility(View.GONE);
            notificator.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.searchNotificator).setVisibility(
                    trackedEntityInstancePagedList.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private LiveData<PagedList<TrackedEntityInstance>> getTrackedEntityInstanceList(TrackedEntityInstanceQuery query) {
        // TODO Use trackedEntityInstanceQuery to return a pagedList with onlineFirst() strategy
        //  paged by 10

        return Sdk.d2().trackedEntityModule().trackedEntityInstanceQuery
                .query(query)
                .onlineFirst()
                .getPaged(20);
    }
}
