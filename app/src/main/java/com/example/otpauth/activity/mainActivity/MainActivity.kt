package com.example.otpauth.activity.mainActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.otpauth.R
import com.example.otpauth.activity.secondActivity.SecondActivity
import com.example.otpauth.model.Accounts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

const val SHARED_PREFS = "sharedPrefs"
const val STORED_ACCOUNTS = "storedAccounts"

class MainActivity : AppCompatActivity() {

  public lateinit var sharedPreferences: SharedPreferences
  public lateinit var gson: Gson

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(findViewById(R.id.toolbar))

    findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
      val switchActivityIntent = Intent(this, SecondActivity::class.java)
      startActivity(switchActivityIntent)
    }
    Log.e("Main Activity", "Main activity created!")
    sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    gson = Gson()
    readAccounts()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun readAccounts() {
    var storedAccountsJson = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
    var storedAccounts = gson.fromJson(storedAccountsJson, Accounts::class.java).accounts
  }
}