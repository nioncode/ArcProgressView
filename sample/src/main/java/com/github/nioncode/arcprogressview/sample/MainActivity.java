package com.github.nioncode.arcprogressview.sample;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.github.nioncode.arcprogressview.ArcProgressView;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

	private ArcProgressView mArcProgressView;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mArcProgressView = (ArcProgressView) findViewById(R.id.progressArc);
		mTextView = (TextView) findViewById(R.id.progressText);

		findViewById(R.id.btn_animate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValue();
			}
		});

		findViewById(R.id.btn_animate_fast).setOnClickListener(new OnClickListener() {
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					updateValue();
				}
			};

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessageDelayed(0, 0);
				handler.sendEmptyMessageDelayed(0, 200);
				handler.sendEmptyMessageDelayed(0, 400);
				handler.sendEmptyMessageDelayed(0, 600);
				handler.sendEmptyMessageDelayed(0, 850);
				handler.sendEmptyMessageDelayed(0, 1300);
			}
		});
	}

	private boolean reverseValue = false;

	private void updateValue() {
		float percentage = reverseValue ? 0.2f : 0.75f;
		float randomOffset = ((new Random().nextInt(20)) - 10) / (float) 100; // randomize percentage by +- 10%
		percentage += randomOffset;

		reverseValue = !reverseValue;

		String text = String.format("%1$d%%", (int) (percentage * 100));
		mTextView.setText(text);

		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			animate(percentage);
		} else {
			mArcProgressView.setAnglePercentage(percentage);
		}
	}

	@TargetApi(VERSION_CODES.HONEYCOMB)
	private void animate(float anglePercentage) {
		ObjectAnimator mAnimator;
		mAnimator = ObjectAnimator.ofFloat(mArcProgressView, "anglePercentage", anglePercentage);
		mAnimator.start();
	}

}
