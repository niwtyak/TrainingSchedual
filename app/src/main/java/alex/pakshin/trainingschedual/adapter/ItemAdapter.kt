package alex.pakshin.trainingschedual.adapter

import alex.pakshin.trainingschedual.AdminActivity
import alex.pakshin.trainingschedual.databinding.AdminViewBinding
import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Workout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vicpin.krealmextensions.query
import java.util.*


class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var date: Date = Calendar.getInstance().time
    private var items: List<String> = emptyList()

    inner class ViewHolder(private val binding: AdminViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: String) {
            with(binding) {
                userName.text = model
                val adapter = TrainPlanAdapter()
                userPlan.adapter = adapter
                adapter.setSort(AdminActivity.Sort.byItem)
                setPlan(model, adapter)
            }
        }
    }

    fun setData(newDate: Date, item: List<String>) {
        date = newDate
        items = item
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdminViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private fun setPlan(item: String, adapter: TrainPlanAdapter) {
        val plans = query<TrainingPlans> { findAll() }
        val newW = mutableListOf<Workout>()
        val items = mutableListOf<String>()
        for (plan in plans) {
            val workouts = query<Workout> { equalTo("planId", plan.id) }
            for (w in workouts) {
                if (w.date?.day == date.day && w.date?.month == date.month && w.ex!!.item == item) {
                    newW += w
                }
            }
            newW.sortBy { it.date }
            adapter.submitList(newW)
        }
    }


override fun getItemCount(): Int = items.size

}