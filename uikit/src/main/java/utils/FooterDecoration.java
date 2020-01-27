package utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class FooterDecoration extends RecyclerView.ItemDecoration {

    private View mLayout;
    private int viewResID;
    private int drawableResID;
    private Activity activity;
    private final String TAG = FooterDecoration.class.getSimpleName();

    public FooterDecoration(final Activity activity, RecyclerView parent, @LayoutRes int resId, @IdRes int imageViewResID, @DrawableRes int drawableResID) {
        // inflate and measure the layout
        if (activity != null) {
            mLayout = LayoutInflater.from(activity).inflate(resId, parent, false);
            mLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            this.viewResID = imageViewResID;
            this.drawableResID = drawableResID;
            this.activity = activity;
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // layout basically just gets drawn on the reserved space on top of the first view
        mLayout.layout(parent.getLeft(), 0, parent.getRight(), mLayout.getMeasuredHeight());
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                c.save();
                c.translate(0, view.getBottom());
                mLayout.draw(c);
                c.restore();

                final ImageView imageView = mLayout.findViewById(viewResID);
                final AnimatedVectorDrawableCompat avdCompat = AnimatedVectorDrawableCompat.create(activity, drawableResID);
                avdCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        imageView.invalidateDrawable(avdCompat);
                        avdCompat.start();
                    }
                });
                imageView.setImageDrawable(avdCompat);

                avdCompat.start();
                break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mLayout.getMeasuredHeight());
        } else {
            outRect.setEmpty();
        }
    }
}