package alex.pakshin.trainingschedual.repository

import alex.pakshin.trainingschedual.R
import alex.pakshin.trainingschedual.Repository
import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import java.text.SimpleDateFormat

class RepositoryImpl : Repository {

    override val plans = MutableLiveData<List<TrainingPlans>>()

    override val exercises = MutableLiveData<List<Exersise>>()
    override fun add() {
        TODO("Not yet implemented")
    }

   /* override fun getUser(): Users {
        user = queryFirst<Users> {
            equalTo("email", email.text.toString().trim()).and()
                .equalTo("password", pass.text.toString().trim())
        }


    override fun add() {
        TODO("Not yet implemented")
    }
*/

}