package com.ansen.common;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class RecoderUtils {

	private final MediaRecorder recorde = new MediaRecorder();
	private final String path;
	private static int SAMPLE_RATE_IN_HZ = 8000;

	public RecoderUtils(String path) {
		this.path = sanitizePath(path);
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".amr";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvoice" + path;
	}

	/** 开始录音 */
	public void start() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			return;
		}

		try {
			// 设置声音的来源
			recorde.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置声音的输出格式
			recorde.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置声音的编码格式
			recorde.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置音频采样率
			recorde.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
			// 设置输出文件
			recorde.setOutputFile(path);
			// 准备录音
			recorde.prepare();
			// 开始录音
			recorde.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 停止录音 */
	public void stop() {
		try {
			// 停止录音
			recorde.stop();
			// 释放资源
			recorde.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getAmplitude() {
		if (recorde != null) {
			return (recorde.getMaxAmplitude());
		} else
			return 0;
	}

}
