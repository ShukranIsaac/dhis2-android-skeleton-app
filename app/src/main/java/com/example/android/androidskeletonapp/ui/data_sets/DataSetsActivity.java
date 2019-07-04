package com.example.android.androidskeletonapp.ui.data_sets;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.Sdk;
import com.example.android.androidskeletonapp.ui.base.ListActivity;
import org.hisp.dhis.android.core.dataset.DataSet;

import java.util.List;

public class DataSetsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp(R.layout.activity_data_sets, R.id.dataSetsToolbar, R.id.dataSetsRecyclerView);
        // TODO List DataSets
        downloadDataSets();
    }

    private void downloadDataSets() {
        DataSetsAdapter dataSetsAdapter = new DataSetsAdapter();
        recyclerView.setAdapter(dataSetsAdapter);
        LiveData<PagedList<DataSet>> listLiveDataSets = Sdk.d2().dataSetModule().dataSets.withStyle().getPaged(30);
        listLiveDataSets.observe(this, dataSetPagedList  -> {
            dataSetsAdapter.submitList(dataSetPagedList );
            findViewById(R.id.dataSetsNotificator).setVisibility(dataSetPagedList.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }
}
