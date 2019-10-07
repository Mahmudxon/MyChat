package uz.mahmudxon.mychat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.dynamic.SupportFragmentWrapper

class PagerAdapter(fm : FragmentManager, var data : ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = data[position]

    override fun getCount() = data.size
}