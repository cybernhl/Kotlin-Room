package com.guadou.kt_demo.demo.demo18_customview.viewgroup


import androidx.recyclerview.widget.RecyclerView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.CurtainLayout
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.lxj.easyadapter.ViewHolder

class ViewGroup6Activity : BaseVMActivity<EmptyViewModel>() {

    val list = arrayListOf<String>()

    companion object {
        fun startInstance() {
            commContext().gotoActivity<ViewGroup6Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_viewgroup6

    override fun startObserve() {

    }

    override fun init() {

        val list = listOf<String>(
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620719788-84659PM672_640-640.jpeg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620763778-cgz14U4131_1080-1080.jpeg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620753999-468j1tk4Bf_1080-1080.jpeg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620769824-l6h9D670S6_1080-1080.jpeg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620711480-kn1JTD9AH8_640-640.jpeg",
            "http://dingyue.nosdn.127.net/ZjoJV6hOWeP4oeFGpJmbIIMIcDm32ZcZIlsggBi6nlAyC1537537669583compressflag.jpg",
            "http://n.sinaimg.cn/front/276/w640h436/20190412/ZXpc-hvntnkr0280890.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://c-ssl.duitang.com/uploads/blog/202101/26/20210126234830_7fdac.thumb.1000_0.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "http://imgs.tom.com/info/201803/CONTENTDC8D035E17FE4F09.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "http://pic.dbw.cn/003/005/723/00300572326_45e7318b.jpg",
            "http://dingyue.nosdn.127.net/EthY9boO=37pqvy2eYvaUPvXHOlegkCJKvjSQ3hsGwooZ1545715927464compressflag.jpg",
            "http://piccn.ihuaben.com/pic/community/201905/1558104946500-A34vN06qiS_1020-1020.jpeg",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://pics2.baidu.com/feed/b3fb43166d224f4a08c98ce5c0b6ee569922d1be.jpeg?token=e2b6a6fd24823a13fea736b32be54c57&s=4020941B064342F4AD3D78CA0300B034",
            "https://p6-tt.byteimg.com/origin/pgc-image/3a80dc08d5df45dc876932bbc4e7fab0?from=pc",
            "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1919677147,2434812183&fm=175&app=25&f=JPEG?w=527&h=752&s=519143311742475908F17DDE030010B4",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://c-ssl.duitang.com/uploads/item/202002/17/20200217222726_MuEJe.thumb.1000_0.jpeg",
            "https://c-ssl.duitang.com/uploads/blog/202106/14/20210614144740_56717.thumb.400_0.jpg",
            "http://gd-hbimg.huaban.com/a4641887f9202fd89e741cc8c64669c65eab2b4f887ec-WRzUoR_fw236",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620753999-468j1tk4Bf_1080-1080.jpeg",
            "http://piccn.ihuaben.com/pic/chapter/201912/0616/1575620763778-cgz14U4131_1080-1080.jpeg",
            "https://c-ssl.duitang.com/uploads/item/202003/29/20200329194656_jsnyw.thumb.400_0.jpg",
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3491176941,1104412089&fm=175&app=25&f=JPEG?w=640&h=954&s=C504F0187C33108C67021AD80300A0BD",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c8d34fe37de1717.webp",
            "https://hbimg.huabanimg.com/db6903ae51e52e9218e59290d4a218c7163a25c42a376-KGZgpm_fw236",
            "https://c-ssl.duitang.com/uploads/item/202001/13/20200113233058_qzdbo.thumb.1000_0.jpg"
        )

        val adapter = Viewgroup6Adapter(list)

        val curtainView = findViewById<CurtainLayout>(R.id.curtain_view)

        curtainView.adapter = adapter

    }

}