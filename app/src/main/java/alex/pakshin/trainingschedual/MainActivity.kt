package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.MainActivity.RealmUtility.defaultConfig
import alex.pakshin.trainingschedual.models.Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.vicpin.krealmextensions.RealmConfigStore
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.realm.Realm
import io.realm.RealmConfiguration
import java.lang.RuntimeException
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val addEventCode = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email: EditText = findViewById(R.id.etemail)
        val pass: EditText = findViewById(R.id.mypass)

        Realm.init(this)
        Realm.setDefaultConfiguration(defaultConfig)
        RealmConfigStore.init(Users::class.java, defaultConfig)

        val admin = Users(null, "admin", "admin", "admin", true)
        admin.save()
         //Realm.deleteRealm(defaultConfig)

        val button: Button = findViewById(R.id.btnlogin)
        button.setOnClickListener {

            val user = queryFirst<Users> {
                equalTo("email", email.text.toString().trim()).and()
                    .equalTo("password", pass.text.toString().trim())
            }
            if (user != null) {
                if (!user.admin) {
                    val intent = Intent(this, ClientActivity::class.java)
                    val gson = Gson()
                    intent.putExtra("user", gson.toJson(user))
                    startActivity(intent)
                } else {
                    val intent = Intent(this, AdminActivity::class.java)
                    val gson = Gson()
                    intent.putExtra("admin", gson.toJson(user))
                    startActivity(intent)
                }
            } else Toast.makeText(applicationContext, "Невверно введены данные", Toast.LENGTH_SHORT)
                .show()
        }

        val newAcc: TextView = findViewById(R.id.createnewac)
        newAcc.setOnClickListener {
            val intent = Intent(this, SingUpActivity::class.java)
            startActivityForResult(intent, addEventCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == addEventCode && resultCode == RESULT_OK) {
            val gson = Gson()
            val newUser: Users = gson.fromJson(
                data?.getStringExtra("result"),
                Users::class.java
            )
            newUser.save()
        }
    }


    object RealmUtility {
        private const val schemaVNow = 0

        val defaultConfig: RealmConfiguration
            get() = RealmConfiguration.Builder()
                .schemaVersion(schemaVNow.toLong())
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true)
                .build()
    }
}