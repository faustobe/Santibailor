package it.faustobe.santibailor.presentation.features.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.faustobe.santibailor.R;

public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recupera l'argomento usando Safe Args
        int testArg = TestFragmentArgs.fromBundle(getArguments()).getTestArg();

        // Visualizza l'argomento in un TextView
        TextView textView = view.findViewById(R.id.testTextView);
        textView.setText(String.valueOf(testArg));
    }
}