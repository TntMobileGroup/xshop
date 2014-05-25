package com.tom.xshop.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class DensityAdaptor {
	
	private static float mFactor = 1.0f;
	private static int mScreenWidth = 0;
	private static int mScreenHeight = 0;
	private static int dpi = 160;
	
	public static void init(Activity app) {
		//SketchBook app = SketchBook.getApp();
		//return;
		
		
		int defaultdpi = 160;
		//int dpi = 0;
		
		if (app != null) {
			DisplayMetrics dm = new DisplayMetrics();
			
			app.getWindowManager().getDefaultDisplay().getMetrics(dm);
			
			mScreenWidth = dm.widthPixels;
			mScreenHeight = dm.heightPixels;
			
			dpi = dm.densityDpi;
		}
		
		mFactor = (float)dpi / (float)defaultdpi;

	}

	public static int getDensity()
	{
		return dpi;
	}
	
	public static int getDensityIndependentValue(int value) {
		
		int outvalue = (int) ((float)value * mFactor);
		
		return outvalue;
		
	}
	
	public static float getDensityIndependentValue(float value) {
		
		float outvalue = value * mFactor;
		
		return outvalue;
	}
	
	public static int getScreenWidth() {
		return mScreenWidth;
	}
	
	public static int getScreenHeight() {
		return mScreenHeight;
	}
	
//	public static int getScreenRotation(Activity app) {
//		Display display = ((WindowManager) app.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		int rotation = display.getRotation();
//		int rotationAngle = 0;
//		if (rotation == Surface.ROTATION_0) {
//			rotationAngle = 0;
//			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//				rotationAngle -= 90;
//			}
//			Log.d("SketchBook", "rotation" + rotationAngle);
//		} else if (rotation == Surface.ROTATION_90) {
//			rotationAngle = 90;
//			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//				rotationAngle -= 90;
//			}
//			Log.d("SketchBook", "rotation" + rotationAngle);
//		} else if (rotation == Surface.ROTATION_180) {
//			rotationAngle = 180;
//			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//				rotationAngle -= 90;
//			}
//			Log.d("SketchBook", "rotation" + rotationAngle);
//		} else if (rotation == Surface.ROTATION_270) {
//			rotationAngle = 270;
//			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//				rotationAngle -= 90;
//			}
//			Log.d("SketchBook", "rotation" + rotationAngle);
//		}
//		return rotationAngle;
//	}
	
	public static int getScreenRotation(Context app) {
		Display display = ((WindowManager) app.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();
		int rotationAngle = 0;
		if (rotation == Surface.ROTATION_0) {
			rotationAngle = 0;
			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				rotationAngle -= 90;
			}
			Log.d("SketchBook", "rotation" + rotationAngle);
		} else if (rotation == Surface.ROTATION_90) {
			rotationAngle = 90;
			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				rotationAngle -= 90;
			}
			Log.d("SketchBook", "rotation" + rotationAngle);
		} else if (rotation == Surface.ROTATION_180) {
			rotationAngle = 180;
			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				rotationAngle -= 90;
			}
			Log.d("SketchBook", "rotation" + rotationAngle);
		} else if (rotation == Surface.ROTATION_270) {
			rotationAngle = 270;
			if (getCurrentOrientation(app) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				rotationAngle -= 90;
			}
			Log.d("SketchBook", "rotation" + rotationAngle);
		}
		return rotationAngle;
	}
	
	public static int getCurrentOrientation(Context app) {
		Display display = ((WindowManager) app.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		
		display.getMetrics(dm);
		
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		
		if (mScreenWidth > mScreenHeight) {
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		} else {
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
	}
}
