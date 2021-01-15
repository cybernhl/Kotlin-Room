package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import android.content.Intent
import androidx.activity.viewModels
import com.google.gson.GsonBuilder
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.cache.ACache
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.interceptor.ArrayDefailtAdapter
import com.guadou.lib_baselib.utils.interceptor.IntDefaut0Adapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo10.*
import javax.inject.Inject


/**
 * 吐司 弹窗 banner
 */
@AndroidEntryPoint  //这里用到了自定义的注入 需要加注解
class Demo10Activity : BaseActivity<EmptyViewModel>() {

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

    override fun inflateLayoutById(): Int = R.layout.activity_demo10

    override fun startObserve() {

    }

    override fun init() {

        initLitener()
    }

    private fun initLitener() {

        //Acache
        btn_1.click {
            //还是和原来一样的用法
            ACache.get().put("demo1", "test")
        }


        //SP用法全部在这里
        btn_2.click {

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


        //时间格式化
        btn_3.click {
            //时间戳转换为格式化对象
//            val dateFormat = 1595930618000.formatDateString()
//            toast(dateFormat)

//            val dateFormat = 1595930618.formatDateString("EE, MMM dd yyyy")
//            toast(dateFormat)

            //指定格式字符串转换为时间戳
            val dateMilles = "2020 09 20".toDateMills("yyyy MM dd")
            toast(dateMilles.toString())
        }


        //富文本的页面展示
        btn_4.click {
            DemoSpanActivity.startInstance()
        }

        //String的功能展示
        btn_5.click {
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

        //Hilt的注入  -- 上面成员变量注入了
        btn_6.click {
            YYLogUtils.w("server:" + userServer.toString())
            userServer.testUser()
        }


        btn_7.click {
            val jsonStr = """
        {
            "name":"Newki",
            "age":"18",
            "gender":"",
            "languages":{}
        }
    """.trimIndent()

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

            YYLogUtils.e("对象：" + newUser.toString())
        }
    }

}