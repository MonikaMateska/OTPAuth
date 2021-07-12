package com.example.otpauth.activity.mainActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otpauth.R
import com.example.otpauth.activity.secondActivity.SecondActivity
import com.example.otpauth.adapter.AccountAdapter
import com.example.otpauth.model.Account
import com.example.otpauth.model.Accounts
import com.example.otpauth.utils.FakeApi
import com.google.gson.Gson
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

  private lateinit var sharedPreferences: SharedPreferences
  private lateinit var gson: Gson
  private lateinit var storedAccounts: List<Accounts>

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    Log.e("First Fragment", "Fragment created")

    sharedPreferences = (activity as MainActivity).sharedPreferences
    gson = (activity as MainActivity).gson

    return inflater.inflate(R.layout.fragment_first, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    var recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    var accountAdapter: AccountAdapter = AccountAdapter(FakeApi.getInstance().accounts)

    // Uncomment this line for real data and comment the above one
    // ! It is not tested
    // var accountAdapter: AccountAdapter = AccountAdapter(readAccounts())

    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)

    recyclerView.adapter = accountAdapter

    Log.e("First Fragment", "Adapter set with data ${accountAdapter.itemCount}")

  }

  private fun readAccounts() : List<Account> {
    var storedAccountsJson = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
    var storedAccounts = gson.fromJson(storedAccountsJson, Accounts::class.java).accounts.toList()
    return storedAccounts
  }

}