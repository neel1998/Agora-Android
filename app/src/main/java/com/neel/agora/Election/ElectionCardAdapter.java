package com.neel.agora.Election;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neel.agora.R;

import org.w3c.dom.Text;

import java.util.List;

public class ElectionCardAdapter extends ArrayAdapter<ElectionData> {
    public ElectionCardAdapter(Context context, List<ElectionData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View electionView = convertView;
        if (electionView == null){
            electionView = LayoutInflater.from(getContext()).inflate(R.layout.election_card_layout, parent, false);
        }
        ElectionData data = getItem(position);
        TextView titleTextview = electionView.findViewById(R.id.election_card_title);
        TextView descriptionTextview = electionView.findViewById(R.id.election_card_description_text);
        TextView startTextview = electionView.findViewById(R.id.election_card_start_value);
        TextView endTextview = electionView.findViewById(R.id.election_card_end_value);
        TextView statusTextview = electionView.findViewById(R.id.election_card_status_value);
        TextView candidateTextView = electionView.findViewById(R.id.election_card_candidates);

        titleTextview.setText("Election: " + data.getName());
        descriptionTextview.setText(data.getDescription());
        startTextview.setText(data.getStart());
        endTextview.setText(data.getEnd());
        statusTextview.setText(data.getStatus());
        return electionView;
    }
}
