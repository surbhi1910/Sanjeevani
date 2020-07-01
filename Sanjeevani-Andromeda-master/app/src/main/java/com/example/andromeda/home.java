package com.example.andromeda;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment implements View.OnClickListener {

    private View view;
    private Button about_Covid, How_does_it_spread, Effects_of_temp, Symptoms, Prevention;

    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        about_Covid = view.findViewById(R.id.about_covid);
        How_does_it_spread = view.findViewById(R.id.how_does_it_spread);
        Effects_of_temp = view.findViewById(R.id.effects_of_temp);
        Symptoms = view.findViewById(R.id.symptoms);
        Prevention = view.findViewById(R.id.prevention);

        about_Covid.setOnClickListener(this);
        How_does_it_spread.setOnClickListener(this);
        Effects_of_temp.setOnClickListener(this);
        Symptoms.setOnClickListener(this);
        Prevention.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
       if(v == about_Covid){
            Intent i = new Intent(getActivity(),About_covid.class);
            startActivity(i);
        }
        else if(v == How_does_it_spread){
            Intent i = new Intent(getActivity(),How_does_it_Spread.class);
            startActivity(i);
        }
        else if(v == Effects_of_temp){
            Intent i = new Intent(getActivity(),Effects_of_Temp.class);
            startActivity(i);
        }
        else if(v == Symptoms){
            Intent i = new Intent(getActivity(),symptoms.class);
            startActivity(i);
        }
        else if(v == Prevention){
            Intent i = new Intent(getActivity(),prevention.class);
            startActivity(i);
        }
    }
}