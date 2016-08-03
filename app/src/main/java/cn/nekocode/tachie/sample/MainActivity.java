package cn.nekocode.tachie.sample;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageSwitcher imageSwitcher;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Bitmap test = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        final Bitmap test2 = BitmapFactory.decodeResource(getResources(), R.drawable.test2);

        imageView = ((ImageView) findViewById(R.id.imageSwitcher));
        imageSwitcher = ((ImageSwitcher) findViewById(R.id.imageSwitcher2));
        assert imageView != null;
        assert imageSwitcher != null;

        imageView.setImageBitmap(test);
        animator = ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                if (nextDrawableId == R.drawable.test) {
                    imageView.setImageBitmap(TachieRenderScriptUtil.trans(test, test2, alpha, MainActivity.this));
                } else {
                    imageView.setImageBitmap(TachieRenderScriptUtil.trans(test2, test, alpha, MainActivity.this));
                }
            }
        });

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        imageSwitcher.setImageResource(R.drawable.test);

        Animation animationIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animationIn.setDuration(2000);
        Animation animationOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animationOut.setDuration(2000);
        imageSwitcher.setInAnimation(animationIn);
        imageSwitcher.setOutAnimation(animationOut);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private int nextDrawableId = R.drawable.test2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_start) {
            imageSwitcher.setImageResource(nextDrawableId);
            animator.start();
            nextDrawableId = nextDrawableId == R.drawable.test ? R.drawable.test2 : R.drawable.test;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
