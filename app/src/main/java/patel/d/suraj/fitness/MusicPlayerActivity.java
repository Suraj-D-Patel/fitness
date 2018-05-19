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
import com.squareup.picasso.Picasso;
import java.io.FileDescriptor;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    private Button b1,b2,b3,b4;
    private ImageView iv;
    Context context;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    public static int oneTimeOnly = 0;
    boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        context=getApplicationContext();
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        iv = (ImageView)findViewById(R.id.imageView);
        tx1 = (TextView)findViewById(R.id.textView2);
        tx2 = (TextView)findViewById(R.id.textView3);
        tx3 = (TextView)findViewById(R.id.textView);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        tx3.setText(""+title+".mp3");

        Long id=getIntent().getLongExtra("pic",0);

        Bitmap bp=getAlbumart(id);
        if(bp!=null){
            iv.setImageBitmap(bp);
        } else {
            Picasso.with(MusicPlayerActivity.this).load(R.drawable.icon).into(iv);
        }

        final Uri myUri =intent.getParcelableExtra("imageUri");

        if(global.mediaPlayer==null) {
            global.mediaPlayer=new MediaPlayer();
        }

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        seekbar.setProgress(0);
        b2.setEnabled(false);

        playMusic(myUri);

        b3.setOnClickListener(v -> { playMusic(myUri); });

        b2.setOnClickListener(v -> {
            flag=false;
            global.mediaPlayer.pause();
            b2.setEnabled(false);
            b3.setEnabled(true);
        });

        b1.setOnClickListener(v -> {
            int temp = (int)startTime;
            if((temp+forwardTime)<=finalTime){
                startTime = startTime + forwardTime;
                global.mediaPlayer.seekTo((int) startTime);
                //Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
            }
        });

        b4.setOnClickListener(v -> {
            int temp = (int)startTime;
            if((temp-backwardTime)>0){
                startTime = startTime - backwardTime;
                global.mediaPlayer.seekTo((int) startTime);
               // Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
            }else{
               // Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
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

        tx2.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime))));

        tx1.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime))));

        seekbar.setProgress((int)startTime);
        seekbar.setMax(global.mediaPlayer.getDuration()/1000);
        myHandler.postDelayed(UpdateSongTime,100);
        b2.setEnabled(true);
        b3.setEnabled(false);
    }

    public Bitmap getAlbumart(Long album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r");

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
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekbar.setProgress((int)global.mediaPlayer.getCurrentPosition()/1000);

            myHandler.postDelayed(this, 100);
        }
    };
}