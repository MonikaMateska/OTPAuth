package com.example.otpauth.activity.mainActivity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otpauth.R
import com.example.otpauth.adapter.AccountAdapter
import com.example.otpauth.model.Account
import com.example.otpauth.model.Accounts
import com.example.otpauth.utils.FakeApi
import com.google.gson.Gson
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import kotlinx.android.synthetic.main.row.*
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import java.util.*
import java.util.concurrent.TimeUnit

class FirstFragment : Fragment() {

  private lateinit var sharedPreferences: SharedPreferences
  private lateinit var gson: Gson
  private lateinit var storedAccounts: MutableList<Account>
  private lateinit var otpConfig: TimeBasedOneTimePasswordConfig
  private lateinit var recyclerView: RecyclerView
  private lateinit var accountAdapter: AccountAdapter
  private lateinit var timer: CountDownTimer
  private var timerLengthSeconds: Long = 15
  private var secondsRemaining: Long = 0

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    sharedPreferences = (activity as MainActivity).sharedPreferences
    gson = (activity as MainActivity).gson
    otpConfig = TimeBasedOneTimePasswordConfig(codeDigits = 6,
      hmacAlgorithm = HmacAlgorithm.SHA1,
      timeStep = 30,
      timeStepUnit = TimeUnit.SECONDS)
    // Commented for development process only.
//    storedAccounts = readAccounts()
    // Added for development process only.
    storedAccounts = FakeApi.getInstance().accounts

    Timer().schedule(object: TimerTask() {
      override fun run() {
        generateOTP()
      }
    }, 0, 15000)

    Timer().schedule(object: TimerTask() {
      override fun run() {
        updateTimer()
      }
    }, 0, 1000)



    return inflater.inflate(R.layout.fragment_first, container, false)
  }

  private fun generateOTP() {
    for (index in storedAccounts.indices) {
      var account = storedAccounts[index]
      val accountOTP = TimeBasedOneTimePasswordGenerator(account.secretKey.toByteArray(), otpConfig)
      account.setOTP(accountOTP.generate(Date()))
      account.timer = 15
      storedAccounts[index] = account
    }

    Thread(Runnable {
      this.activity?.runOnUiThread(java.lang.Runnable {
        Log.e("OTP", "OTP updated")
        accountAdapter.notifyDataSetChanged()
      })
    }).start()
  }

  private fun updateTimer() {
    for (index in storedAccounts.indices) {
      var account = storedAccounts[index]
      account.timer--
      storedAccounts[index] = account
    }

    Thread(Runnable {
      this.activity?.runOnUiThread(java.lang.Runnable {
        Log.e("OTP", "Timer updated")
        accountAdapter.notifyDataSetChanged()
      })
    }).start()
  }



  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    recyclerView = view.findViewById(R.id.recyclerView)

    // Commented for development process only.
    accountAdapter = AccountAdapter(storedAccounts, sharedPreferences)

    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
    recyclerView.adapter = accountAdapter

    Log.e("First Fragment", "Adapter set with data ${accountAdapter.itemCount}")
  }



  override fun onResume() {
    super.onResume()
    var lastAccountsState = readAccounts()
    if (lastAccountsState.size > storedAccounts.size) {
      storedAccounts.clear()
      storedAccounts.addAll(lastAccountsState)
      generateOTP()
    }
  }

  private fun readAccounts() : MutableList<Account> {
    var storedAccountsJson = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
    var storedAccounts = gson.fromJson(storedAccountsJson, Accounts::class.java).accounts.toMutableList()
    return storedAccounts
  }

}