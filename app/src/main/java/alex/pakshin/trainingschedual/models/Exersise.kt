package alex.pakshin.trainingschedual.models

import com.vicpin.krealmextensions.AutoIncrementPK
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
@AutoIncrementPK
open class Exersise(
    @PrimaryKey var id: Long? = null,
    var planId: Long?=null,
    var title: String = " " ,
    var coef:Int?=null,
    var target:String="",
    var item: String="",
    var rep:Int=0,
    var sets:Int=0,
    var number:Int? = null,
    var time: String = ""
):RealmObject()