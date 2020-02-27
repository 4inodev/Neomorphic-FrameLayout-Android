package com.chinodev.androidneomorphframelayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

public class NeomorphFrameLayout extends FrameLayout {
    //attributes
    private String SHAPE_TYPE;
    private String SHADOW_TYPE;
    private int CORNER_RADIUS;
    private int ELEVATION;
    private int HIGHLIGHT_COLOR;
    private int SHADOW_COLOR;
    private int BACKGROUND_COLOR;
    private int LAYER_TYPE;
    private boolean SHADOW_VISIBLE;

    //global variables
    private int SHAPE_PADDING = 0;
    //constants
    private final String SHAPE_TYPE_RECTANGLE = "1";
    private final String SHAPE_TYPE_CIRCLE = "2";
    private final String SHADOW_TYPE_OUTER = "1";
    private final String SHADOW_TYPE_INNER = "2";
    //global objects
    private Paint basePaint;
    private Paint paintShadow;
    private Paint paintHighLight;
    private Path basePath;
    private Path pathShadow;
    private Path pathHighlight;
    private RectF rectangle;


    public NeomorphFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NeomorphFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NeomorphFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        getAttrs(context, attrs);
        initPaints();
        rectangle = new RectF(SHAPE_PADDING, SHAPE_PADDING, this.getWidth() - SHAPE_PADDING, this.getHeight() - SHAPE_PADDING);
    }

    public void getAttrs(Context context, AttributeSet attrs) {
        int defaultElevation = (int) context.getResources().getDimension(R.dimen.neomorph_view_elevation);
        int defaultCornerRadius = (int) context.getResources().getDimension(R.dimen.neomorph_view_corner_radius);

        if (attrs != null) {
            //get attrs array
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeomorphFrameLayout);
            //get all attributes
            SHAPE_TYPE = a.getString(R.styleable.NeomorphFrameLayout_neomorph_view_type);
            if (SHAPE_TYPE == null) {
                SHAPE_TYPE = SHAPE_TYPE_RECTANGLE;
            }

            SHADOW_TYPE = a.getString(R.styleable.NeomorphFrameLayout_neomorph_shadow_type);
            if (SHADOW_TYPE == null) {
                SHADOW_TYPE = SHADOW_TYPE_OUTER;
            }

            ELEVATION = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_elevation, defaultElevation);
            CORNER_RADIUS = a.getDimensionPixelSize(R.styleable.NeomorphFrameLayout_neomorph_corner_radius, defaultCornerRadius);
            BACKGROUND_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_background_color,
                    ContextCompat.getColor(context, R.color.neomorph_background_color));
            SHADOW_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_shadow_color,
                    ContextCompat.getColor(context, R.color.neomorph_shadow_color));
            HIGHLIGHT_COLOR = a.getColor(R.styleable.NeomorphFrameLayout_neomorph_highlight_color,
                    ContextCompat.getColor(context, R.color.neomorph_highlight_color));
            SHADOW_VISIBLE = a.getBoolean(R.styleable.NeomorphFrameLayout_neomorph_shadow_visible, true);
            String layerType = a.getString(R.styleable.NeomorphFrameLayout_neomorph_layer_type);
            if (layerType == null || layerType.equals("1")) {
                LAYER_TYPE = View.LAYER_TYPE_SOFTWARE; //SW by default
            } else LAYER_TYPE = View.LAYER_TYPE_HARDWARE;

            a.recycle();
        } else {
            SHAPE_TYPE = "rectangle";
            ELEVATION = defaultElevation;
            CORNER_RADIUS = defaultCornerRadius;
            BACKGROUND_COLOR = ContextCompat.getColor(context, R.color.neomorph_background_color);
            SHADOW_COLOR = ContextCompat.getColor(context, R.color.neomorph_shadow_color);
            HIGHLIGHT_COLOR = ContextCompat.getColor(context, R.color.neomorph_highlight_color);
            LAYER_TYPE = View.LAYER_TYPE_SOFTWARE;
            SHADOW_VISIBLE = true;
            SHADOW_TYPE = SHADOW_TYPE_OUTER;
        }
    }

    private void initPaints() {
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);

        basePaint.setColor(BACKGROUND_COLOR);
        paintShadow.setColor(BACKGROUND_COLOR);
        paintHighLight.setColor(BACKGROUND_COLOR);

        if (SHADOW_VISIBLE) {
            paintShadow.setShadowLayer(ELEVATION, ELEVATION, ELEVATION, SHADOW_COLOR);
            paintHighLight.setShadowLayer(ELEVATION, -ELEVATION, -ELEVATION, HIGHLIGHT_COLOR);
        }

        basePath = new Path();
        pathHighlight = new Path();
        pathShadow = new Path();

        //TODO: make SHAPE_PADDING dynamic
        SHAPE_PADDING = ELEVATION * 2;

        setWillNotDraw(false);
        setLayerType(LAYER_TYPE, null);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectangle = new RectF(SHAPE_PADDING, SHAPE_PADDING, this.getWidth() - SHAPE_PADDING, this.getHeight() - SHAPE_PADDING);
        resetPath(w, h);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPadding(SHAPE_PADDING, SHAPE_PADDING, SHAPE_PADDING, SHAPE_PADDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (SHADOW_TYPE) {
            case SHADOW_TYPE_INNER:
                canvas.clipPath(basePath);
                break;
            default:
            case SHADOW_TYPE_OUTER:
                break;
        }
        if (SHADOW_VISIBLE) {
            paintShadow.setAlpha(155);
            paintHighLight.setAlpha(155);
        } else {
            paintShadow.setAlpha(0);
            paintHighLight.setAlpha(0);
        }
        canvas.drawPath(basePath, basePaint);
        canvas.drawPath(pathShadow, paintShadow);
        canvas.drawPath(pathHighlight, paintHighLight);
    }

    private void resetPath(int w, int h) {
        basePath.reset();
        pathHighlight.reset();
        pathShadow.reset();

        switch (SHAPE_TYPE) {
            case SHAPE_TYPE_CIRCLE:
                //get max suitable diameter, which is the smallest dimension
                int maxDiameter = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
                int radius = (maxDiameter / 2) - SHAPE_PADDING;
                basePath.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
                pathHighlight.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
                pathShadow.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
                break;
            default:
            case SHAPE_TYPE_RECTANGLE:
                basePath.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                pathHighlight.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                pathShadow.addRoundRect(rectangle, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
                break;
        }

        if (SHADOW_TYPE.equals(SHADOW_TYPE_INNER)) {
            if (!pathHighlight.isInverseFillType()) {
                pathHighlight.toggleInverseFillType();
            }
            if (!pathShadow.isInverseFillType()) {
                pathShadow.toggleInverseFillType();
            }
        }

        basePath.close();
        pathHighlight.close();
        pathShadow.close();
    }

    public void setShadowInner() {
        SHADOW_VISIBLE = true;
        SHADOW_TYPE = SHADOW_TYPE_INNER;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setShadowOuter() {
        SHADOW_VISIBLE = true;
        SHADOW_TYPE = SHADOW_TYPE_OUTER;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void switchShadowType() {
        SHADOW_VISIBLE = true;

        if (SHADOW_TYPE.equals(SHADOW_TYPE_INNER)) {
            SHADOW_TYPE = SHADOW_TYPE_OUTER;
        } else SHADOW_TYPE = SHADOW_TYPE_INNER;

        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }

    public void setShadowNone() {
        SHADOW_VISIBLE = false;
        initPaints();
        resetPath(getWidth(), getHeight());
        invalidate();
    }
}