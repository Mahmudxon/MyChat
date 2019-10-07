package uz.mahmudxon.mychat.fragment

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import uz.mahmudxon.mychat.extentions.BaseFragment
import uz.mahmudxon.mychat.R
import uz.mahmudxon.mychat.adapter.PagerAdapter
import uz.mahmudxon.mychat.fragment.page.CallListFragment
import uz.mahmudxon.mychat.fragment.page.ChatListFragment

class MainFragment : BaseFragment(R.layout.fragment_main) {

   var isDrawerOpen = false

    override fun onCreatedView(senderData: Any?) {
        ic_drawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener
        {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                isDrawerOpen = false
            }

            override fun onDrawerOpened(drawerView: View) {
                isDrawerOpen = true
            }
        })
        setPager()
    }

    fun closeDrawer()
    {
        drawer_layout?.closeDrawer(GravityCompat.START)
    }

    private fun setPager()
    {
        val data = ArrayList<Fragment>()
        data.add(ChatListFragment())
        data.add(CallListFragment())
        pager.adapter = PagerAdapter(requireFragmentManager(), data)
        tablayout.setupWithViewPager(pager)

        tablayout.getTabAt(0)?.setIcon(R.drawable.ic_chat_white)
        tablayout.getTabAt(1)?.setIcon(R.drawable.ic_call_white)
    }
}