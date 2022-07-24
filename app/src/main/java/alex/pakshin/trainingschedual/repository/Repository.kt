package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import androidx.lifecycle.MutableLiveData

interface Repository {
    val plans : MutableLiveData<List<TrainingPlans>>
    val exercises : MutableLiveData<List<Exersise>>

    fun add()
}
