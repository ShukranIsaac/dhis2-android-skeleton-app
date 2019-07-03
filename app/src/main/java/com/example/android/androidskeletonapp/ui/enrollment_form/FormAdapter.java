package com.example.android.androidskeletonapp.ui.enrollment_form;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.service.forms.FormField;

import org.hisp.dhis.android.core.common.ValueType;

import java.util.ArrayList;
import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FieldHolder> {

    private List<FormField> fields;
    private OnValueSaved valueSaved;

    public FormAdapter(OnValueSaved valueSavedListener) {
        this.fields = new ArrayList<>();
        this.valueSaved = valueSavedListener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return fields.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public void updateData(List<FormField> updates) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return fields.size();
            }

            @Override
            public int getNewListSize() {
                return updates.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return fields.get(oldItemPosition).getUid().equals(updates.get(newItemPosition).getUid());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return fields.get(oldItemPosition) == updates.get(newItemPosition);
            }
        });

        fields.clear();
        fields.addAll(updates);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public FieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO Create view holder depending on the field value type
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (ValueType.values()[viewType]) {
            case DATE:
                return new DateFieldHolder(inflater.inflate(R.layout.item_date_field, parent), valueSaved);
            case LONG_TEXT:
                return new TextFieldHolder(inflater.inflate(R.layout.item_field, parent), valueSaved);
            default:
                return new TextFieldHolder(inflater.inflate(R.layout.item_field, parent), valueSaved);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FieldHolder holder, int position) {
        holder.bind(fields.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        //TODO: Return the valueType ordinal
        return fields.get(position).getValueType().ordinal();
    }

    public interface OnValueSaved {
        void onValueSaved(String fieldUid, String value);
    }
}
