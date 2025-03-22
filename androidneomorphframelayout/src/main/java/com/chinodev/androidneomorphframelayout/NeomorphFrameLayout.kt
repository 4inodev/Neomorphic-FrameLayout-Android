package com.chinodev.androidneomorphframelayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

@Suppress("PrivatePropertyName")
class NeomorphFrameLayout(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int): FrameLayout(context, attrs, defStyleAttr)
{

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    //constants
    private val SHAPE_TYPE_RECTANGLE = "1"
    private val SHAPE_TYPE_CIRCLE = "2"
    private val SHADOW_TYPE_OUTER = "1"
    private val SHADOW_TYPE_INNER = "2"

    //attributes
    private var SHAPE_TYPE = SHAPE_TYPE_RECTANGLE
    private var SHADOW_TYPE = SHADOW_TYPE_OUTER
    private var CORNER_RADIUS = 0
    private var ELEVATION = 0
    private var HIGHLIGHT_COLOR = 0
    private var SHADOW_COLOR = 0
    private var BACKGROUND_COLOR = 0
    private var LAYER_TYPE = LAYER_TYPE_SOFTWARE
    private var SHADOW_VISIBLE = true

    //global variables
    private var SHAPE_PADDING = 0

    //global objects
    private lateinit var basePaint: Paint
    private lateinit var paintShadow: Paint
    private lateinit var paintHighLight: Paint
    private lateinit var basePath: Path
    private lateinit var pathShadow: Path
    private lateinit var pathHighlight: Path
    private var rectangle: RectF

    init {
        getAttrs(context, attrs)
        initPaints()

        rectangle = RectF(
            SHAPE_PADDING.toFloat(),
            SHAPE_PADDING.toFloat(),
            (this.width - SHAPE_PADDING).toFloat(),
            (this.height - SHAPE_PADDING).toFloat()
        )
    }

    private fun getAttrs(context: Context, attrs: AttributeSet?) {
        val defaultElevation =
            context.resources.getDimension(R.dimen.neomorph_view_elevation).toInt()
        val defaultCornerRadius =
            context.resources.getDimension(R.dimen.neomorph_view_corner_radius).toInt()

        attrs?.let {

            context.withStyledAttributes(attrs, R.styleable.NeomorphFrameLayout) {
                //get all attributes
                SHAPE_TYPE = getString(R.styleable.NeomorphFrameLayout_neomorph_view_type) ?: SHAPE_TYPE_RECTANGLE

                SHADOW_TYPE = getString(R.styleable.NeomorphFrameLayout_neomorph_shadow_type) ?: SHADOW_TYPE_OUTER

                ELEVATION = getDimensionPixelSize(
                    R.styleable.NeomorphFrameLayout_neomorph_elevation,
                    defaultElevation
                )
                CORNER_RADIUS = getDimensionPixelSize(
                    R.styleable.NeomorphFrameLayout_neomorph_corner_radius,
                    defaultCornerRadius
                )
                BACKGROUND_COLOR = getColor(
                    R.styleable.NeomorphFrameLayout_neomorph_background_color,
                    ContextCompat.getColor(context, R.color.neomorph_background_color)
                )
                SHADOW_COLOR = getColor(
                    R.styleable.NeomorphFrameLayout_neomorph_shadow_color,
                    ContextCompat.getColor(context, R.color.neomorph_shadow_color)
                )
                HIGHLIGHT_COLOR = getColor(
                    R.styleable.NeomorphFrameLayout_neomorph_highlight_color,
                    ContextCompat.getColor(context, R.color.neomorph_highlight_color)
                )
                SHADOW_VISIBLE =
                    getBoolean(R.styleable.NeomorphFrameLayout_neomorph_shadow_visible, true)
                val layerType = getString(R.styleable.NeomorphFrameLayout_neomorph_layer_type)
                LAYER_TYPE = if (layerType == null || layerType == "1") {
                    LAYER_TYPE_SOFTWARE //SW by default
                } else LAYER_TYPE_HARDWARE

            }
        } ?: run {

            ELEVATION = defaultElevation
            CORNER_RADIUS = defaultCornerRadius
            BACKGROUND_COLOR = ContextCompat.getColor(context, R.color.neomorph_background_color)
            SHADOW_COLOR = ContextCompat.getColor(context, R.color.neomorph_shadow_color)
            HIGHLIGHT_COLOR = ContextCompat.getColor(context, R.color.neomorph_highlight_color)

        }

    }

    private fun initPaints() {

        basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        paintShadow = Paint(Paint.ANTI_ALIAS_FLAG)
        paintHighLight = Paint(Paint.ANTI_ALIAS_FLAG)

        basePaint.color = BACKGROUND_COLOR
        paintShadow.color = BACKGROUND_COLOR
        paintHighLight.color = BACKGROUND_COLOR

        if (SHADOW_VISIBLE) {
            paintShadow.setShadowLayer(
                ELEVATION.toFloat(),
                ELEVATION.toFloat(),
                ELEVATION.toFloat(),
                SHADOW_COLOR
            )
            paintHighLight.setShadowLayer(
                ELEVATION.toFloat(),
                -ELEVATION.toFloat(),
                -ELEVATION.toFloat(),
                HIGHLIGHT_COLOR
            )
        }

        basePath = Path()
        pathHighlight = Path()
        pathShadow = Path()

        //TODO: make SHAPE_PADDING dynamic
        SHAPE_PADDING = ELEVATION * 2

        setWillNotDraw(false)
        setLayerType(LAYER_TYPE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectangle = RectF(
            SHAPE_PADDING.toFloat(),
            SHAPE_PADDING.toFloat(),
            (this.width - SHAPE_PADDING).toFloat(),
            (this.height - SHAPE_PADDING).toFloat()
        )
        resetPath(w, h)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPadding(SHAPE_PADDING, SHAPE_PADDING, SHAPE_PADDING, SHAPE_PADDING)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (SHADOW_TYPE) {
            SHADOW_TYPE_INNER -> canvas.clipPath(basePath)
            SHADOW_TYPE_OUTER -> {}
        }
        if (SHADOW_VISIBLE) {
            paintShadow.alpha = 155
            paintHighLight.alpha = 155
        } else {
            paintShadow.alpha = 0
            paintHighLight.alpha = 0
        }
        canvas.apply {
            drawPath(basePath, basePaint)
            drawPath(pathShadow, paintShadow)
            drawPath(pathHighlight, paintHighLight)
        }
    }

    private fun resetPath(w: Int, h: Int) {
        basePath.reset()
        pathHighlight.reset()
        pathShadow.reset()

        when (SHAPE_TYPE) {
            SHAPE_TYPE_CIRCLE -> {
                //get max suitable diameter, which is the smallest dimension
                val maxDiameter = if (this.width < this.height) this.width else this.height
                val radius = (maxDiameter / 2) - SHAPE_PADDING
                basePath.addCircle(
                    (w / 2).toFloat(),
                    (h / 2).toFloat(),
                    radius.toFloat(),
                    Path.Direction.CW
                )
                pathHighlight.addCircle(
                    (w / 2).toFloat(),
                    (h / 2).toFloat(),
                    radius.toFloat(),
                    Path.Direction.CW
                )
                pathShadow.addCircle(
                    (w / 2).toFloat(),
                    (h / 2).toFloat(),
                    radius.toFloat(),
                    Path.Direction.CW
                )
            }

            SHAPE_TYPE_RECTANGLE -> {
                basePath.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
                pathHighlight.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
                pathShadow.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
            }

            else -> {
                basePath.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
                pathHighlight.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
                pathShadow.addRoundRect(
                    rectangle,
                    CORNER_RADIUS.toFloat(),
                    CORNER_RADIUS.toFloat(),
                    Path.Direction.CW
                )
            }
        }

        if (SHADOW_TYPE == SHADOW_TYPE_INNER) {
            if (!pathHighlight.isInverseFillType) {
                pathHighlight.toggleInverseFillType()
            }
            if (!pathShadow.isInverseFillType) {
                pathShadow.toggleInverseFillType()
            }
        }

        basePath.close()
        pathHighlight.close()
        pathShadow.close()
    }

    fun setShadowInner() = setShadow(true)
    fun setShadowOuter() = setShadow(false)
    fun switchShadowType() = setShadow(SHADOW_TYPE != SHADOW_TYPE_INNER) //switch
    fun setShadowNone() = setShadow(false, isNone = true)

    private fun setShadow(isInner: Boolean, isNone: Boolean = false){
        SHADOW_VISIBLE = if (isNone){
            false
        }else{
            SHADOW_TYPE = if (isInner) SHADOW_TYPE_INNER else SHADOW_TYPE_OUTER
            true
        }
        updateUI()
    }

    fun neomorphBackgroundColor(@ColorInt color: Int) {
        BACKGROUND_COLOR = color
        updateUI()
    }

    fun neomorphShadowColor(@ColorInt color: Int) {
        SHADOW_COLOR = color
        updateUI()
    }

    fun neomorphHighlightColor(@ColorInt color: Int) {
        HIGHLIGHT_COLOR = color
        updateUI()
    }

    fun setViewRectangular(){
        SHAPE_TYPE = SHAPE_TYPE_RECTANGLE
        updateUI()
    }

    fun setViewCircular(){
        SHAPE_TYPE = SHAPE_TYPE_CIRCLE
        updateUI()
    }

    fun neomorphElevation(elevation: Int){
        ELEVATION = elevation
        updateUI()
    }

    fun neomorphCornerRadius(cornerRadius: Int){
        CORNER_RADIUS = cornerRadius
        updateUI()
    }

    private fun updateUI(){
        initPaints()
        resetPath(width, height)
        invalidate()
    }


}