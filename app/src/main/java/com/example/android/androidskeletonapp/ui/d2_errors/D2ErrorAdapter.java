package com.example.android.androidskeletonapp.ui.d2_errors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.ui.base.DiffByIdItemCallback;
import com.example.android.androidskeletonapp.ui.base.ListItemHolder;

import org.hisp.dhis.android.core.maintenance.D2Error;

import java.util.ArrayList;
import java.util.List;

public class D2ErrorAdapter extends PagedListAdapter<D2Error, ListItemHolder> {

    private List<D2Error> d2ErrorList = new ArrayList<>();


    protected D2ErrorAdapter() {
        super(new DiffByIdItemCallback<>());
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        D2Error d2Error = d2ErrorList.get(position);
        holder.title.setText(d2Error.httpErrorCode());
        holder.subtitle1.setText(d2Error.errorDescription());
        holder.subtitle2.setText(d2Error.errorComponent().toString());
    }

    @Override
    public int getItemCount() {
        return d2ErrorList.size();
    }
}
