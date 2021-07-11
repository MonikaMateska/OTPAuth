package com.example.otpauth.activity.mainActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.otpauth.R
import com.example.otpauth.activity.secondActivity.SecondActivity


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_first, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    view.findViewById<Button>(R.id.button_first).setOnClickListener {
//      val i = Intent(activity, SecondActivity::class.java)
//      startActivity(i)
//      (activity as Activity?)!!.overridePendingTransition(0, 0)
//    }
  }

}