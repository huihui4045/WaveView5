package com.dn.waveview5;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class WaveView extends View {
	
	private Paint paint;
	private Path path;
	private int waveLength = 400;
	private int dx;
	private int dy;
	private Bitmap mBitmap;
	private int width;
	private int height;
	
	private Region region;
	private int waveHeight = 80;
	private int waveView_boatBitmap;
	private boolean waveView_rise;
	private int duration;
	private int originY;
	private ValueAnimator animator;
	
	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initAttrs(context,attrs);
		init();
	}
	
	//初始化自定义属性
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
		waveView_boatBitmap = (int) a.getDimension(R.styleable.WaveView_boatBitmap, 0);
		waveView_rise = a.getBoolean(R.styleable.WaveView_rise, false);
		duration = (int) a.getDimension(R.styleable.WaveView_duration, 2000);
		originY = (int) a.getDimension(R.styleable.WaveView_originY, 500);
		waveHeight = (int) a.getDimension(R.styleable.WaveView_waveHeight, 200);
		waveLength = (int) a.getDimension(R.styleable.WaveView_waveLength, 400);
		a.recycle();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;       // 缩放图片
		if(waveView_boatBitmap>0){
			mBitmap = BitmapFactory.decodeResource(getResources(), waveView_boatBitmap, options);
		}else{
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg2, options);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
		
		width = widthSize;
		height = heightSize;
//			width = getMeasuredWidth();
//			height = getMeasuredHeight();
		if(originY==0){
			originY = height;
		}
	}

	private void init() {
		paint = new Paint();
//			paint.setColor(Color.RED);
		paint.setColor(getResources().getColor(R.color.water_color));
		paint.setStyle(Style.FILL_AND_STROKE);
		
		path = new Path();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		setPathData();
		canvas.drawPath(path, paint);
		//画小船
		//小船的坐标
//		PathMeasure p = new PathMeasure(path, forceClosed);
//		p.getLength()/2
//		p.getPosTan(distance, pos, tan)
		
		Rect bounds = region.getBounds();
		/**
		 * 从波峰往下滑到originY的时候，bounds.top是不断增加的。
		 * 下半部分，发生增加变化的是bounds.bottom
		 */
		if(bounds.top<originY){
			canvas.drawBitmap(mBitmap, bounds.right-mBitmap.getWidth()/2, bounds.top-mBitmap.getHeight()/2, paint);
		}else{
			canvas.drawBitmap(mBitmap, bounds.right-mBitmap.getWidth()/2, bounds.bottom-mBitmap.getHeight()/2, paint);
		}
		
		 System.out.println("bounds:"+bounds.left+","+bounds.top+","+bounds.right+","+bounds.bottom);
	}

	private void setPathData() {
		path.reset();
		int halfWaveLength = waveLength/2;
		path.moveTo(-waveLength+dx, originY - dy);
		for (int i = -waveLength; i < width + waveLength; i += waveLength) {
//			path.quadTo(x1, y1, x2, y2)
			path.rQuadTo(halfWaveLength/2, -waveHeight, halfWaveLength, 0);
			path.rQuadTo(halfWaveLength/2, waveHeight, halfWaveLength, 0);
		}
		float x = width/2;
		region = new Region();
		Region clip = new Region((int)(x-0.1), 0, (int)x, height*2);
		//用一个矩形区域去切割一个path路径得到一个矩形区域
		region.setPath(path, clip);
		
		
		path.lineTo(width, height);
		path.lineTo(0, height);
		path.close();
		
	}
	
	public void startAnimation() {
		//dx不断地增加
		animator = ValueAnimator.ofFloat(0,1);
		animator.setDuration(duration);
		animator.setInterpolator(new LinearInterpolator());
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float fraction = (float) animation.getAnimatedValue();
				dx = (int) (waveLength*fraction);
//				dy += 5;
				postInvalidate();
			}
		});
		animator.start();
	}
	

}
