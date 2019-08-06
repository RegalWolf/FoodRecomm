package com.iqbalhasan.foodrecomm.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.iqbalhasan.foodrecomm.R;

public class BiodataFragment extends Fragment {

    private Spinner spinnerBiodata;
    private ArrayAdapter<CharSequence> adapterBiodata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biodata, container, false);

        spinnerBiodata = view.findViewById(R.id.spinner_biodata);

        adapterBiodata = ArrayAdapter
                .createFromResource(getContext(), R.array.biodata, android.R.layout.simple_spinner_item);
        adapterBiodata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBiodata.setAdapter(adapterBiodata);
        spinnerBiodata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Fragment selectedFragment = null;

                if (text.equals("Profile")) {
                    selectedFragment = new ProfileFragment();
                } else if (text.equals("Makanan Disukai")) {
                    selectedFragment = new MakananDisukaiFragment();
                } else if (text.equals("Makanan Tidak Disukai")) {
                    selectedFragment = new MakananTidakDisukaiFragment();
                }

                getChildFragmentManager().beginTransaction().replace(R.id.fragment_biodata_container,
                        selectedFragment).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

}
