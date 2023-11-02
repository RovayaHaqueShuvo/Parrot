package nsda.com.parrot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import nsda.com.parrot.databinding.ActivityVerificationNumberBinding

class VerificationNumber : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVerificationNumberBinding.inflate(layoutInflater)
        var auth: FirebaseAuth?
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser != null) {
            startActivity(Intent(this@VerificationNumber, Parrot_Interface::class.java))
            finish()

        }
        binding.enteredNuberEDTXT.requestFocus()

        binding.GetOTpbtn.setOnClickListener {
            var intent = Intent(this@VerificationNumber, OTPAcitvity::class.java)
            intent.putExtra("number_passing", binding.enteredNuberEDTXT.text.toString())
            startActivity(intent)
        }
    }
}