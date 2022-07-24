package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.adapter.AdminAdapter
import alex.pakshin.trainingschedual.adapter.ItemAdapter
import alex.pakshin.trainingschedual.adapter.TrainPlanAdapter
import alex.pakshin.trainingschedual.databinding.ActivityAdminBinding
import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.models.Workout
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.vicpin.krealmextensions.query
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_admin.trainingPlanTitle
import kotlinx.android.synthetic.main.client_activity.*
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {

    enum class Sort {
        byName, byTime, byItem
    }


    private var curDate: Date = Calendar.getInstance().time
    private lateinit var  binding :ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.getInstance(MainActivity.RealmUtility.defaultConfig)

        setPlan(curDate,Sort.byName,binding)

        val intent: Intent? = intent
        val openCalendarButton: Button = findViewById(R.id.openCalendarButton)

        openCalendarButton.setOnClickListener {
            startActivityForResult(Intent(this, CalendarActivity::class.java), 3)
        }

        sortButton.setOnClickListener {
            val popupMenu = PopupMenu(this, sortButton)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sortName -> {
                        setPlan(curDate,Sort.byName,binding)
                        true
                    }
                    R.id.sortTime -> {
                        setPlan(curDate, Sort.byTime,binding)
                        true
                    }
                    R.id.sortItem -> {
                        setPlan(curDate, Sort.byItem,binding)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        if (intent != null) {
            val gson = Gson()
            val curUser: Users = gson.fromJson(
                intent.getStringExtra("admin"),
                Users::class.java
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == RESULT_OK) {
            val newDate = data?.getStringExtra("date")
            curDate = Date(newDate!!)
            setPlan(curDate,Sort.byName,binding)
        }
    }


    private fun setPlan(date: Date, sort: Sort = Sort.byName,binding: ActivityAdminBinding) {

        val plans = query<TrainingPlans> { findAll() }

        val sdf = SimpleDateFormat("dd MMM yyyy")
        findViewById<TextView>(R.id.trainingPlanTitle).text =
            "Тренировки на ${sdf.format(date)}"

        when (sort) {
            Sort.byName -> {
                val pl = mutableListOf<TrainingPlans>()
                for (plan in plans) {
                    val workouts = query<Workout> { equalTo("planId", plan.id) }
                    workouts.find { it.date?.day == date.day && it.date?.month == date.month }.let {
                        if (it != null) {
                            pl += plan
                        }
                    }
                }

                val adapter = AdminAdapter()
                binding.adminRecyclerView.adapter = adapter

                if (pl.isNotEmpty()) {
                    trainingPlanTitle.text = "Тренировки на ${sdf.format(date)}"

                    adapter.setData(date)
                    adapter.submitList(pl)
                } else {
                    trainingPlanTitle.text = "На ${sdf.format(date)} тренировок нет"
                    adapter.submitList(emptyList())
                }


            }
            Sort.byItem -> {
                val items = mutableListOf<String>()
                for (plan in plans) {
                    val workouts = query<Workout> { equalTo("planId", plan.id) }
                    for (w in workouts) {
                        if (w.date?.day == date.day && w.date?.month == date.month && !items.contains(
                                w.ex!!.item
                            )
                        )
                            items += w.ex!!.item
                    }

                    val adapter = ItemAdapter()
                    binding.adminRecyclerView.adapter = adapter

                    if (items.isNotEmpty()) {
                        trainingPlanTitle.text = "Тренировки на ${sdf.format(date)}"
                       adapter.setData(date, items)
                    } else {
                        trainingPlanTitle.text = "На ${sdf.format(date)} тренировок нет"
                        adapter.setData(date, emptyList())
                    }


                }
            }
            Sort.byTime -> {
                val newW = mutableListOf<Workout>()
                for (plan in plans) {
                    val workouts = query<Workout> { equalTo("planId", plan.id) }
                    for (w in workouts) {
                        if (w.date?.day == date.day && w.date?.month == date.month)
                            newW += w
                    }
                    newW.sortBy { it.date }

                    val adapter = TrainPlanAdapter()
                    binding.adminRecyclerView.adapter = adapter

                    if (newW.isNotEmpty()) {
                        trainingPlanTitle.text = "Тренировки на ${sdf.format(date)}"
                        adapter.setSort(Sort.byTime)
                        adapter.submitList(newW)
                    } else {
                        trainingPlanTitle.text = "На ${sdf.format(date)} тренировок нет"
                        adapter.submitList(emptyList())
                    }


                }
            }

        }
    }
}