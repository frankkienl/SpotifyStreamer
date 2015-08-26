package nl.frankkie.spotifystreamer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by FrankkieNL on 9-8-2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener{

    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    private boolean mIsPrepared = false;
    private Context mContext;
    private final boolean startWhenPrepared = true;
    private MediaCallbacks mCallbacks;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsPrepared = true;
    }

    public class LocalBinder extends Binder {

        public void setCallbacks(MediaCallbacks callbacks){
            mCallbacks = callbacks;
        }

        //Return the MediaPlayer so the Fragment can control it.
        MediaPlayer getMediaPlayer(){
            return PlayerService.this.mMediaPlayer;
        }

        void setMediaPlayer(MediaPlayer mediaPlayer){
            PlayerService.this.mMediaPlayer = mediaPlayer;
            if (mMediaPlayer != null){
                mMediaPlayer.setOnPreparedListener(PlayerService.this);
            } else {
                mIsPrepared = false; //as mediaplayer is null
            }
        }

        boolean isMediaPrepared(){
            return mIsPrepared;
        }

        public void initMediaPlayer(String mediaUrl) {
            setMediaPlayer(new MediaPlayer());
            try {
                getMediaPlayer().setDataSource(mContext, Uri.parse(mediaUrl));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, getString(R.string.spotify_error) + ":\n" + getString(R.string.cannot_play), Toast.LENGTH_LONG).show();
                return;
            }
            getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //mediaPlayerIsPrepared = true;
                    mIsPrepared = true;
                    if (startWhenPrepared) {
                        mCallbacks.playbackStarted();
                        //getMediaPlayer().start();
                    }
                }
            });
            getMediaPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mIsPrepared = false;
                    Toast.makeText(mContext, getString(R.string.spotify_error) + ":\n" + getString(R.string.cannot_play), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            getMediaPlayer().prepareAsync();
        }
    }

    public interface MediaCallbacks {
        public void playbackStarted();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
