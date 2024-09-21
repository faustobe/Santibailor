package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.faustobe.santibailor.R;

public class TempCategoryFragment extends BaseSettingsCategoryFragment {
    private static final String ARG_TITLE = "title";
    private String title;

    public static TempCategoryFragment newInstance(String title) {
        TempCategoryFragment fragment = new TempCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp_category, container, false);
    }

    @Override
    protected void setupUI() {
        TextView titleView = requireView().findViewById(R.id.category_title);
        titleView.setText(title);
    }

    @Override
    protected void observeViewModel() {
        // Non facciamo nulla qui per il fragment temporaneo
    }
}