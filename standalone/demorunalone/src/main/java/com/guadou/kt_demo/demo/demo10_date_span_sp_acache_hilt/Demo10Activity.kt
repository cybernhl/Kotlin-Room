package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import android.content.Intent
import android.view.View
import com.google.gson.GsonBuilder
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo10Binding
import com.guadou.lib_baselib.base.activity.BaseVDBLoadingActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.cache.ACache
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.interceptor.ArrayDefailtAdapter
import com.guadou.lib_baselib.utils.interceptor.IntDefaut0Adapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 吐司 弹窗 banner
 */
@AndroidEntryPoint  //这里用到了自定义的注入 需要加注解
class Demo10Activity : BaseVDBLoadingActivity<EmptyViewModel, ActivityDemo10Binding>() {

    @Inject
    lateinit var userServer: UserServer

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo10Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo10, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {
        initData()
    }

    private fun initData() {
        showStateLoading()

        CommUtils.getHandler().postDelayed({
            showStateSuccess()
        }, 1500)
    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun putACache(view: View) {
            ACache.get().put("demo1", "test")
        }

        fun putSP() {
            SP().getString("a", "default")
            SP().getBoolean("b", false)

            SP().putString("abc", "test demo")

            SP().edit {
                putString("a", "1")
                putBoolean("b", true)
            }

            SP().remove("a")

            SP().clear()
        }

        fun navSpanActivity() {
            DemoSpanActivity.startInstance()
        }

        fun formatString() {
            //判断字符串
//            val isEmpty = "".checkEmpty()
//            toast("isEmpty:" + isEmpty)

            //判断集合
//            val isEmpty = listOf<String>().checkEmpty()
//            toast("isEmpty:" + isEmpty)

//            val list = arrayListOf("123")
//            val isEmpty = list.checkEmpty()
//            toast("isEmpty:" + isEmpty)


            //数组转集合，集合转数组
//            val str = "a,b,c,d,e,f,g"
//            val list = str.toCommaList()
//            toast("list:" + list.toString())

//            val list = listOf("a", "b", "c", "d", "e", "f", "g", "h")
//            val str = list.toCommaStr()
//            toast("str:" + str)

            //小数点的格式化
            toast("2312109.65473".formatMoney())
        }

        fun formatDate(view: View) {
            //时间戳转换为格式化对象
//            val dateFormat = 1595930618000.formatDateString()
//            toast(dateFormat)

//            val dateFormat = 1595930618.formatDateString("EE, MMM dd yyyy")
//            toast(dateFormat)

            //指定格式字符串转换为时间戳
            val dateMilles = "2020 09 20".toDateMills("yyyy MM dd")
            toast(dateMilles.toString())
        }

        val printHilt: () -> Unit = {
            YYLogUtils.w("server:" + userServer.toString() + "Dao:"+userServer.getDaoContent())
            userServer.testUser()
        }

        fun printGson() {
            val jsonStr = """{
            "name":"Newki",
            "age":"18",
            "gender":"",
            "languages":{}
            }""".trimIndent()

            //加入容错处理的Gson 正常使用
            val newUser = GsonBuilder()
                .registerTypeAdapter(Int::class.java, IntDefaut0Adapter())
                .registerTypeAdapter(List::class.java, ArrayDefailtAdapter())
                .create()
                .fromJson(jsonStr, UserBean::class.java)

            //使用KGson  报错NumberFormatException
//            val newUser = BaseApplication.mGson.fromJson<UserBean>(jsonStr)

            //使用默认Gson  报错NumberFormatException
//            val newUser = BaseApplication.mGson.fromJson(jsonStr,UserBean::class.java)

            toast(newUser.toString())
        }

        fun testBackResule() {
            setResult(-1, Intent().putExtra("text", "测试返回的数据"))
            finish()
        }

        //EditText的文字监听
        val onEditTextChangeListener: (String) -> Unit = {
            toast(it)
        }

    }
}