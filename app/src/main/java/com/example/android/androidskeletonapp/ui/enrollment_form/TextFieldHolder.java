package com.example.android.androidskeletonapp.ui.enrollment_form;

import android.text.InputFilter;
import android.text.InputType;
import android.view.View;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.service.forms.FormField;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;

import static org.apache.commons.lang3.StringUtils.isEmpty;

class TextFieldHolder extends FieldHolder {

    private final TextInputEditText editText;

    TextFieldHolder(@NonNull View itemView, FormAdapter.OnValueSaved valueSavedListener) {
        super(itemView, valueSavedListener);
        this.editText = itemView.findViewById(R.id.inputEditText);
    }

    void bind(FormField fieldItem) {
        super.bind(fieldItem);

        editText.setFilters(new InputFilter[]{});
        editText.setMaxLines(1);

        switch (fieldItem.getValueType()) {
            case NUMBER:
            case DATE:
            case INTEGER:
            case INTEGER_NEGATIVE:
            case INTEGER_ZERO_OR_POSITIVE:
            case INTEGER_POSITIVE:
            case PERCENTAGE:
            case UNIT_INTERVAL:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL |
                        InputType.TYPE_NUMBER_FLAG_SIGNED);
                break;
            case PHONE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
                default:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
        }

        // TODO set initial value, enable if editable and add value listener for text changes
        if (!isEmpty(fieldItem.getValue())) {
            editText.setText(fieldItem.getValue());
        } else {
            editText.setText(null);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String value = editText.getText() != null ? editText.getText().toString() : null;
                    valueSavedListener.onValueSaved(fieldItem.getUid(), value);
                }
            }
        });
    }
}
