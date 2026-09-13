package com.sujichim.jasanjao2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;


public class Bokjindo extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// no title, no bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_bokjindo);

		PhotoView photoView = (PhotoView) findViewById(R.id.bokjindo);
		photoView.setImageResource(R.drawable.threeone3);




	}

	/** Called when leaving the activity */
	@Override
	public void onPause() {
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
