package nl.frankkie.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by FrankkieNL on 9-8-2015.
 */
public class PlayerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;

    public class LocalBinder extends Binder {
        //Return the MediaPlayer so the Fragment can control it.
        MediaPlayer getMediaPlayer(){
            return PlayerService.this.mMediaPlayer;
        }

        void setMediaPlayer(MediaPlayer mediaPlayer){
            PlayerService.this.mMediaPlayer = mediaPlayer;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
