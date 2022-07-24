package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.adapter.TrainPlanAdapter
import alex.pakshin.trainingschedual.databinding.ClientActivityBinding
import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.models.Workout
import alex.pakshin.trainingschedual.viewModel.PlanViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import io.realm.Realm
import kotlinx.android.synthetic.main.client_activity.*
import java.text.SimpleDateFormat
import java.util.*


class ClientActivity : AppCompatActivity() {

    private var date: Date = Calendar.getInstance().time
    private var userId: Long? = 0
    private val viewModel by viewModels<PlanViewModel>()
    private val adapter = TrainPlanAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ClientActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Realm.getInstance(MainActivity.RealmUtility.defaultConfig)

        binding.recyclerView.adapter = adapter

        val newPlanButton: Button = findViewById(R.id.newPlanButton)
        val openCalendarButton: Button = findViewById(R.id.openCalendarButton)
        val newWorkButton: Button = findViewById(R.id.newWorkButton)

        val intent: Intent? = intent


        if (intent != null) {
            val gson = Gson()
            val curUser: Users = gson.fromJson(
                intent.getStringExtra("user"),
                Users::class.java
            )
            userId = curUser.id

            newPlanButton.setOnClickListener {
                startActivityForResult(
                    Intent(
                        this,
                        AddNewPlanActivity::class.java
                    ).putExtra("newPlan", gson.toJson(curUser)),
                    11
                )
            }
            openCalendarButton.setOnClickListener {
                startActivityForResult(Intent(this, CalendarActivity::class.java), 13)
            }

            newWorkButton.setOnClickListener {
                startActivityForResult(
                    Intent(
                        this,
                        NewWorkActivity::class.java
                    ).putExtra("newWorkout", gson.toJson(curUser)), 5
                )
            }

            val trainingPlan = queryFirst<TrainingPlans> { equalTo("ownerId", userId) }
            if (trainingPlan == null) {
                newPlanButton.visibility = View.VISIBLE
                binding.trainingPlanTitle.text = "Тренировочный план не создан"
                newPlanButton.text = "Создать новый тренировочный план"
                openCalendarButton.visibility = View.GONE
                newWorkButton.visibility = View.GONE
            } else {
                setPlan(date)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val newPlanButton: Button = findViewById(R.id.newPlanButton)
        val openCalendarButton: Button = findViewById(R.id.openCalendarButton)

        if (requestCode == 11 && resultCode == RESULT_OK) {
            newPlanButton.visibility = View.VISIBLE
            newPlanButton.text = "Изменить параметры"
            openCalendarButton.visibility = View.VISIBLE
            newWorkButton.visibility = View.VISIBLE
        }

        if (requestCode == 13 && resultCode == RESULT_OK) {
            newPlanButton.visibility = View.VISIBLE
            newPlanButton.text = "Изменить параметры"
            openCalendarButton.visibility = View.VISIBLE
            newWorkButton.visibility = View.VISIBLE
            val newDateString = data?.getStringExtra("date")
            setPlan(Date(newDateString!!))
        }

        if (requestCode == 5 && resultCode == RESULT_OK) {
            val newDateString = data?.getStringExtra("workout")
            setPlan(Date(newDateString!!))
        }

    }


    private fun setPlan(date: Date) {

        val plan =
            queryFirst<TrainingPlans> { equalTo("ownerId", userId) } ?: throw RuntimeException()

        val workouts = query<Workout> { equalTo("planId", plan.id) }
        val newW = mutableListOf<Workout>()

        for (w in workouts){
            if (w.date?.day == date.day && w.date?.month == date.month)
                newW += w
        }

        val sdf = SimpleDateFormat("dd MMM yyyy")

        if (newW.isNotEmpty()) {
            trainingPlanTitle.text = "Тренировки на ${sdf.format(date)}"

            adapter.submitList(newW)
        } else {
            trainingPlanTitle.text = "На ${sdf.format(date)} тренировок нет"
            adapter.submitList(emptyList())
        }
    }
}