package ru.ino.senseix;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RadiusLinearLayout extends LinearLayout {
	
	private float tl = 0f, tr = 0f, br = 0f, bl = 0f;
	private Path path = new Path();
	private RectF rect = new RectF();
	
	public RadiusLinearLayout(Context c) {
		super(c);
		setWillNotDraw(false);
	}
	
	public RadiusLinearLayout(Context c, AttributeSet a) {
		super(c, a);
		setWillNotDraw(false);
	}
	
	public void setRadius(float radius) {
		tl = radius;
		tr = radius;
		br = radius;
		bl = radius;
		invalidate();
	}
	
	public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
		tl = topLeft;
		tr = topRight;
		br = bottomRight;
		bl = bottomLeft;
		invalidate();
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		int save = canvas.save();
		
		rect.set(0, 0, getWidth(), getHeight());
		path.reset();
		
		path.addRoundRect(
		rect,
		new float[]{
			tl, tl,
			tr, tr,
			br, br,
			bl, bl
		},
		Path.Direction.CW
		);
		
		canvas.clipPath(path);
		super.dispatchDraw(canvas);
		canvas.restoreToCount(save);
	}
}
