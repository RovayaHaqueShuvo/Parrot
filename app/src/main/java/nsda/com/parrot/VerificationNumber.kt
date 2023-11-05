package nsda.com.parrot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

            if( binding.enteredNuberEDTXT.text.toString().isEmpty()){
                Toast.makeText(this@VerificationNumber,"Enter Your Number", Toast.LENGTH_SHORT).show()
            }
            else {
            var intent = Intent(this@VerificationNumber, OTPActivity::class.java)
            intent.putExtra("number_passing", binding.enteredNuberEDTXT.text.toString())
            startActivity(intent)
        }}
    }
}