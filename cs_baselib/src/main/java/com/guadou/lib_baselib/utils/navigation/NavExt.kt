package com.guadou.lib_baselib.utils.navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlin.reflect.KClass

/**
 * 扩展方法合集
 */

//NavHost进行fork,以便扩展方法到自己的NavHost上
class MyNavHost(
    @PublishedApi internal val context: Context, navHost: NavHost
) : NavHost by navHost

//获取到父Activity对象
@PublishedApi
internal fun MyNavHost.requireActivity(): FragmentActivity = context as FragmentActivity

//重点方法 根据传入的Fragment-Class 创建导航图，并绑定自己自定义的FragmentNavigator
fun NavHostFragment.loadRoot(root: KClass<out Fragment>) {
    val context = activity ?: return

    navController.apply {
        navigatorProvider.addNavigator(
            MyFragmentNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = root.hashCode()
        graph = createGraph(startDestination = startDestId) {

            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[MyFragmentNavigator::class],
                    startDestId,
                    root
                ).apply {
                    label = "home"
                })

        }
//            .also { graph ->
        //for NavController #mBackStackToRestore
//            val vm = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
//            vm.nodes.valueIterator().forEach {
//                it.removeFromParent()
//                graph += it
//            }
//        }

    }
}

//通过Fragment的构造对象来实现loadRoot
inline fun <reified T : Fragment> NavHostFragment.loadRoot(
    noinline returnFragmentBlock: () -> T
) {
    val clazz = T::class
    FragmentCaches[clazz.qualifiedName!!] = returnFragmentBlock
    loadRoot(T::class)
}

//重点方法
//主要执行次方法，在push中手动注册目的地并绑定路由图 并通过navigate的方法手动跳转到目的地
//这种方法只能通过args传递参数
fun MyNavHost.push(
    clazz: KClass<out Fragment>,
    arguments: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit = {}
) = with(navController) {
    val node = putFragment(/*requireActivity(),*/ clazz)
    navigate(
        node.id, arguments,
        convertNavOptions(clazz, NavOptions().apply(optionsBuilder)),
        extras
    )
}

//通过factory的方式实现Fragment
//这种方式可以通过构造传递参数
inline fun <reified T : Fragment> MyNavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline returnFragmentBlock: () -> T
) {
    val clazz = T::class
    FragmentCaches[clazz.qualifiedName!!] = returnFragmentBlock
    push(clazz, optionsBuilder = optionsBuilder)
}

//存入Fragment到导航图
@PublishedApi
internal fun NavController.putFragment(
//    activity: FragmentActivity,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
            navigatorProvider[MyFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
//            getRouteUri(clazz)?.let {
//                deepLink {
//                    uriPattern = it
//                }
//            }
        }).build()
        graph.plusAssign(destination)
//        activity.saveToViewModel(destination)
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}

//Fragment的栈返回
fun MyNavHost.pop() {
    navController.popBackStack()
}

//返回到指定栈
fun NavHost.popTo(clazz: KClass<out Fragment>, include: Boolean = false) {
    YYLogUtils.w("返回到指定栈: include-$include")
    navController.popBackStack(clazz.hashCode(), include)
}

//创建Fragmetn中使用navigator的实例
val Fragment.navigator
    get() = MyNavHost(requireContext()) {
        val clazz = this::class
        requireParentFragment().findNavController().apply {
            putFragment(/*requireActivity(),*/ clazz) //make sure the fragment in back stack
        }
    }

val View.navigator
    get() = MyNavHost(context) { findNavController() }


//让Activity直接finish
fun Fragment.finish() {
    requireActivity().finish()
}