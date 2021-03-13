package com.example.killers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.killers.Crimi;
import com.example.killers.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CriminalFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.criminalfragment, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Killers");
        databaseReference.keepSynced(true);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Crimi, CrimiViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Crimi, CrimiViewHolder>
                (Crimi.class, R.layout.criminal_items, CrimiViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CrimiViewHolder crimiViewHolder, Crimi model, int i) {
                crimiViewHolder.setName(model.getName());
                crimiViewHolder.setImage(getContext(), model.getImage());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CrimiViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        public CrimiViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
        }
        public void setName(String name)
        {
            TextView nameCriminal = (TextView)view.findViewById(R.id.nameCriminal);
            nameCriminal.setText(name);
        }
        public void setImage(Context context, String image)
        {
            ImageView imageCriminal = (ImageView)view.findViewById(R.id.imageCriminal);
            Picasso.get().load(image).into(imageCriminal);
        }
    }
}
