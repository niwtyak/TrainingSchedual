package alex.pakshin.trainingschedual.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import com.vicpin.krealmextensions.AutoIncrementPK

@AutoIncrementPK
open class Users(
    @PrimaryKey var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var admin:Boolean = false
) : RealmObject()
