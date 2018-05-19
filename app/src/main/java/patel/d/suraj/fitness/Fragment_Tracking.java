package patel.d.suraj.fitness;

/**
 * Created by suraj.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Tracking extends Fragment {

    Button button;
    ListView listView;
    ArrayList<Session> sessionArrayList;
    MyAdapter adapter;

    public Fragment_Tracking() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        listView = view.findViewById(R.id.lv2);
        button = (Button) view.findViewById(R.id.bt1as);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        sessionArrayList = new ArrayList<>();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) ->
                startActivity(new Intent(getActivity(), HistoryActivity.class)));

        button.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TrackActivity.class));
        });

        final SharedPreferences mypref = getActivity().getSharedPreferences("data.txt", MODE_PRIVATE);
        final String n = mypref.getString("username", null);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mainref = firebaseDatabase.getReference("Session");

        Query childref = mainref.orderByChild("username").equalTo(n).limitToFirst(2);
        childref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot maindataSnapshot) {
                sessionArrayList.clear();

                for (DataSnapshot singlerow : maindataSnapshot.getChildren()) {
                    Session stud = singlerow.getValue(Session.class);
                    Log.d("MYMSG", stud.getSessionname());
                    sessionArrayList.add(stud);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return sessionArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return sessionArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.single_row, viewGroup, false);

            Session stud = sessionArrayList.get(i);
            String sessionName="Session Name: "+stud.getSessionname();

            SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("USERNAME",MODE_PRIVATE);
            String username=sharedPreferences.getString("userz",null);

            final TextView tv1, tv2, tv3;
            tv1 = (TextView) (view.findViewById(R.id.tv1));
            tv2 = (TextView) (view.findViewById(R.id.tv2));
            tv3 = (TextView) (view.findViewById(R.id.tv3));
            tv1.setText(sessionName);
           /* tv2.setText(stud.getUsername());*/
             tv2.setText(username);
            tv3.setText(stud.getDate());

            return view;
        }
    }
}