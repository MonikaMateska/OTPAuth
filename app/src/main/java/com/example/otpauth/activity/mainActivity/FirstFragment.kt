package com.example.otpauth.activity.mainActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.otpauth.R
import com.example.otpauth.activity.secondActivity.SecondActivity
import com.example.otpauth.model.Accounts
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

  private lateinit var sharedPreferences: SharedPreferences
  protected lateinit var gson: Gson

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    sharedPreferences = (activity as MainActivity).sharedPreferences
    gson = (activity as MainActivity).gson

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
  private fun readAccounts() {
    var storedAccountsJson =  sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
    var storedAccounts = gson.fromJson(storedAccountsJson, Accounts::class.java).accounts

  }
}