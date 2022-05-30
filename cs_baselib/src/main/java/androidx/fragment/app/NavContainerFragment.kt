package androidx.fragment.app

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import com.guadou.basiclib.R
import com.guadou.lib_baselib.utils.navigation.FragmentCaches

internal class NavContainerFragment : Fragment() {

    private val _vm: FragmentViewModel by viewModels()

    private val className by lazy {
        requireNotNull(arguments?.getString(REAL_FRAGMENT))
    }

    private val _real: Class<out Fragment> by lazy {
        Class.forName(className) as Class<out Fragment>
    }


    private val mRealFragment: Fragment
        get() {
            return _vm.fragment ?: run {
                val frag = FragmentCaches[className]?.invoke()
                    .also { FragmentCaches.remove(className) }
                    ?: _real.newInstance()
                frag.apply {
                    retainInstance = true
                }
            }.also { _vm.fragment = it }
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mChildFragmentManager.beginTransaction().apply {
            mRealFragment.arguments = arguments
            add(R.id.container, mRealFragment)
            commitNow()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sharedElementEnterTransition = mRealFragment.sharedElementEnterTransition
        sharedElementReturnTransition = mRealFragment.sharedElementReturnTransition

        return FrameLayout(requireContext()).apply {
            addView(inflater.inflate(R.layout.nav_container_fragment_layout, container, false)
                .apply { appendBackground() })
        }
    }


    private fun View.appendBackground() {
        val a: TypedArray =
            requireActivity().theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()
        setBackgroundResource(background)
    }

    internal companion object {
        const val REAL_FRAGMENT = "realFragment"
    }

    data class FragmentViewModel(var fragment: Fragment? = null) : ViewModel()
}
