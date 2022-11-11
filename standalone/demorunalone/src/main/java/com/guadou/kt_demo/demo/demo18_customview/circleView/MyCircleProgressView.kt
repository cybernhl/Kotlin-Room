package com.guadou.kt_demo.demo.demo18_customview.circleView

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.font_text_view.TypefaceUtil
import java.text.DecimalFormat

/**
 * 圆形进度条,进度圆环
 * 带阴影 带进度 带动画 带圆角
 * 兼职工作的出勤率进度自定义View
 */
class MyCircleProgressView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //是否开启抗锯齿
    private var antiAlias: Boolean = true

    //圆心位置
    private lateinit var centerPosition: Point

    //半径
    private var raduis: Float? = null

    //声明边界矩形
    private var mRectF: RectF? = null

    //声明背景圆画笔
    private lateinit var mBgCirPaint: Paint //画笔
    private var mBgCirColor: Int? = null //颜色
    private var mBgCirWidth: Float = 15f //圆环背景宽度

    //声明进度圆的画笔
    private lateinit var mCirPaint: Paint //画笔
    private var mCirColor: Int? = null //颜色
    private var mCirWidth: Float = 15f //主圆的宽度

    //绘制的起始角度和滑过角度(默认从顶部开始绘制，绘制360度)
    private var mStartAngle: Float = 270f
    private var mSweepAngle: Float = 360f

    //动画时间（默认一秒）
    private var mAnimTime: Int = 1000

    //属性动画
    private var mAnimator: ValueAnimator? = null

    //动画进度
    private var mAnimPercent: Float = 0f

    //进度值
    private var mValue: String? = null

    //最大值(默认为100)
    private var mMaxValue: Float = 100f

    //绘制数值
    private lateinit var mValuePaint: TextPaint
    private var mValueSize: Float? = null
    private var mValueColor: Int? = null

    //绘制进度的后缀-默认为百分号%
    private var mUnit: CharSequence? = "%"

    //绘制描述
    private var mHint: CharSequence? = null
    private lateinit var mHintPaint: TextPaint
    private var mHintSize: Float? = null
    private var mHintColor: Int? = null

    //颜色渐变色
    private var isGradient: Boolean? = null
    private var mGradientColors: IntArray? = intArrayOf(Color.RED, Color.GRAY, Color.BLUE)
    private var mGradientColor: Int? = null
    private var mSweepGradient: SweepGradient? = null

    //阴影
    private var mShadowColor: Int? = null
    private var mShadowSize: Float? = null
    private var mShadowIsShow: Boolean = false

    //保留的小数位数(默认2位)
    private var mDigit: Int = 2

    //是否需要动画(默认需要动画)
    private var isAnim: Boolean = true

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mAnimPercent = 0f
        centerPosition = Point()//初始化圆心属性
        mRectF = RectF()
        mAnimator = ValueAnimator()//初始化属性动画
        initAttrs(attrs, context)//初始化属性
        initPaint()//初始化画笔
    }


    /**
     * 初始化属性
     */
    private fun initAttrs(attrs: AttributeSet?, context: Context?) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.MyCircleProgressView)

        isAnim = typedArray.getBoolean(R.styleable.MyCircleProgressView_isanim, true)
        mDigit = typedArray.getInt(R.styleable.MyCircleProgressView_digit, 2)
        mBgCirColor = typedArray.getColor(R.styleable.MyCircleProgressView_mBgCirColor, Color.GRAY)
        mBgCirWidth = typedArray.getDimension(R.styleable.MyCircleProgressView_mBgCirWidth, 15f)
        mCirColor = typedArray.getColor(R.styleable.MyCircleProgressView_mCirColor, Color.YELLOW)
        mCirWidth = typedArray.getDimension(R.styleable.MyCircleProgressView_mCirWidth, 15f)
        mAnimTime = typedArray.getInt(R.styleable.MyCircleProgressView_animTime, 1000)
        mValue = typedArray.getString(R.styleable.MyCircleProgressView_value)
        mMaxValue = typedArray.getFloat(R.styleable.MyCircleProgressView_maxvalue, 100f)
        mStartAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_startAngle, 270f)
        mSweepAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_sweepAngle, 360f)
        mValueSize = typedArray.getDimension(R.styleable.MyCircleProgressView_valueSize, 15f)
        mValueColor = typedArray.getColor(R.styleable.MyCircleProgressView_valueColor, Color.BLACK)
        mHint = typedArray.getString(R.styleable.MyCircleProgressView_hint)
        mHintSize = typedArray.getDimension(R.styleable.MyCircleProgressView_hintSize, 15f)
        mHintColor = typedArray.getColor(R.styleable.MyCircleProgressView_hintColor, Color.GRAY)
        mUnit = typedArray.getString(R.styleable.MyCircleProgressView_unit)
        mShadowColor = typedArray.getColor(R.styleable.MyCircleProgressView_shadowColor, Color.BLACK)
        mShadowIsShow = typedArray.getBoolean(R.styleable.MyCircleProgressView_shadowShow, false)
        mShadowSize = typedArray.getFloat(R.styleable.MyCircleProgressView_shadowSize, 8f)
        isGradient = typedArray.getBoolean(R.styleable.MyCircleProgressView_isGradient, false)
        mGradientColor = typedArray.getResourceId(R.styleable.MyCircleProgressView_gradient, 0)
        if (mGradientColor != 0) {
            mGradientColors = resources.getIntArray(mGradientColor!!)
        }

        typedArray.recycle()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        //圆画笔（主圆的画笔设置）
        mCirPaint = Paint()
        mCirPaint.isAntiAlias = antiAlias //是否开启抗锯齿
        mCirPaint.style = Paint.Style.STROKE //画笔样式
        mCirPaint.strokeWidth = mCirWidth //画笔宽度
        mCirPaint.strokeCap = Paint.Cap.ROUND  //笔刷样式（圆角的效果）
        mCirPaint.color = mCirColor!!//画笔颜色

        //背景圆画笔（一般和主圆一样大或者小于主圆的宽度）
        mBgCirPaint = Paint()
        mBgCirPaint.isAntiAlias = antiAlias
        mBgCirPaint.style = Paint.Style.STROKE
        mBgCirPaint.strokeWidth = mBgCirWidth
        mBgCirPaint.strokeCap = Paint.Cap.ROUND
        mBgCirPaint.color = mBgCirColor!!


        //初始化主题文字的字体画笔
        mValuePaint = TextPaint()
        mValuePaint.isAntiAlias = antiAlias  //是否抗锯齿
        mValuePaint.textSize = mValueSize!!  //字体大小
        mValuePaint.color = mValueColor!!  //字体颜色
        mValuePaint.textAlign = Paint.Align.CENTER //从中间向两边绘制，不需要再次计算文字
        mValuePaint.typeface = TypefaceUtil.getSFSemobold(context) //字体加粗

        //初始化提示文本的字体画笔
        mHintPaint = TextPaint()
        mHintPaint.isAntiAlias = antiAlias
        mHintPaint.textSize = mHintSize!!
        mHintPaint.color = mHintColor!!
        mHintPaint.textAlign = Paint.Align.CENTER
        mHintPaint.typeface = TypefaceUtil.getSFRegular(context) //自定义字体
    }

    /**
     * 设置圆形和矩阵的大小,设置圆心位置
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //圆心位置
        centerPosition.x = w / 2
        centerPosition.y = h / 2

        //半径
        val maxCirWidth = Math.max(mCirWidth, mBgCirWidth)
        val minWidth = Math.min(
            w - paddingLeft - paddingRight - 2 * maxCirWidth,
            h - paddingBottom - paddingTop - 2 * maxCirWidth
        )

        raduis = minWidth / 2

        //矩形坐标
        mRectF!!.left = centerPosition.x - raduis!! - maxCirWidth / 2
        mRectF!!.top = centerPosition.y - raduis!! - maxCirWidth / 2
        mRectF!!.right = centerPosition.x + raduis!! + maxCirWidth / 2
        mRectF!!.bottom = centerPosition.y + raduis!! + maxCirWidth / 2

        if (isGradient!!) {
            setupGradientCircle() //设置圆环画笔颜色渐变
        }
    }

    /**
     * 核心方法-绘制文本与圆环
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }


    /**
     * 绘制中心的文本
     */
    private fun drawText(canvas: Canvas) {

        canvas.drawText(
            mValue + mUnit,
            centerPosition.x.toFloat(),
            centerPosition.y.toFloat(),
            mValuePaint
        )

        if (mHint != null || mHint != "") {
            canvas.drawText(
                mHint.toString(),
                centerPosition.x.toFloat(),
                centerPosition.y - mHintPaint.ascent() + 15,   //设置间距
                mHintPaint
            )
        }

    }

    /**
     * 使用渐变色画圆
     */
    private fun setupGradientCircle() {
        mSweepGradient =
            SweepGradient(
                centerPosition.x.toFloat(),
                centerPosition.y.toFloat(),
                mGradientColors!!,
                null
            )
        mCirPaint.shader = mSweepGradient
    }

    /**
     * 画圆（主要的圆）
     */
    private fun drawCircle(canvas: Canvas?) {
        canvas?.save()
        if (mShadowIsShow) {
            mCirPaint.setShadowLayer(mShadowSize!!, 0f, 0f, mShadowColor!!) //设置阴影
        }

        //画背景圆
        canvas?.drawArc(mRectF!!, mStartAngle, mSweepAngle, false, mBgCirPaint)

        //画圆
        canvas?.drawArc(mRectF!!, mStartAngle, mSweepAngle * mAnimPercent, false, mCirPaint)
        canvas?.restore()
    }

    /**
     * 设置当前需要展示的值
     */
    fun setValue(value: String, maxValue: Float): MyCircleProgressView {
        if (isNum(value)) {

            mValue = value
            mMaxValue = maxValue

            //当前的进度和最大的进度，去做动画的绘制
            val start = mAnimPercent
            val end = value.toFloat() / maxValue
            startAnim(start, end, mAnimTime)

        } else {
            mValue = value
        }
        return this
    }

    /**
     * 执行属性动画
     */
    private fun startAnim(start: Float, end: Float, animTime: Int) {
        mAnimator = ValueAnimator.ofFloat(start, end)
        mAnimator?.duration = animTime.toLong()
        mAnimator?.addUpdateListener {
            //得到当前的动画进度并赋值
            mAnimPercent = it.animatedValue as Float

            //根据当前的动画得到当前的值
            mValue = if (isAnim) {
                CircleUtil.roundByScale((mAnimPercent * mMaxValue).toDouble(), mDigit)
            } else {
                CircleUtil.roundByScale(mValue!!.toDouble(), mDigit)
            }

            //不停的重绘当前值-表现出动画的效果
            postInvalidate()
        }
        mAnimator?.start()
    }

    /**
     * 设置动画时长
     * */
    fun setAnimTime(animTime: Int): MyCircleProgressView {
        this.mAnimTime = animTime
        invalidate()
        return this
    }


    /**
     * 是否渐变色
     * */
    fun setIsGradient(isGradient: Boolean): MyCircleProgressView {
        this.isGradient = isGradient
        invalidate()
        return this
    }

    /**
     * 设置渐变色
     * */
    fun setGradientColors(gradientColors: IntArray): MyCircleProgressView {
        mGradientColors = gradientColors
        setupGradientCircle()
        return this
    }

    /**
     * 是否显示阴影
     * */
    fun setShadowEnable(enable: Boolean): MyCircleProgressView {
        mShadowIsShow = enable
        invalidate()
        return this
    }

    /**
     * 设置小数位数
     * */
    fun setDigit(digit: Int): MyCircleProgressView {
        mDigit = digit
        invalidate()
        return this
    }

    //判断当前的值是否是数字类型
    private fun isNum(str: String): Boolean {
        try {
            val toDouble = str.toDouble()
        } catch (e: Exception) {
            return false
        }
        return true
    }


    /**
     * 内部工具类
     */
    private class CircleUtil {

        companion object {
            /**
             * 将double格式化为指定小数位的String，不足小数位用0补全
             *
             * @param v     需要格式化的数字
             * @param scale 小数点后保留几位
             * @return
             */
            fun roundByScale(v: Double, scale: Int): String {
                if (scale < 0) {
                    throw IllegalArgumentException("参数错误，必须设置大于0的数字")
                }
                if (scale == 0) {
                    return DecimalFormat("0").format(v)
                }
                var formatStr = "0."

                for (i in 0 until scale) {
                    formatStr += "0"
                }
                return DecimalFormat(formatStr).format(v);
            }

            fun dip2px(context: Context, dpValue: Float): Int {
                val scale = context.resources.displayMetrics.density
                return (dpValue * scale + 0.5f).toInt()
            }

            fun dp2px(context: Context, dpValue: Float): Int {
                return dip2px(context, dpValue)
            }

            fun px2dip(context: Context, pxValue: Float): Int {
                val scale = context.resources.displayMetrics.density
                return (pxValue / scale + 0.5f).toInt()
            }

            fun px2sp(context: Context, pxValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (pxValue / fontScale + 0.5f).toInt()
            }

            fun sp2px(context: Context, spValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (spValue * fontScale + 0.5f).toInt()
            }

        }
    }

}