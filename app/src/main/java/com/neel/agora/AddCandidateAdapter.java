package com.neel.agora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AddCandidateAdapter extends ArrayAdapter<String> {
    public AddCandidateAdapter(Context context, List<String> candidates) {
        super(context, 0, candidates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.add_candidate_list_layout, parent, false);
        }
        String candidateName = getItem(position);
        TextView candidateNameTextView = listView.findViewById(R.id.candidates_name);
        candidateNameTextView.setText(candidateName);
        return listView;
    }
}
