package com.example.android.androidskeletonapp.ui.d2_errors;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.Sdk;
import com.example.android.androidskeletonapp.ui.base.ListActivity;

import org.hisp.dhis.android.core.maintenance.D2Error;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class D2ErrorActivity extends ListActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp(R.layout.activity_d2_errors, R.id.d2ErrorsToolbar, R.id.d2ErrorsRecyclerView);
        // TODO List D2 errors
        observeErrors();
    }

    private void observeErrors() {
        D2ErrorAdapter d2ErrorAdapter = new D2ErrorAdapter();
        recyclerView.setAdapter(d2ErrorAdapter);

        LiveData<PagedList<D2Error>> pagedListLiveData = Sdk.d2().maintenanceModule().d2Errors.getPaged(12);
        pagedListLiveData.observe(this, pagedErrors -> {
            d2ErrorAdapter.submitList(pagedErrors);
            findViewById(R.id.d2ErrorsNotificator).setVisibility(pagedErrors.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }
}
