package alex.pakshin.trainingschedual

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView
import com.google.gson.Gson

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDayClickListener {
            val datetime = it.calendar.time
            setResult(RESULT_OK, Intent().putExtra("date",datetime.toString()))
            finish()
        }
    }
}