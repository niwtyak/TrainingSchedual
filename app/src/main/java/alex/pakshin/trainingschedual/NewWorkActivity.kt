package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.models.Workout
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.applandeo.materialcalendarview.CalendarView
import com.google.gson.Gson
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.save
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_new_work.*
import kotlinx.android.synthetic.main.activity_new_work.view.*
import kotlinx.android.synthetic.main.train_plan_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_work)
        Realm.getInstance(MainActivity.RealmUtility.defaultConfig)
        if (intent != null) {
            val gson = Gson()
            val curUser: Users = gson.fromJson(
                intent.getStringExtra("newWorkout"),
                Users::class.java
            )

            timePicker1.setIs24HourView(true)

            var datetime: Date = calendarView2.firstSelectedDate.time

            calendarView2.setOnDayClickListener {
                datetime = it.calendar.time
            }

            applyButton.setOnClickListener {
                datetime.hours = timePicker1.hour
                datetime.minutes = timePicker1.minute
                val plan = queryFirst<TrainingPlans> { equalTo("ownerId",curUser.id)} ?: throw RuntimeException()
                val wk = queryLast<Workout> {equalTo("ownerId",curUser.id).and().equalTo("planId",plan.id)}
                var train = wk?.train?.plus(1) ?: 1
                if (train>3) train=1

                val newEx = query<Exersise> {
                    equalTo("planId", plan.id).and().equalTo("number", train)
                }
                var ct = datetime.time

                for (ex in newEx) {
                    val workout = Workout(null,curUser.id,plan.id,Date(ct),train,ex)
                    ct += ex.sets * (ex.rep * ex.coef!! / 2 * 1000 + 90000)
                    workout.save()
                }

                setResult(RESULT_OK, Intent().putExtra("workout", datetime.toString() ))
                finish()
            }
        }
    }
}