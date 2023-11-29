package com.guadou.lib_baselib.ext


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guadou.lib_baselib.base.BaseApplication
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.IDNameEntity
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.utils.interceptor.LoginInterceptorTask
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 *  通用扩展
 */
val Int.ap: Int
    get() = CommUtils.dip2px(this)

fun Int.sp(): Int {
    return CommUtils.dip2px(this)
}

var IDNameEntity.propertyId: String
    get() = this.id
    set(value:String) {
        this.id = value
    }

val IDNameEntity.propertyName :String
       get() = this.name


/**
 * 全局的Context
 */
fun Any.commContext(): Context {
    return CommUtils.getContext()
}

/** dp和px转换 **/
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    return (spValue * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Fragment.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun Fragment.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun Fragment.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}


fun View.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun View.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun View.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Int {
    return itemView.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.sp2px(dpValue: Float): Int {
    return itemView.sp2px(dpValue)
}

fun RecyclerView.ViewHolder.px2sp(pxValue: Float): Int {
    return itemView.px2sp(pxValue)
}

fun ViewModel.px2dp(pxValue: Float): Int {
    return CommUtils.getContext()!!.px2dp(pxValue)
}

fun ViewModel.dp2px(dpValue: Float): Int {
    return CommUtils.getContext()!!.dp2px(dpValue)
}

fun ViewModel.sp2px(dpValue: Float): Int {
    return CommUtils.getContext()!!.sp2px(dpValue)
}

fun ViewModel.px2sp(pxValue: Float): Int {
    return CommUtils.getContext()!!.px2sp(pxValue)
}


/** 动态创建Drawable **/
fun Context.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    val content = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
        setStroke(strokeWidth, strokeColor)
    }
    if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
        return RippleDrawable(ColorStateList.valueOf(rippleColor), content, null)
    }
    return content
}

fun Fragment.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun View.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun RecyclerView.ViewHolder.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return itemView.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}


/** toast相关 **/
fun Any.toast(msg: String?) {
    ToastUtils.makeText(CommUtils.getContext(), msg)
}

fun Any.toast(res: Int) {
    ToastUtils.makeText(CommUtils.getContext(), res)
}

fun Any.toastError(msg: String?) {
    ToastUtils.showFailText(CommUtils.getContext(), msg)
}

fun Any.toastError(res: Int) {
    ToastUtils.showFailText(CommUtils.getContext(), res)
}

fun Any.toastSuccess(msg: String?) {
    ToastUtils.showSuccessText(CommUtils.getContext(), msg)
}

fun Any.toastSuccess(res: Int) {
    ToastUtils.showSuccessText(CommUtils.getContext(), res)
}


/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

//内联函数+标注泛型 = 泛型实例化
inline fun <reified T> String.toBean() = Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)


/** Window相关 **/
fun Context.windowWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun Context.windowHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}

fun Fragment.windowWidth(): Int {
    return context!!.windowWidth()
}

fun Fragment.windowHeight(): Int {
    return context!!.windowHeight()
}

fun View.windowWidth(): Int {
    return context!!.windowWidth()
}

fun View.windowHeight(): Int {
    return context!!.windowHeight()
}

fun RecyclerView.ViewHolder.windowWidth(): Int {
    return itemView.windowWidth()
}

fun RecyclerView.ViewHolder.windowHeight(): Int {
    return itemView.windowHeight()
}


/** 网络相关 **/
/**
 * 当前网络是否有连接
 */
fun Any.isNetworkConnected() = NetWorkUtil.isConnected(CommUtils.getContext())

/**
 * 当前是否是Wifi连接
 */
fun Any.isWifiConnected() = NetWorkUtil.isWifiConnected(CommUtils.getContext())


/**
 * 数组转bundle
 */
fun Array<out Pair<String, Any?>>.toBundle(): Bundle? {
    return Bundle().apply {
        forEach { it ->
            val value = it.second
            when (value) {
                null -> putSerializable(it.first, null as Serializable?)
                is Int -> putInt(it.first, value)
                is Long -> putLong(it.first, value)
                is CharSequence -> putCharSequence(it.first, value)
                is String -> putString(it.first, value)
                is Float -> putFloat(it.first, value)
                is Double -> putDouble(it.first, value)
                is Char -> putChar(it.first, value)
                is Short -> putShort(it.first, value)
                is Boolean -> putBoolean(it.first, value)
                is Serializable -> putSerializable(it.first, value)
                is Parcelable -> putParcelable(it.first, value)

                is IntArray -> putIntArray(it.first, value)
                is LongArray -> putLongArray(it.first, value)
                is FloatArray -> putFloatArray(it.first, value)
                is DoubleArray -> putDoubleArray(it.first, value)
                is CharArray -> putCharArray(it.first, value)
                is ShortArray -> putShortArray(it.first, value)
                is BooleanArray -> putBooleanArray(it.first, value)

                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(
                        it.first,
                        value as Array<CharSequence>
                    )
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(
                        it.first,
                        value as Array<Parcelable>
                    )
                }
            }
        }
    }

}

//data class 对象的深拷贝
fun <T : Any> T.deepCopy(): T {
    //如果不是数据类，直接返回
    if (!this::class.isData) {
        return this
    }

    //拿到构造函数
    return this::class.primaryConstructor!!.let { primaryConstructor ->
        primaryConstructor.parameters.map { parameter ->
            //转换类型

            //最终value=第一个参数类型的对象
            val value = (this::class as KClass<T>).memberProperties.first {
                it.name == parameter.name
            }.get(this)

            //如果当前类(这里的当前类指的是参数对应的类型，比如说这里如果非基本类型时)是数据类
            if ((parameter.type.classifier as? KClass<*>)?.isData == true) {
                parameter to value?.deepCopy()
            } else {
                parameter to value
            }

            //最终返回一个新的映射map,即返回一个属性值重新组合的map，并调用callBy返回指定的对象
        }.toMap().let(primaryConstructor::callBy)
    }
}


/**
 * 主线程运行
 */
fun Any.runOnUIThread(block: () -> Unit) {
    Handler(Looper.getMainLooper()).post { block() }
}


/**
 * 检查是否有网络-直接检查全局内存
 */
fun Any.checkNet(
    block: () -> Unit,
    msg: String = "Network connection error, please check the network connection"
) {

    if (BaseApplication.checkHasNet()) {
        block()
    } else {
        toast(msg)
    }

}


// =======================  倒计时的实现 ↓ =========================

@ExperimentalCoroutinesApi
fun BaseViewModel.countDown(
    time: Int = 5,
    start: (scop: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {

    viewModelScope.launch {

        // 在这个范围内启动的协程会在Lifecycle被销毁的时候自动取消

        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)

        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()

        }.catch {
            //错误
            YYLogUtils.e(it.message ?: "Unkown Error")

        }.collect {
            // 在这里 更新值来显示到UI
            next(it)
        }

    }

}

/**
 * 倒计时的实现
 */
@ExperimentalCoroutinesApi
fun FragmentActivity.countDown(
    time: Int = 5,
    start: (scop: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {

    lifecycleScope.launch {

        // 在这个范围内启动的协程会在Lifecycle被销毁的时候自动取消

        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)

        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()

        }.catch {
            //错误
            YYLogUtils.e(it.message ?: "Unkown Error")

        }.collect {
            // 在这里 更新值来显示到UI
            next(it)
        }

    }
}

@ExperimentalCoroutinesApi
fun Fragment.countDown(
    time: Int = 5,
    start: (scop: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {

    lifecycleScope.launch {

        // 在这个范围内启动的协程会在Lifecycle被销毁的时候自动取消

        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.onStart {
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)

        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            end()

        }.catch {
            //错误
            YYLogUtils.e(it.message ?: "Unkown Error")

        }.collect {
            // 在这里 更新值来显示到UI
            next(it)
        }

    }

}

fun CoroutineScope.countDownFlow(
    time: Int = 60,
    onStart: (suspend () -> Unit)? = null,
    onEach: (suspend (Int) -> Unit)? = null,
    onCancel: (suspend (msg: String?) -> Unit)? = null,
    onCompletion: (suspend () -> Unit)? = null
): Job {
    return (time downTo 0)
        .asFlow()
        .cancellable()
        .flowOn(Dispatchers.Default)
        .onStart {
            onStart?.invoke()
        }
        .onEach {
            onEach?.invoke(it)
            delay(1000L)
        }
        .onCompletion {
            if (it == null) {
                onCompletion?.invoke() // 正常完成
            } else {
                onCancel?.invoke(it.message) // 取消时执行取消回调
            }
        }
        .launchIn(this)

}

/**
 * 登录的校验
 */
fun Any.extLoginCheck(loginBlock: () -> Unit, taskBlock: () -> Unit) {

    object : LoginInterceptorTask() {

        override fun isLogin(): Boolean {
//            return BaseApplication.isUserLogin
            return false
        }

        override fun gotoLogin() {
            loginBlock()
        }

    }.execute {
        taskBlock()
    }
}

/**
 * 刷新登录的状态-通知给任务器
 */
fun Any.extLoginEnd() {
    //不管登录成功还是失败，完成登录通知一下登录拦截器放行
    LoginInterceptorTask.loginEnded()
}