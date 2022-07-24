package alex.pakshin.trainingschedual

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.ex_info.*

class DecriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ex_info)

        video.setVideoURI(Uri.parse("https://youtu.be/XUeE5cTzsfo"))
        video.requestFocus()
        video.start()
    }


}