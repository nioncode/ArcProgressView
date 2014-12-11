package de.nioncode.gaugeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;


public class GaugeView extends View {
	private static final float DEFAULT_INITIAL_ANGLE = 0.0f;
	private static final float DEFAULT_MIN_ANGLE = 0.0f;
	private static final float DEFAULT_MAX_ANGLE = 360.0f;
	private static final float DEFAULT_START_ANGLE = 0.0f;
	private static final int DEFAULT_RADIAL_FOREGROUND_COLOR = 0xff0000ff;
	private static final int DEFAULT_RADIAL_BACKGROUND_COLOR = 0x880000ff;
	private static final float DEFAULT_RADIAL_FOREGROUND_STROKE_WIDTH = 5.0f;
	private static final float DEFAULT_RADIAL_BACKGROUND_STROKE_WIDTH = 2.0f;

	private float mAngle = 0.0f;
	private float mMinAngle = DEFAULT_MIN_ANGLE;
	private float mMaxAngle = DEFAULT_MAX_ANGLE;
	private float mStartAngle = DEFAULT_START_ANGLE;
	private Paint mRadialForeground;
	private Paint mRadialBackground;
	private RectF mBounds;

	public GaugeView(Context context) {
		super(context);
		init();
	}

	public GaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GaugeView, 0, 0);
		try {
			parseStyledAttributes(a);
		} finally {
			a.recycle();
		}
	}

	public GaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GaugeView, defStyleAttr, 0);
		try {
			parseStyledAttributes(a);
		} finally {
			a.recycle();
		}
	}

	@TargetApi(21)
	public GaugeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GaugeView, defStyleAttr,
		                                                         defStyleRes);
		try {
			parseStyledAttributes(a);
		} finally {
			a.recycle();
		}
	}

	/**
	 * NOTE: Adapted from Platform-21 View.java
	 * <p/>
	 * Utility to reconcile a desired size and state, with constraints imposed
	 * by a MeasureSpec.  Will take the desired size, unless a different size
	 * is imposed by the constraints.  The returned value is a compound integer,
	 * with the resolved size in the {@link #MEASURED_SIZE_MASK} bits and
	 * optionally the bit {@link #MEASURED_STATE_TOO_SMALL} set if the resulting
	 * size is smaller than the size the view wants to be.
	 *
	 * @param size        How big the view wants to be
	 * @param measureSpec Constraints imposed by the parent
	 * @return Size information bit mask as defined by
	 * {@link #MEASURED_SIZE_MASK} and {@link #MEASURED_STATE_TOO_SMALL}.
	 */
	public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
		int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (specMode) {
			case MeasureSpec.UNSPECIFIED:
				result = size;
				break;
			case MeasureSpec.AT_MOST:
				if (specSize < size) {
					if (VERSION.SDK_INT > VERSION_CODES.HONEYCOMB) {
						result = specSize | MEASURED_STATE_TOO_SMALL;
					} else {
						result = specSize;
					}
				} else {
					result = size;
				}
				break;
			case MeasureSpec.EXACTLY:
				result = specSize;
				break;
		}
		if (VERSION.SDK_INT > VERSION_CODES.HONEYCOMB) {
			return result | (childMeasuredState & MEASURED_STATE_MASK);
		}
		return result;
	}

	public float getAngle() {
		return mAngle;
	}

	public void setAngle(float angle) {
		mAngle = angle;
		invalidate();
	}

	public float getMinAngle() {
		return mMinAngle;
	}

	public void setMinAngle(float minAngle) {
		mMinAngle = minAngle;
		invalidate();
		requestLayout();
	}

	public float getMaxAngle() {
		return mMaxAngle;
	}

	public void setMaxAngle(float maxAngle) {
		mMaxAngle = maxAngle;
		invalidate();
		requestLayout();
	}

	public float getStartAngle() {
		return mStartAngle;
	}

	public void setStartAngle(float startAngle) {
		mStartAngle = startAngle;
		invalidate();
		requestLayout();
	}

	public int getRadialForegroundColor() {
		return mRadialForeground.getColor();
	}

	public void setRadialForegroundColor(int radialForegroundColor) {
		mRadialForeground.setColor(radialForegroundColor);
		invalidate();
	}

	public float getRadialForegroundStrokeWidth() {
		return mRadialForeground.getStrokeWidth();
	}

	public void setRadialForegroundStrokeWidth(float radialForegroundStrokeWidth) {
		mRadialForeground.setStrokeWidth(radialForegroundStrokeWidth);
		invalidate();
		requestLayout();
	}

	public int getRadialBackgroundColor() {
		return mRadialBackground.getColor();
	}

	public void setRadialBackgroundColor(int radialBackgroundColor) {
		mRadialBackground.setColor(radialBackgroundColor);
		invalidate();
	}

	public float getRadialBackgroundStrokeWidth() {
		return mRadialBackground.getStrokeWidth();
	}

	public void setRadialBackgroundStrokeWidth(float radialBackgroundStrokeWidth) {
		mRadialBackground.setStrokeWidth(radialBackgroundStrokeWidth);
		invalidate();
		requestLayout();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(mBounds, mStartAngle, mMaxAngle - mMinAngle, false, mRadialBackground);
		canvas.drawArc(mBounds, mStartAngle, mAngle, false, mRadialForeground);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
		final int width = resolveSizeAndState(minWidth, widthMeasureSpec, 1);

		// Make the height as high as the MeasureSpec allows while trying to make it the same size as the width.
		final int height = resolveSizeAndState(MeasureSpec.getSize(width), heightMeasureSpec, 0);

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float maxStrokeWidth = Math.max(mRadialForeground.getStrokeWidth(), mRadialBackground.getStrokeWidth());
		mBounds = new RectF(getPaddingLeft() + maxStrokeWidth, getPaddingTop() + maxStrokeWidth,
		                    w - getPaddingRight() - maxStrokeWidth,
		                    h - getPaddingBottom() - maxStrokeWidth);
	}

	private void parseStyledAttributes(TypedArray a) {
		mAngle = a.getFloat(R.styleable.GaugeView_initialAngle, DEFAULT_INITIAL_ANGLE);
		mMinAngle = a.getFloat(R.styleable.GaugeView_minAngle, DEFAULT_MIN_ANGLE);
		mMaxAngle = a.getFloat(R.styleable.GaugeView_maxAngle, DEFAULT_MAX_ANGLE);
		mStartAngle = a.getFloat(R.styleable.GaugeView_startAngle, DEFAULT_START_ANGLE);
		final int radialForegroundColor = a.getColor(R.styleable.GaugeView_radialForegroundColor,
		                                             DEFAULT_RADIAL_FOREGROUND_COLOR);
		final float radialForegroundStrokeWidth = a.getDimension(R.styleable.GaugeView_radialForegroundStrokeWidth,
		                                                         DEFAULT_RADIAL_FOREGROUND_STROKE_WIDTH);
		final int radialBackgroundColor = a.getColor(R.styleable.GaugeView_radialBackgroundColor,
		                                             DEFAULT_RADIAL_BACKGROUND_COLOR);
		final float radialBackgroundStrokeWidth = a.getDimension(R.styleable.GaugeView_radialBackgroundStrokeWidth,
		                                                         DEFAULT_RADIAL_BACKGROUND_STROKE_WIDTH);

		mRadialForeground.setColor(radialForegroundColor);
		mRadialForeground.setStrokeWidth(radialForegroundStrokeWidth);
		mRadialBackground.setColor(radialBackgroundColor);
		mRadialBackground.setStrokeWidth(radialBackgroundStrokeWidth);
	}

	private void init() {
		mRadialForeground = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRadialForeground.setColor(DEFAULT_RADIAL_FOREGROUND_COLOR);
		mRadialForeground.setStrokeWidth(DEFAULT_RADIAL_FOREGROUND_STROKE_WIDTH);
		mRadialForeground.setStyle(Paint.Style.STROKE);
		mRadialForeground.setStrokeCap(Paint.Cap.ROUND);

		mRadialBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRadialBackground.setColor(DEFAULT_RADIAL_BACKGROUND_COLOR);
		mRadialBackground.setStrokeWidth(DEFAULT_RADIAL_BACKGROUND_STROKE_WIDTH);
		mRadialBackground.setStyle(Paint.Style.STROKE);
	}

}
