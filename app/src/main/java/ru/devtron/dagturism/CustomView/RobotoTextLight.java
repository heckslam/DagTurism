package ru.devtron.dagturism.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 30.01.2016.
 */
public class RobotoTextLight extends TextView {
    public RobotoTextLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RobotoTextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextLight(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface light = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setTypeface(light);
    }
}
