package alex.pakshin.trainingschedual.viewModel

import alex.pakshin.trainingschedual.Repository
import alex.pakshin.trainingschedual.models.Users
import alex.pakshin.trainingschedual.repository.RepositoryImpl
import androidx.lifecycle.ViewModel

class PlanViewModel:ViewModel() {
    private val repository:Repository = RepositoryImpl()
    val plans by repository::plans
    val exercises by repository::exercises

    fun checkUser(users: Users){
        repository.add()
    }
}