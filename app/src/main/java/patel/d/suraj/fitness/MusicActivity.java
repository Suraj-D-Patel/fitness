package patel.d.suraj.fitness;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj.
 */

public class MusicActivity extends AppCompatActivity {

    Context context;
    ListView listView;
    List<ListElements> ListElementsArrayList;
    MYAdapter adapter;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        listView = (ListView) findViewById(R.id.lv_music);
        context = getApplicationContext();
        ListElementsArrayList = new ArrayList<>();

        GetAllMediaMp3Files();

        listView.setAdapter(adapter);
        // ListView on item selected listener.
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent in=new Intent(MusicActivity.this,MusicPlayerActivity.class);
            in.putExtra("title",ListElementsArrayList.get(position).getTitle());
            in.putExtra("imageUri",ListElementsArrayList.get(position).getA());
            in.putExtra("pic",ListElementsArrayList.get(position).getAlbum_id());
            startActivity(in);
        });
        adapter=new MYAdapter();
        listView.setAdapter(adapter);
    }

    public Bitmap getAlbumart(Long album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "frag_exercise_pic_four");
            if (pfd != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd, new Rect(1,2,3,4),options);
            }
        } catch (Exception e) {e.printStackTrace();}
        return bm;
    }

    class MYAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ListElementsArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return ListElementsArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.row_layout_listview, viewGroup, false);
            ListElements stud = ListElementsArrayList.get(i);
            ImageView imv1;
            final TextView tv1, tv2;
            imv1=(ImageView) (view.findViewById(R.id.imageview_in_listview));
            tv1 = (TextView) (view.findViewById(R.id.textview_in_listview));
            tv2 = (TextView) (view.findViewById(R.id.textview2_in_listview));
            tv1.setText(stud.getTitle());
            tv2.setText(stud.getArtist());
            Bitmap bp=getAlbumart(stud.getAlbum_id());

            if(bp!=null){
                imv1.setImageBitmap(bp);
            }
            else {
                Picasso.with(MusicActivity.this).load(R.drawable.music_photos_background).resize(37,37).into(imv1);
            }
            return view;
        }
    }

    public String getRealPathFromURI(Context context,Uri a) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(a,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }}

    public void GetAllMediaMp3Files(){
        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cursor = contentResolver.query(uri,null,null,null,null);
        if (cursor == null) {
            Toast.makeText(MusicActivity.this,"Something Went Wrong.", Toast.LENGTH_LONG);
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MusicActivity.this,"No Music Found on SD Card.", Toast.LENGTH_LONG);
        }
        else {
            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int album_id=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int uricolumn=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);
                String SongTitle = cursor.getString(Title);
                String Artist= cursor.getString(artist);
                Long album_ID=cursor.getLong(album_id);
                Uri a=Uri.parse(cursor.getString(uricolumn));
                ListElementsArrayList.add(new ListElements(SongTitle,Artist,a,album_ID));
            } while (cursor.moveToNext());
        }
    }
}