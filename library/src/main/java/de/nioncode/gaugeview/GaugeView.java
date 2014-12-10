package de.nioncode.gaugeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class GaugeView extends View {

	public GaugeView(Context context) {
		super(context);
	}

	public GaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public GaugeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

}
