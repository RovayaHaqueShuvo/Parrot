package nsda.com.parrot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import nsda.com.parrot.databinding.ActivityParrotInterfaceBinding

class Parrot_Interface : AppCompatActivity() {
    private lateinit var binding: ActivityParrotInterfaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityParrotInterfaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            Firebase.auth.signOut()
            finish()
            startActivity(Intent(this@Parrot_Interface, VerificationNumber::class.java))
        }
    }
}