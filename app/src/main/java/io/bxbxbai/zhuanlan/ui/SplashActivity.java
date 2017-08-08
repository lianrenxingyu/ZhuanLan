package io.bxbxbai.zhuanlan.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import io.bxbxbai.common.StopWatch;
import io.bxbxbai.zhuanlan.R;
import io.bxbxbai.zhuanlan.utils.FastBlur;

import java.util.Random;

/**
 * 闪屏Activity
 *
 * @author bxbxbai
 */
public class SplashActivity extends Activity {

    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;

    private static final int[] SPLASH_ARRAY = {
            R.drawable.splash0,
            R.drawable.splash1,
            R.drawable.splash2,
            R.drawable.splash3,
            R.drawable.splash4,
            R.drawable.splash5,
            R.drawable.splash6,
            R.drawable.splash7,
            R.drawable.splash8,
            R.drawable.splash9,
            R.drawable.splash10,
            R.drawable.splash11,
            R.drawable.splash12,
            R.drawable.splash13,
            R.drawable.splash14,
            R.drawable.splash15,
            R.drawable.splash16,
    };

    private ImageView mSplashImage;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        mSplashImage = ButterKnife.findById(this, R.id.iv_entry);
        titleView = ButterKnife.findById(this, R.id.tv_title);
//      随机获取
        Random r = new Random(SystemClock.elapsedRealtime());
        mSplashImage.setImageResource(SPLASH_ARRAY[r.nextInt(SPLASH_ARRAY.length)]);
//        mSplashImage.setImageResource(SPLASH_ARRAY[15]);
        animateImage();

//        下面的模糊化效果没有成功
//        applyBlur();
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void animateImage() {
//          ObjectAnimator 是一个动画类，ofFloat是它的一个静态工厂方法，
//          第一个参数是操纵的控件，第二个参数是何种动画属性，最后是一个可变参数，表示具体缩放值，或者旋转角度等

//          这里表示，X轴和Y轴的缩放动画
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mSplashImage, View.SCALE_X, 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mSplashImage, View.SCALE_Y, 1f, SCALE_END);

//        AnimatorSet 属性动画集合，可以同时播放一系列属性动画
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                开始新的应用的主界面activity
                MainActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
            }
        });
    }

    //http://blog.jobbole.com/63894/

//    模糊化效果
    private void applyBlur() {
        mSplashImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSplashImage.getViewTreeObserver().removeOnPreDrawListener(this);
                mSplashImage.buildDrawingCache();

                blur(mSplashImage.getDrawingCache(), titleView);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        float radius = 10;
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(this);
        Allocation allocation = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, allocation.getElement());

        blur.setInput(allocation);
        blur.setRadius(radius);
        blur.forEach(allocation);
        allocation.copyTo(overlay);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

        rs.destroy();
        StopWatch.log(System.currentTimeMillis() - startMs + "ms");
    }
//虚化方法，没有被实现
    private void blur2(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
//        }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }
}
