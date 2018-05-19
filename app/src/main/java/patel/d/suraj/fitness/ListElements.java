package patel.d.suraj.fitness;

import android.net.Uri;

/**
 * Created by suraj.
 */

public class ListElements {

    String Title;
    String Artist;
    Uri uri;
    Long album_id;

    public ListElements(String title, String artist,Uri a,Long album_id) {
        Title = title;
        Artist = artist;
        this.album_id=album_id;
        this.uri=a;
    }
    public Uri getA() {
        return uri;
    }

    public void setA(Uri a) {
        this.uri = a;
    }

    public Long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(Long album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }
}





