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

public class Fragment_Diet_Plan extends Fragment {

    CarouselView carouselView;
    ListView zlistview;
    int[] Images = {R.drawable.one, R.drawable.two, R.drawable.three,R.drawable.four};
    MyAdapter adapter;
    String items[]={"Weight Loss","Weight Gain"};

    public Fragment_Diet_Plan() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diet_plan,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        zlistview=(ListView) (getActivity().findViewById(R.id.lv));
        carouselView = (CarouselView)getActivity(). findViewById(R.id.carouselView);

        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(Images.length);

        adapter=new MyAdapter();
        zlistview.setAdapter(adapter);
        zlistview.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getActivity(), DietActivity.class);
            intent.putExtra("option", items[position]);
            startActivity(intent);
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
                Picasso.with(getActivity()).load(R.drawable.apple).resize(40,40).into(imageView);
            } else if(i==1){
                Picasso.with(getActivity()).load(R.drawable.b).resize(40,40).into(imageView);
            }

            tv1.setText(items[i]);
            return view;
        }
    }
    ImageListener imageListener = (position, imageView) -> imageView.setImageResource(Images[position]);
}