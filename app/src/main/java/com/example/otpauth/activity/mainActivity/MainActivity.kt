package com.example.otpauth.activity.mainActivity

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
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


const val SHARED_PREFS = "sharedPrefs"
const val STORED_ACCOUNTS = "storedAccounts"

class MainActivity : AppCompatActivity() {

  public lateinit var sharedPreferences: SharedPreferences
  public lateinit var gson: Gson

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    var loginMessage  = findViewById<TextView>(R.id.loginMessage)
    var loginButton  = findViewById<Button>(R.id.loginBtn)
    var accountsView = findViewById<LinearLayout>(R.id.accountsView)
    var loginView = findViewById<LinearLayout>(R.id.loginView)

    setSupportActionBar(findViewById(R.id.toolbar))
    setupBiometricLogin(context = this)
    setupQRCodeButton()

    accountsView.visibility = View.GONE
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

  private fun setupQRCodeButton() {
    findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
      val switchActivityIntent = Intent(this, SecondActivity::class.java)
      startActivity(switchActivityIntent)
    }
  }

  fun setupBiometricLogin(context: Context) {
    val biometricManager = BiometricManager.from(context)
    when (biometricManager.canAuthenticate()) {
      BiometricManager.BIOMETRIC_SUCCESS -> {
        loginMessage.setText("You can use your fingerprint sensor to login")
        loginMessage.setTextColor(Color.parseColor("#FaFaFa"))
      }

      BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
        loginMessage.setText("The device does not have fingerprint sensor")
        loginBtn.visibility = View.GONE
      }

      BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
          loginMessage.setText("The biometric sensor is currently unavailable")
          loginBtn.visibility = View.GONE
      }

      BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
        loginMessage.setText("Your device does not have any fingerprint saved, please check your security settings")
        loginBtn.visibility = View.GONE

      }
    }

    var executor = ContextCompat.getMainExecutor(this)
    var biometricPrompt = BiometricPrompt(this, executor,
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int,
                                           errString: CharSequence) {
          super.onAuthenticationError(errorCode, errString)
          Toast.makeText(applicationContext,
            "Authentication error: $errString", Toast.LENGTH_SHORT)
            .show()
        }

        override fun onAuthenticationSucceeded(
          result: BiometricPrompt.AuthenticationResult) {
          super.onAuthenticationSucceeded(result)
          Toast.makeText(applicationContext,
            "Login successful!", Toast.LENGTH_SHORT)
            .show()
          loginView.visibility = View.GONE
          accountsView.visibility = View.VISIBLE
        }

        override fun onAuthenticationFailed() {
          super.onAuthenticationFailed()
          Toast.makeText(applicationContext, "Authentication failed",
            Toast.LENGTH_SHORT)
            .show()
        }
      })

    var promptInfo = BiometricPrompt.PromptInfo.Builder()
      .setTitle("Login")
      .setSubtitle("Use your fingerprint to login")
      .setNegativeButtonText("Cancel")
      .build()

    loginBtn.setOnClickListener(View.OnClickListener {
      biometricPrompt.authenticate(promptInfo)
    }

    )
  }


  private fun readAccounts() {
    var storedAccountsJson = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
    var storedAccounts = gson.fromJson(storedAccountsJson, Accounts::class.java).accounts
  }
}