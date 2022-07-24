package alex.pakshin.trainingschedual.adapter

import alex.pakshin.trainingschedual.AdminActivity
import alex.pakshin.trainingschedual.databinding.TrainPlanViewBinding
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.models.Workout
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vicpin.krealmextensions.queryFirst
import java.text.SimpleDateFormat

class TrainPlanAdapter : ListAdapter<Workout, TrainPlanAdapter.ViewHolder>(DiffCallback) {
    private var sort: AdminActivity.Sort = AdminActivity.Sort.byName

    fun setSort(sort: AdminActivity.Sort){
        this.sort=sort
    }

    inner class ViewHolder(private val binding: TrainPlanViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Workout) {
            with(binding) {
                when(sort){
                    AdminActivity.Sort.byName->{
                        userName.visibility= View.GONE
                        exTitle.text = model.ex!!.title
                        itemName.text = model.ex!!.item
                        rep.text = "${model.ex!!.rep - 3}-${model.ex!!.rep}x${model.ex!!.sets}"
                        val sdf = SimpleDateFormat("HH:mm")
                        time.text = sdf.format(model.date)
                    }
                    AdminActivity.Sort.byItem->{
                        userName.visibility= View.GONE
                        exTitle.text = model.ex!!.title
                        val plan = queryFirst<TrainingPlans> { equalTo("id", model.planId) }
                        itemName.text = queryFirst<Users> { equalTo("id", plan?.ownerId) }?.name ?: "name"
                        rep.text = "${model.ex!!.rep-3}-${model.ex!!.rep}x${model.ex!!.sets}"
                        val sdf = SimpleDateFormat("HH:mm")
                        time.text = sdf.format(model.date)
                    }
                    AdminActivity.Sort.byTime->{
                        val plan = queryFirst<TrainingPlans> { equalTo("id", model.planId) }
                        userName.visibility= View.VISIBLE
                        userName.text= queryFirst<Users> { equalTo("id", plan?.ownerId) }?.name ?: "name"
                        exTitle.text = model.ex!!.title
                        itemName.text = model.ex!!.item
                        rep.text = "${model.ex!!.rep - 3}-${model.ex!!.rep}x${model.ex!!.sets}"
                        val sdf = SimpleDateFormat("HH:mm")
                        time.text = sdf.format(model.date)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrainPlanViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean =
            oldItem.id == newItem.id && oldItem.planId == newItem.planId && oldItem.date == newItem.date &&
                    oldItem.train == newItem.train && oldItem.ex!!.id == newItem.ex!!.id
    }


}