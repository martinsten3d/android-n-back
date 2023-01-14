package se.kth.martsten.lab_1.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * class for several different animations to be used with views
 */
public class AnimationUtil {

    /**
     * fades in a view over a period of time
     * @param view the view that should be faded in
     * @param duration how long (in milliseconds) the fade in should take
     */
    public static void fadeInView(View view, int duration) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);
    }

    /**
     * fades out a view over a period of time
     * @param view the view that should be faded out
     * @param duration how long (in milliseconds) the fade out should take
     */
    public static void fadeOutView(View view, int duration) {
        view.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * animates the color of a view to a given color, and then back to the original color again
     * @param view the view that should change color
     * @param colorFrom the original color of the view
     * @param colorTo the color that the view should change to
     * @param duration how long time (in milliseconds) the color transition should take
     */
    public static void blinkViewColor(View view, int colorFrom, int colorTo, int duration) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        ValueAnimator colorAnimation2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);

        colorAnimation.setDuration(duration);
        colorAnimation2.setDuration(duration);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { colorAnimation2.start(); }
        });

        colorAnimation.start();

        colorAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator2) {
                view.setBackgroundColor((int) animator2.getAnimatedValue());
            }
        });
    }
}
