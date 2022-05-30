package androidx.fragment.app

internal class NavContainerFragmentManager : FragmentManager() {

    init {
        addOnBackStackChangedListener {
            if (mBackStack.size > 1)
                throw RuntimeException("NavContainerFragmentManager容器只能装载一个Fragment，专用于Navgation的Fragment")
        }
    }

}