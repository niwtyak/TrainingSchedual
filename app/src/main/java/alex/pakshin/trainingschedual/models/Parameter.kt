package alex.pakshin.trainingschedual.models

import io.realm.RealmObject

open class Parameter (
    var ownerId:Long? = null,
    var sex: String = "",
    var age: Int? = null,
    var weight: Int? = null,
    var height: Int? = null,
    var experience: String = "",
    var target: String = "",
    var difficulty: String = ""
):RealmObject()
