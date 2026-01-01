package it.faustobe.santibailor.presentation.features.organizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Priorita;
import it.faustobe.santibailor.presentation.features.impegni.ImpegniViewModel;

@AndroidEntryPoint
public class OrganizzaGiornataFragment extends Fragment {
    private ImpegniViewModel impegniViewModel;

    private EditText etImpegno1, etImpegno2, etImpegno3, etImpegno4, etImpegno5;
    private EditText etOra1, etOra2, etOra3, etOra4, etOra5;
    private MaterialButton btnSalva;
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizza_giornata, container, false);

        etImpegno1 = view.findViewById(R.id.et_impegno_1);
        etImpegno2 = view.findViewById(R.id.et_impegno_2);
        etImpegno3 = view.findViewById(R.id.et_impegno_3);
        etImpegno4 = view.findViewById(R.id.et_impegno_4);
        etImpegno5 = view.findViewById(R.id.et_impegno_5);

        etOra1 = view.findViewById(R.id.et_ora_1);
        etOra2 = view.findViewById(R.id.et_ora_2);
        etOra3 = view.findViewById(R.id.et_ora_3);
        etOra4 = view.findViewById(R.id.et_ora_4);
        etOra5 = view.findViewById(R.id.et_ora_5);

        btnSalva = view.findViewById(R.id.btn_salva_impegni);
        btnBack = view.findViewById(R.id.btn_back);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        impegniViewModel = new ViewModelProvider(this).get(ImpegniViewModel.class);

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        btnSalva.setOnClickListener(v -> salvaImpegni());
    }

    private void salvaImpegni() {
        int count = 0;

        // Impegno 1
        if (!etImpegno1.getText().toString().trim().isEmpty()) {
            salvaImpegno(etImpegno1.getText().toString(), etOra1.getText().toString());
            count++;
        }

        // Impegno 2
        if (!etImpegno2.getText().toString().trim().isEmpty()) {
            salvaImpegno(etImpegno2.getText().toString(), etOra2.getText().toString());
            count++;
        }

        // Impegno 3
        if (!etImpegno3.getText().toString().trim().isEmpty()) {
            salvaImpegno(etImpegno3.getText().toString(), etOra3.getText().toString());
            count++;
        }

        // Impegno 4
        if (!etImpegno4.getText().toString().trim().isEmpty()) {
            salvaImpegno(etImpegno4.getText().toString(), etOra4.getText().toString());
            count++;
        }

        // Impegno 5
        if (!etImpegno5.getText().toString().trim().isEmpty()) {
            salvaImpegno(etImpegno5.getText().toString(), etOra5.getText().toString());
            count++;
        }

        if (count > 0) {
            Toast.makeText(getContext(), count + " impegni salvati", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigateUp();
        } else {
            Toast.makeText(getContext(), "Inserisci almeno un impegno", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvaImpegno(String titolo, String oraStr) {
        Calendar dataOra = Calendar.getInstance();

        // Parse ora se fornita
        if (!oraStr.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
            try {
                Date time = sdf.parse(oraStr);
                if (time != null) {
                    Calendar timeCal = Calendar.getInstance();
                    timeCal.setTime(time);
                    dataOra.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                    dataOra.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                }
            } catch (ParseException e) {
                // Ignora errori di parsing, usa l'ora corrente
            }
        }

        long now = System.currentTimeMillis();
        Impegno impegno = new Impegno(
            0,  // id (sarà assegnato dal database)
            titolo,
            "",  // descrizione
            dataOra.getTimeInMillis(),  // dataOra come long
            null,  // categoria
            false,  // reminderEnabled
            0,  // reminderMinutesBefore
            false,  // completato
            null,  // note
            now,  // createdAt
            now,  // updatedAt
            Priorita.MEDIA.toString(),  // priorita come String
            null  // imageUrl
        );

        impegniViewModel.insertImpegno(impegno);
    }
}
