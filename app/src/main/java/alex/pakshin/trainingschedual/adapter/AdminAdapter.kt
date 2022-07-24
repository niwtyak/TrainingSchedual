package alex.pakshin.trainingschedual.adapter

import alex.pakshin.trainingschedual.AdminActivity
import alex.pakshin.trainingschedual.databinding.AdminViewBinding
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.models.Workout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import java.util.*


class AdminAdapter : ListAdapter<TrainingPlans, AdminAdapter.ViewHolder>(DiffCallback) {
    private var date: Date = Calendar.getInstance().time

    inner class ViewHolder(private val binding: AdminViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: TrainingPlans) {
            with(binding) {
                userName.text =
                    queryFirst<Users> { equalTo("id", model.ownerId) }?.name ?: "name"
                val adapter = TrainPlanAdapter()
                userPlan.adapter = adapter
                adapter.setSort(AdminActivity.Sort.byName)
                setPlan(model, adapter)

            }
        }
    }

    fun setData(newDate: Date) {
        date = newDate
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdminViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private object DiffCallback : DiffUtil.ItemCallback<TrainingPlans>() {

        override fun areItemsTheSame(oldItem: TrainingPlans, newItem: TrainingPlans): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: TrainingPlans, newItem: TrainingPlans): Boolean =
            oldItem.id == newItem.id && oldItem.ownerId == newItem.ownerId && oldItem.date == newItem.date
    }

    private fun setPlan(plan: TrainingPlans, adapter: TrainPlanAdapter) {
        val workouts = query<Workout> { equalTo("planId", plan.id) }
        val newW = mutableListOf<Workout>()

        for (w in workouts) {
            if (w.date?.day == date.day && w.date?.month == date.month)
                newW += w
        }

        if (newW.isNotEmpty()) {
            adapter.submitList(newW)
        } else {

            adapter.submitList(emptyList())
        }
    }

}
