package ru.devtron.dagturism.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 30.01.2016.
 */
public class RobotoTextMedium extends TextView {
    public RobotoTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RobotoTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface medium = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(medium);
    }
}
