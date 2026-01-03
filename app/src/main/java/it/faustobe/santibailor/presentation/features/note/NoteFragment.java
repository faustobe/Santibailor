package it.faustobe.santibailor.presentation.features.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.faustobe.santibailor.R;

public class NoteFragment extends Fragment {
    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_TEXT = "note_text";
    private static final String KEY_LAST_SAVED = "last_saved";

    private EditText etNote;
    private TextView tvLastSaved;
    private ImageButton btnBack;
    private ImageButton btnSave;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        etNote = view.findViewById(R.id.et_note);
        tvLastSaved = view.findViewById(R.id.tv_last_saved);
        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);

        prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Carica note salvate
        loadNote();

        // Pulsante indietro
        btnBack.setOnClickListener(v -> {
            saveNote();
            Navigation.findNavController(v).navigateUp();
        });

        // Pulsante salva
        btnSave.setOnClickListener(v -> {
            saveNote();
            Toast.makeText(getContext(), getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        });

        // Auto-save mentre scrivi (con delay)
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Auto-save ogni 2 secondi
                etNote.removeCallbacks(autoSaveRunnable);
                etNote.postDelayed(autoSaveRunnable, 2000);
            }
        });
    }

    private final Runnable autoSaveRunnable = new Runnable() {
        @Override
        public void run() {
            saveNote();
            updateLastSavedText();
        }
    };

    private void loadNote() {
        String savedText = prefs.getString(KEY_NOTE_TEXT, "");
        etNote.setText(savedText);

        long lastSaved = prefs.getLong(KEY_LAST_SAVED, 0);
        if (lastSaved > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
            tvLastSaved.setText(getString(R.string.last_saved, sdf.format(new Date(lastSaved))));
        }
    }

    private void saveNote() {
        String text = etNote.getText().toString();
        prefs.edit()
            .putString(KEY_NOTE_TEXT, text)
            .putLong(KEY_LAST_SAVED, System.currentTimeMillis())
            .apply();
    }

    private void updateLastSavedText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
        tvLastSaved.setText(getString(R.string.last_saved, sdf.format(new Date())));
    }

    @Override
    public void onPause() {
        super.onPause();
        saveNote();
    }
}
