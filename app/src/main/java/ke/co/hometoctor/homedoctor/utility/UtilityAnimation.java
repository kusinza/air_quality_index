package ke.co.hometoctor.homedoctor.utility;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public class UtilityAnimation {
    ScaleAnimation scaleAnimation;
    public ScaleAnimation getscaleAnimation(long duration, boolean death){
        if(death){
            scaleAnimation=new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        }
        else
            scaleAnimation=new ScaleAnimation(0.95f, 1f, 0.95f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        return  scaleAnimation;
    }
    public ScaleAnimation getscaleAnimation2(long duration, boolean death){
        if(death){
            scaleAnimation=new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        }
        else
            scaleAnimation=new ScaleAnimation(0.95f, 1f, 0.95f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return  scaleAnimation;
    }
    public ScaleAnimation getscaleAnimation2back(long duration, boolean death){
        if(death){
            scaleAnimation=new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        }
        else
            scaleAnimation=new ScaleAnimation(0.95f, 1f, 0.95f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return  scaleAnimation;
    }
    public AlphaAnimation getAlphaanime(){
        AlphaAnimation blink= new AlphaAnimation(0.0f,1.0f);
        blink.setDuration(150);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);
        return  blink;
    }
    public Animation fade (){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(2000);
        fadeOut.setDuration(2000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        //animation.addAnimation(fadeOut);
        animation.addAnimation(getscaleAnimation(2000,true));
        return animation;
    }

}
