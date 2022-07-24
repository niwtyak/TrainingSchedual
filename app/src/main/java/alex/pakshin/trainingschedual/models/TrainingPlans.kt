package alex.pakshin.trainingschedual.models

import com.vicpin.krealmextensions.AutoIncrementPK
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

@AutoIncrementPK
open class TrainingPlans(
    @PrimaryKey var id: Long? = null,
    var ownerId: Long? = null,
    var date: Date? = null,
    var title: String = ""
) : RealmObject()