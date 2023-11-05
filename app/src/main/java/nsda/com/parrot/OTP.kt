package nsda.com.parrot

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import nsda.com.parrot.databinding.ActivityOtpBinding
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var verificationId: String? = null
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and ProgressDialog
        auth = FirebaseAuth.getInstance()
        dialog = ProgressDialog(this@OTPActivity)
        dialog!!.setMessage("Sending OTP...")
        dialog!!.setCancelable(true)
        dialog!!.show()

        // Retrieve phone number from the intent
        val phoneNumber = intent.getStringExtra("number_passing")

        // Update the UI with the phone number
        binding.VerificationNumberTXT.text = "Verifying $phoneNumber"

        // Set up PhoneAuthOptions
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setActivity(this@OTPActivity)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle verification failure (e.g., show an error message)
                    Toast.makeText(this@OTPActivity, "Verification failed", Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    dialog!!.dismiss()
                    this@OTPActivity.verificationId = verificationId

                    // Show the keyboard for OTP input
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    binding.otpNum.requestFocus()
                }
            }).build()

        // Initiate phone number verification
        PhoneAuthProvider.verifyPhoneNumber(options)

        // Set an OTP completion listener
        binding.otpNum.setOtpCompletionListener {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, it)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@OTPActivity, Parrot_Interface::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@OTPActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success, you can handle it here
                    val user = task.result?.user
                } else {
                    // Sign-in failed, handle it here
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // Handle this case
                    }
                }
            }
    }
}
