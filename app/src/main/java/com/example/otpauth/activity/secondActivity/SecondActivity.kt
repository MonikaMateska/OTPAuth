package com.example.otpauth.activity.secondActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.otpauth.R
import com.example.otpauth.activity.mainActivity.MainActivity
import com.example.otpauth.activity.mainActivity.SHARED_PREFS
import com.example.otpauth.activity.mainActivity.STORED_ACCOUNTS
import com.example.otpauth.model.Account
import com.example.otpauth.model.Accounts
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_second.*

private const val CAMERA_REQUEST_CODE = 101

class SecondActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()
        gson = Gson()

        setPermissions()
        codeScanner()
    }

    private fun codeScanner() {

        codeScanner = CodeScanner(this, scanner_view)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val uri = it.text.toUri();
                    if (uri != null) {
                        val secretKey = uri.getQueryParameter("secret")
                        val issuer = uri.getQueryParameter("issuer");
                        val user = uri.pathSegments.last().split(":").last()

                        if (secretKey != null && issuer != null) {
                            val scannedAccount = Account(user, issuer, secretKey)
                            saveScannedAccount(scannedAccount)
                        } else {
                            Toast.makeText(this@SecondActivity, "Please scan a valid QR code!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SecondActivity, "Please scan a valid QR code!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.localizedMessage} ")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun saveScannedAccount(account: Account) {
        var storedAccounts = readStoredAccounts()

        if (storedAccounts != null && storedAccounts?.accounts?.contains(account)!!) {
            Toast.makeText(this, "Account is already stored!", Toast.LENGTH_SHORT).show()
            codeScanner.startPreview()
        } else {
            storedAccounts?.accounts?.add(account)
            val storedAccountsString = gson.toJson(storedAccounts).toString()
            sharedPreferencesEditor.putString(STORED_ACCOUNTS, storedAccountsString)
            sharedPreferencesEditor.apply()
            Toast.makeText(this, "Account stored successfully!", Toast.LENGTH_SHORT).show()
        }

        navigateToFirstActivity()
    }

    private fun readStoredAccounts(): Accounts? {
        val storedAccountsString = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }")
        val storedAccounts = gson.fromJson(storedAccountsString, Accounts::class.java)
        return storedAccounts
    }

    private fun setPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
        CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please grant camera permission in order to scan QR code!", Toast.LENGTH_SHORT).show()
                    navigateToFirstActivity()
                } else {
                    Log.e("Main", "Camera permission granted")
                }
            }
        }
    }

    private fun navigateToFirstActivity() {
        val switchActivityIntent = Intent(this,  MainActivity::class.java)
        startActivity(switchActivityIntent)
    }

    // maybe delete
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

}