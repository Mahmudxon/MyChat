package uz.mahmudxon.mychat.extentions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.graphics.BitmapFactory
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.widget.Adapter
import androidx.fragment.app.FragmentManager
import java.io.IOException
import java.io.InputStream
import uz.mahmudxon.mychat.R


@SuppressLint("ShowToast")
fun BaseFragment.toast(text : String)
{
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

//toast
fun AppCompatActivity.toast(text : String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Activity.toast(text : String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

//inflate adapter view
fun ViewGroup.inflate(@LayoutRes resId : Int) = LayoutInflater.from(context).inflate(resId, this, false)


//Fragments
abstract class BaseFragment(@LayoutRes val resId: Int) : Fragment() {
    internal var senderData: Any? = null

    override fun onCreateView(inflater: LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {
        return inflater.inflate(resId, container, false)
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        onCreatedView(senderData)
    }

    abstract fun onCreatedView(senderData: Any?)

    @SuppressLint("ResourceType")
    fun startFragment(fragment: BaseFragment, senderData: Any? = null) {
        fragment.senderData = senderData
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            ?.replace(R.id.content, fragment)
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.addToBackStack(fragment.hashCode().toString())
            ?.commit()
    }

    @SuppressLint("ResourceType")
    fun addFragment(fragment: BaseFragment, senderData: Any? = null) {
        fragment.senderData = senderData
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            ?.add(R.id.content, fragment)
            ?.addToBackStack(fragment.hashCode().toString())
            ?.commit()
    }

    fun finish() {
        fragmentManager?.popBackStack()
    }
}

fun AppCompatActivity.startFragment(fragment: BaseFragment, senderData: Any? = null) {
    val resId = ViewModelProviders.of(this)[BaseFragmentViewModel::class.java].resId
    fragment.senderData = senderData
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
        .replace(R.id.content, fragment).commit()
}
fun AppCompatActivity.addFragment(fragment: BaseFragment, senderData: Any? = null) {
    val resId = ViewModelProviders.of(this)[BaseFragmentViewModel::class.java].resId
    fragment.senderData = senderData
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
        .add(R.id.content, fragment).addToBackStack(fragment.hashCode().toString()).commit()
}

fun AppCompatActivity.clearFragmentManager()
{
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.initialFragment(@IdRes resId: Int) {
    ViewModelProviders.of(this)[BaseFragmentViewModel::class.java].resId = resId
}

fun BaseFragment.initialFragment(@IdRes resId: Int) {
    ViewModelProviders.of(this)[BaseFragmentViewModel::class.java].resId = resId
}

class BaseFragmentViewModel : ViewModel() {
    internal var resId: Int = R.id.content
}

