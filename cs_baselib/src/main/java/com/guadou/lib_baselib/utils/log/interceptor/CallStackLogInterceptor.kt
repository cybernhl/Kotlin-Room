package com.guadou.lib_baselib.utils.log.interceptor

import com.guadou.lib_baselib.utils.log.Chain
import com.guadou.lib_baselib.utils.log.LogInterceptor
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.log.getCallStack

class CallStackLogInterceptor : LogInterceptor {
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
            CallStackLogInterceptor::class.java.name,
            YYLogUtils::class.java.name,
            Chain::class.java.name,
        )
    }

    override fun log(priority: Int, tag: String, log: String, chain: Chain) {
        if (enable()) {
            chain.proceed(priority, tag, TOP_BORDER)

            chain.proceed(priority, tag, "$LEFT_BORDER [Thread] → " + Thread.currentThread().name)

            chain.proceed(priority, tag, MIDDLE_BORDER)

            getCallStack(blackList).take(2).forEach {
                val callStack = StringBuilder()
                    .append(LEFT_BORDER)
                    .append("\t${it}").toString()
                chain.proceed(priority, tag, callStack)
            }

            chain.proceed(priority, tag, MIDDLE_BORDER)

            chain.proceed(priority, tag, "$LEFT_BORDER $log")

            chain.proceed(priority, tag, BOTTOM_BORDER)

        } else {
            chain.proceed(priority, tag, log)
        }

    }

    override fun enable(): Boolean {
        return true
    }
}