package com.guadou.kt_demo.demo.demo18_customview.guide

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.StatusBarUtils


/**
 * 新功能全屏指引View
 */
class GuideViewActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<GuideViewActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_guide_view


    override fun startObserve() {

    }

    override fun init() {

        val mUserGuideView = findViewById<UserGuideView>(R.id.guide_view)
        val mYYJobsLl = findViewById<ViewGroup>(R.id.ll_part_time)
        val mCVBuildLl = findViewById<ViewGroup>(R.id.ll_cv_build)
        val mFreelancerLl = findViewById<ViewGroup>(R.id.ll_freelancer)
        val mFullTimeLl = findViewById<ViewGroup>(R.id.ll_fulltime_jobs)
        val mRewardsLl = findViewById<ViewGroup>(R.id.ll_rewards)
        val mNewsFeedLl = findViewById<ViewGroup>(R.id.ll_news_feed)



        CommUtils.getHandler().postDelayed({

            val partTimeJobLocation = IntArray(2)
            mYYJobsLl.getLocationOnScreen(partTimeJobLocation) //兼职
            partTimeJobLocation[0] = partTimeJobLocation[0] + CommUtils.dip2px(10)
            partTimeJobLocation[1] = partTimeJobLocation[1] - CommUtils.dip2px(25)

            val cvBuildLocation = IntArray(2)
            mCVBuildLl.getLocationOnScreen(cvBuildLocation) //简历
            cvBuildLocation[0] = cvBuildLocation[0] + 0
            cvBuildLocation[1] = cvBuildLocation[1] - CommUtils.dip2px(25)

            val freelancerLocation = IntArray(2)
            mFreelancerLl.getLocationOnScreen(freelancerLocation) //自由工作
            freelancerLocation[0] = freelancerLocation[0] + CommUtils.dip2px(10)
            freelancerLocation[1] = freelancerLocation[1] - CommUtils.dip2px(25)

            val fullTimeLocation = IntArray(2)
            mFullTimeLl.getLocationOnScreen(fullTimeLocation) //全职
            fullTimeLocation[0] = fullTimeLocation[0] + CommUtils.dip2px(10)
            fullTimeLocation[1] = fullTimeLocation[1] - CommUtils.dip2px(30)

            val rewardsLocation = IntArray(2)
            mRewardsLl.getLocationOnScreen(rewardsLocation) //奖励
            rewardsLocation[0] = rewardsLocation[0] + 0
            rewardsLocation[1] = rewardsLocation[1] - CommUtils.dip2px(25)

            val newsFeedLocation = IntArray(2)
            mNewsFeedLl.getLocationOnScreen(newsFeedLocation) //朋友圈
            newsFeedLocation[0] = newsFeedLocation[0] + CommUtils.dip2px(10)
            newsFeedLocation[1] = newsFeedLocation[1] - CommUtils.dip2px(25)

            val infos = listOf(
                GuideInfo(
                    mYYJobsLl, R.drawable.iv_picture_part_time_job, partTimeJobLocation,
                    R.drawable.iv_yy_part_time_job_word, CommUtils.dip2px(30), 0,
                    R.drawable.iv_arrow_yy_part_time_guide, CommUtils.dip2px(30), 0,
                ),
                GuideInfo(
                    mCVBuildLl, R.drawable.iv_picture_cv_builder, cvBuildLocation,
                    R.drawable.iv_cv_builder_word, 0, 0,
                    R.drawable.iv_arrow_cv_builder, -CommUtils.dip2px(20), 0,
                ),
                GuideInfo(
                    mFreelancerLl, R.drawable.iv_picture_freelancer, freelancerLocation,
                    R.drawable.iv_yy_freelancer_word, -CommUtils.dip2px(30), 0,
                    R.drawable.iv_arrow_yy_rewards_guide, CommUtils.dip2px(20), 0,
                ),
                GuideInfo(
                    mFullTimeLl, R.drawable.iv_picture_full_time_jobs, fullTimeLocation,
                    R.drawable.iv_yy_full_time_word, CommUtils.dip2px(30), 0,
                    R.drawable.iv_arrow_yy_full_time_guide, CommUtils.dip2px(30), 0,
                ),
                GuideInfo(
                    mRewardsLl, R.drawable.iv_picture_rewards, rewardsLocation,
                    R.drawable.iv_yy_promotion_word, 0, 0,
                    R.drawable.iv_arrow_yy_promotion_guide, -CommUtils.dip2px(20), 0,
                ),
                GuideInfo(
                    mNewsFeedLl, R.drawable.iv_picture_news_feed, newsFeedLocation,
                    R.drawable.iv_yy_new_feed_word, -CommUtils.dip2px(60), 0,
                    R.drawable.iv_arrow_yy_new_feed_guide, CommUtils.dip2px(30), 0,
                ),
            )


            //设置外部不可取消
            mUserGuideView.setTouchOutsideEffect(false)
            mUserGuideView.setStatusBarHeight(StatusBarUtils.getStatusBarHeight(mActivity))

            mUserGuideView.setupGuideInfo(infos)
            //设置数据源
            mUserGuideView.setShowArrow(true)
        }, 1000)

    }


}