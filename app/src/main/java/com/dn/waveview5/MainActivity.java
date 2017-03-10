package com.dn.waveview5;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WaveView waveView = (WaveView) findViewById(R.id.waveView);
		waveView.startAnimation();
		
//		WaveView1 waveView = (WaveView1) findViewById(R.id.waveView);
//		waveView.startAnimation();
		
//		WaveView2 waveView = (WaveView2) findViewById(R.id.waveView);
//		waveView.startAnimation();
	}
}
