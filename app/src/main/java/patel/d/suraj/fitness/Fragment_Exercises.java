package patel.d.suraj.fitness;

/**
 * Created by suraj.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class Fragment_Exercises extends Fragment {

    CarouselView carouselView;
    ListView lv;
    int[] sampleImages = {R.drawable.s, R.drawable.p, R.drawable.q,R.drawable.r};
    MyAdapter adapter;

    String items[]={"Push Up","Sit Up","Chin Up"};

    public Fragment_Exercises() {    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercises,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();

        lv=(ListView) (getActivity().findViewById(R.id.list));
        carouselView = (CarouselView)getActivity(). findViewById(R.id.cv);

        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(sampleImages.length);

        adapter=new MyAdapter();
        lv.setAdapter(adapter);






        lv.setOnItemClickListener((parent, view, position, id) -> {

            if(items[position].equals("Push Up")) {
                startActivity(new Intent(getActivity(),PushUps.class));
            }
            else if(items[position].equals("Sit Up")) {
                Intent intent = new Intent(getActivity(), WorkOutActivity.class);
                intent.putExtra("option12", items[position]);
                startActivity(intent);
            }
            else if(items[position].equals("Chin Up")) {
                startActivity(new Intent(getActivity(), PullUps.class));
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.row_layout_listview, viewGroup, false);

            ImageView imageView;
            final TextView tv1, tv2;
            imageView=(ImageView) (view.findViewById(R.id.imageview_in_listview));
            tv1 = (TextView) (view.findViewById(R.id.textview_in_listview));
            tv2 = (TextView) (view.findViewById(R.id.textview2_in_listview));

            tv1.setTextSize(20);
            tv2.setVisibility(View.INVISIBLE);

            if(i==0) {
                Picasso.with(getActivity()).load(R.drawable.push).resize(40,40).into(imageView);
            }
            else if(i==1) {
                Picasso.with(getActivity()).load(R.drawable.situp_sitdown).resize(40,40).into(imageView);
            }
            else if(i==2){
                Picasso.with(getActivity()).load(R.drawable.pull).resize(40,40).into(imageView);
            }

            tv1.setText(items[i]);
            return view;
        }
    }
    ImageListener imageListener = (position, imageView) -> imageView.setImageResource(sampleImages[position]);
}