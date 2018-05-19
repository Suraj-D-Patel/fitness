package patel.d.suraj.fitness;

/**
 * Created by suraj.
 */

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.io.FileDescriptor;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    private Button btn_fwd,btn_pause,btn_play,btn_rewind;
    private ImageView album_image;
    Context context;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView time_start,time_end,song_title;
    public static int oneTimeOnly = 0;
    boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        context=getApplicationContext();
        btn_fwd = (Button) findViewById(R.id.btn_fwd);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_rewind = (Button)findViewById(R.id.btn_rewind);
        album_image = (ImageView)findViewById(R.id.album_image);
        time_start = (TextView)findViewById(R.id.time_start);
        time_end = (TextView)findViewById(R.id.time_end);
        song_title = (TextView)findViewById(R.id.song_title);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        song_title.setText(title+".mp3");

        Long id=getIntent().getLongExtra("pic",0);

        Bitmap bp=getAlbumart(id);
        if(bp!=null){
            album_image.setImageBitmap(bp);
        } else {
            Picasso.with(MusicPlayerActivity.this).load(R.drawable.music_photos_background).into(album_image);
        }

        final Uri myUri =intent.getParcelableExtra("imageUri");

        if(global.mediaPlayer==null) {
            global.mediaPlayer=new MediaPlayer();
        }

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        seekbar.setProgress(0);
        btn_pause.setEnabled(false);

        playMusic(myUri);

        btn_play.setOnClickListener(v -> { playMusic(myUri); });

        btn_pause.setOnClickListener(v -> {
            flag=false;
            global.mediaPlayer.pause();
            btn_pause.setEnabled(false);
            btn_play.setEnabled(true);
        });

        btn_fwd.setOnClickListener(v -> {
            int temp = (int)startTime;
            if((temp+forwardTime)<=finalTime){
                startTime = startTime + forwardTime;
                global.mediaPlayer.seekTo((int) startTime);
                //Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
            }
        });

        btn_rewind.setOnClickListener(v -> {
            int temp = (int)startTime;
            if((temp-backwardTime)>0){
                startTime = startTime - backwardTime;
                global.mediaPlayer.seekTo((int) startTime);
              // Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
            }else{
               Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playMusic(Uri myUri) {
        if(flag) {
            try {
                global.mediaPlayer.reset();
                global.mediaPlayer.setDataSource(getApplicationContext(), myUri);
                global.mediaPlayer.prepare();
                global.mediaPlayer.start();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        }
        else if(!flag) { global.mediaPlayer.start(); }
        finalTime = global.mediaPlayer.getDuration();
        startTime = global.mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time_end.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime))));

        time_start.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime))));

        seekbar.setProgress((int)startTime);
        seekbar.setMax(global.mediaPlayer.getDuration()/1000);
        myHandler.postDelayed(UpdateSongTime,100);
        btn_pause.setEnabled(true);
        btn_play.setEnabled(false);
    }

    public Bitmap getAlbumart(Long album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "frag_exercise_pic_four");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return bm;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = global.mediaPlayer.getCurrentPosition();
            time_start.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekbar.setProgress((int)global.mediaPlayer.getCurrentPosition()/1000);

            myHandler.postDelayed(this, 100);
        }
    };
}