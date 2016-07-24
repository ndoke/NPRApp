/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;

public class MediaPlayerControlHelper implements MediaController.MediaPlayerControl, OnPreparedListener {
	private MediaPlayer mp = new MediaPlayer();
	private String url;
	private Activity parent;
	private MediaController mc;
	private Handler handler;

	public MediaPlayerControlHelper(String url, Activity parent) {
		this.url = url;
		this.parent = parent;
		this.handler = new Handler();
		mc = new MediaController(parent);
		try {
			mp.setOnPreparedListener(this);
			mp.setDataSource(url);
			mp.prepareAsync();
		} catch (Exception e) {
			Log.w(MainActivity.LOGGING_KEY, "");
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		mp.start();
	}

	public void show() {
		mc.show(0);
	}

	public void stop() {
		mc.hide();
		mp.stop();
		mp.release();
	}

	@Override
	public void seekTo(int pos) {
		mp.seekTo(pos);
	}

	@Override
	public void pause() {
		mp.pause();
	}

	@Override
	public boolean isPlaying() {
		return mp.isPlaying();
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return mp.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mp.getCurrentPosition();
	}

	@Override
	public int getBufferPercentage() {
		// mp.
		return 0;
	}

	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		mc.setMediaPlayer(this);
		mc.setAnchorView(parent.findViewById(R.id.storyParentRelLayout));

		handler.post(new Runnable() {
			public void run() {
				mc.setEnabled(true);
				mc.show(0);
			}
		});

	}
}
