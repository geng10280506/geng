package com.mediarecordtest;

import java.io.File;
import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button mRecordButton;
	private Button mStopRecordButton;
	private Button mPlayBtnButton;
	private Button mStopPlayButton;

	private MediaRecorder recorder;
	private MediaPlayer player;

	private File soundFile;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();

	}

	private void initViews() {
		mRecordButton = (Button) findViewById(R.id.record_btn);
		mStopRecordButton = (Button) findViewById(R.id.stop_record_btn);
		mPlayBtnButton = (Button) findViewById(R.id.play_btn);
		mStopPlayButton = (Button) findViewById(R.id.stop_play_btn);

		mRecordButton.setOnClickListener(this);
		mStopRecordButton.setOnClickListener(this);
		mPlayBtnButton.setOnClickListener(this);
		mStopPlayButton.setOnClickListener(this);
		try {
			path = Environment.getExternalStorageDirectory().getCanonicalFile()
					+ "/sound.amr";
			player = new MediaPlayer();
			player.setLooping(true);
			player.setDataSource(path);
			player.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.record_btn:
			record();
			break;
		case R.id.stop_record_btn:
			stopRecord();
			break;
		case R.id.play_btn:
			play();
			break;
		case R.id.stop_play_btn:
			pausePlay();
			break;
		default:
			break;
		}

	}

	private void record() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(), "SD卡不存在,请插入SD卡!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			soundFile = new File(path);
			recorder = new MediaRecorder();
			// 设置录音的声音来源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置录制声音的输出格式
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// 设置声音的编码格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置声音文件保存位置
			recorder.setOutputFile(soundFile.getAbsolutePath());
			// 准备录制
			recorder.prepare();
			// 开始录制
			recorder.start();
			Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_SHORT)
					.show();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

	private void stopRecord() {
		if (soundFile != null && soundFile.exists()) {
			recorder.stop();
			recorder.release();
			recorder = null;
			Toast.makeText(getApplicationContext(), "停止录音", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void pausePlay() {
		if (null != player) {
			player.pause();
			Toast.makeText(getApplicationContext(), "暂停播放", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void play() {
		if (soundFile != null && soundFile.exists()) {
			if (player.isPlaying()) {
				return;
			}
			player.start();
			
			Toast.makeText(getApplicationContext(), "开始播放", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != player) {
			player.release();
		}
		if (null != recorder) {
			recorder.release();
		}
	}
}
