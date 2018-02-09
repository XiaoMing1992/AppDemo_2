package guyuanjun.com.myappdemo.fragment.my.store.music.business.lyric;

/**
 * Created by HP on 2017-4-27.
 */

public class Music {
    private String songId;

    private String songName;

    private String artist;

    private String albumId;

    private String lyricPath;

    private String title;

    public String getSongId()
    {
        return songId;
    }

    public void setSongId(String songId)
    {
        this.songId = songId;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(String albumId)
    {
        this.albumId = albumId;
    }

    public String getLyricPath()
    {
        return lyricPath;
    }

    public void setLyricPath(String lyricPath)
    {
        this.lyricPath = lyricPath;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "GCMLyricInfo [songId=" + songId + ", songName=" + songName + ", artist=" + artist + ", albumId="
                + albumId + ", lyricPath=" + lyricPath + "]";
    }
}
