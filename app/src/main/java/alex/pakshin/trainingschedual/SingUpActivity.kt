package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.models.Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson


class SingUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val name: EditText = findViewById(R.id.editName)
        val email: EditText = findViewById(R.id.editEmail)
        val pass: EditText = findViewById(R.id.editPass)

        val button: Button = findViewById(R.id.buttonAcount)

        button.setOnClickListener {
            val returnIntent = Intent()
            val newUser = Users(
                null,
                name.text.toString().trim(),
                email.text.toString().trim(),
                pass.text.toString().trim()
            )
            val gson = Gson()
            returnIntent.putExtra("result", gson.toJson(newUser))
            setResult(RESULT_OK, returnIntent)
            finish()
        }

    }
}