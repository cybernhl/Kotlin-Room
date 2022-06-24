package com.guadou.lib_baselib.utils.log.interceptor

import com.guadou.lib_baselib.utils.log.ILogInterceptor
import com.guadou.lib_baselib.utils.log.LogInterceptorChain
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 对Log进行装饰，美化打印效果
 * 打印Log之前 封装打印的线程-打印的位置
 * 具体的打印输出由其他拦截器负责
 */
class LogDecorateInterceptor(private val isEnable: Boolean) : ILogInterceptor {

    companion object {

        private const val TOP_LEFT_CORNER = '┏'
        private const val BOTTOM_LEFT_CORNER = '┗'
        private const val MIDDLE_CORNER = '┠'
        private const val LEFT_BORDER = '┃'
        private const val DOUBLE_DIVIDER = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        private const val SINGLE_DIVIDER = "──────────────────────────────────────────────────────"

        private const val TOP_BORDER = TOP_LEFT_CORNER.toString() + DOUBLE_DIVIDER
        private const val BOTTOM_BORDER = BOTTOM_LEFT_CORNER.toString() + DOUBLE_DIVIDER
        private const val MIDDLE_BORDER = MIDDLE_CORNER.toString() + SINGLE_DIVIDER

        private val blackList = listOf(
            LogDecorateInterceptor::class.java.name,
            YYLogUtils::class.java.name,
            LogInterceptorChain::class.java.name,
        )
    }

    override fun log(priority: Int, tag: String, LogMsg: String, chain: LogInterceptorChain) {

        if (isEnable) {
            chain.process(priority, tag, TOP_BORDER)

            chain.process(priority, tag, "$LEFT_BORDER [Thread] → " + Thread.currentThread().name)

            chain.process(priority, tag, MIDDLE_BORDER)

            printStackInfo(priority, tag, chain)

            chain.process(priority, tag, MIDDLE_BORDER)

            chain.process(priority, tag, "$LEFT_BORDER $LogMsg")

            chain.process(priority, tag, BOTTOM_BORDER)

        } else {
            chain.process(priority, tag, LogMsg)
        }

    }

    //获取调用栈信息
    private fun printStackInfo(priority: Int, tag: String, chain: LogInterceptorChain) {
        var str = ""
        var line = 0
        val traces = Thread.currentThread().stackTrace.drop(3)
        for (i in traces.indices) {
            if (line >= 3) return   //这里只打印三行

            val element = traces[i]
            val perTrace = java.lang.StringBuilder(str)
            if (element.isNativeMethod) {
                continue
            }
            val className = element.className
            if (className.startsWith("android.")
                || className.contains("com.android")
                || className.contains("java.lang")
                || className.contains("com.youth.xframe")
                || className in blackList
            ) {
                continue
            }
            perTrace.append(element.className)
                .append('.')
                .append(element.methodName)
                .append("  (")
                .append(element.fileName)
                .append(':')
                .append(element.lineNumber)
                .append(")")
            str += "  "
            line++

            //打印日志
            chain.process(priority, tag, "$LEFT_BORDER $perTrace")
        }
    }

}