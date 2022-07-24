package alex.pakshin.trainingschedual

import alex.pakshin.trainingschedual.models.Exersise
import alex.pakshin.trainingschedual.models.Parameter
import alex.pakshin.trainingschedual.models.TrainingPlans
import alex.pakshin.trainingschedual.models.Users
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.save
import com.vicpin.krealmextensions.saveAll
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.add_new_plan_activity.*
import org.bson.types.ObjectId
import java.util.*
import kotlin.math.pow

class AddNewPlanActivity : AppCompatActivity() {

    enum class Items{
        Турник,Скамья,Зал, СкамьяДляЖима, ГруднойТренажер, Брусья, ТренажерСмита, СкамьяДляЖимаНогами, ТренажёрДляРазведенияНог,ТренажёрДляСгибанияРук
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_plan_activity)

        val intent: Intent? = intent
        Realm.getInstance(MainActivity.RealmUtility.defaultConfig)

        if (intent != null) {
            val gson = Gson()
            val curUser: Users = gson.fromJson(
                intent.getStringExtra("newPlan"),
                Users::class.java
            )



            button.setOnClickListener {
                val newParams = Parameter(
                    curUser.id,
                    editSex.selectedItem.toString(),
                    edit_age.text.toString().toInt(),
                    edit_weigth.text.toString().toInt(),
                    edit_heigth.text.toString().toInt(),
                    editExp.selectedItem.toString(),
                    editTarget.selectedItem.toString(),
                    editDif.selectedItem.toString()
                )
                newParams.save()


                var exList: LinkedList<Exersise> = LinkedList()

                exList.add(Exersise(0, null, "Скручивания в висе ", 25, "press","Турник"))
                exList.add(Exersise(0, null, "Подъём ног в висе", 22, "press","Турник"))
                exList.add(Exersise(0, null, "Подъём ног на скамье", 17, "press","Скамья"))
                exList.add(Exersise(0, null, "Подъём ног лежа", 15, "press","Зал"))
                exList.add(Exersise(0, null, "Подъём ног в упоре", 13, "press","Скамья"))
                exList.add(Exersise(0, null, "Скручивания лёжа на скамье", 10, "press","Скамья"))
                exList.add(Exersise(0, null, "Жим штанги лёжа", 25, "chest","Скамья для жима"))
                exList.add(Exersise(0, null, "Жим в грудном тренажёре", 22, "chest","Грудной тренажер"))
                exList.add(Exersise(0, null, "Отжимания узким хватом", 17, "chest","Зал"))
                exList.add(Exersise(0, null, "Жим штанги узким хватом", 15, "chest","Скамья для жима"))
                exList.add(Exersise(0, null, "Отжимания от брусьев", 13, "chest","Брусья"))
                exList.add(Exersise(0, null, "Жим гантелей лёжа", 10, "chest","Скамья"))
                exList.add(Exersise(0, null, "Становая тяга со штангой", 25, "back","Тренажер смита"))
                exList.add(Exersise(0, null, "Тяга штанги в наклоне", 22, "back","Тренажер смита"))
                exList.add(Exersise(0, null, "Подтягивания широким хватом", 17, "back","Турник"))
                exList.add(Exersise(0, null, "Подтягивания узким хватом", 15, "back","Турник"))
                exList.add(Exersise(0, null, "Гиперэкстензия", 13, "back","Скамья"))
                exList.add(Exersise(0, null, "Наклоны с резинкой ", 10, "back","Зал"))
                exList.add(Exersise(0, null, "Приседания со штангой ", 25, "legs","Тренажер смита"))
                exList.add(Exersise(0, null, "Болгарские приседания", 22, "legs","Зал"))
                exList.add(Exersise(0, null, "Приседания на одной ноге", 17, "legs","Зал"))
                exList.add(Exersise(0, null, "Выпады со штангой", 15, "legs","Тренажер смита"))
                exList.add(Exersise(0, null, "Жим ногами в тренажёре", 13, "legs","Скамья для жима ногами"))
                exList.add(Exersise(0, null, "Разведение ног в тренажёре", 10, "legs","Тренажёр для разведения ног"))
                exList.add(Exersise(0, null, "Сгибания рук с гантелями «молот»", 25, "arms","Турник"))
                exList.add(Exersise(0, null, "Сгибание рук со штангой стоя", 22, "arms","Зал"))
                exList.add(Exersise(0, null, "Сгибания рук с гантелями сидя", 17, "arms","Скамья"))
                exList.add(Exersise(0, null, "Французский жим гантелей", 15, "arms","Скамья"))
                exList.add(Exersise(0, null, "Отжимания от лавки сзади", 13, "arms","Скамья"))
                exList.add(Exersise(0, null, "Сгибание рук в тренажёре", 10, "arms","Тренажёр для сгибания рук"))


                var ypr: Int = 0
                var repEasy: Int = 0
                var repHard: Int = 0
                var diff: Int = 0

                val newExList: MutableList<Exersise> = LinkedList()

                val newPlan = TrainingPlans(null, curUser.id, Calendar.getInstance().time)
                newPlan.save()

                if (newParams.sex == "Женский") {
                    ypr -= 6; repEasy -= 3; repHard -= 2; diff -= 2
                }

                when (newParams.weight!!.div((newParams.height!!.toDouble()).pow(2.0))) {
                    in 1.0..18.5 -> {
                        ypr += 5; repEasy += 3; repHard += 2; diff += 2
                    }
                    in 18.6..25.0 -> {
                        ypr += 10; repEasy += 6; repHard += 3; diff += 6
                    }
                    in 25.1..35.0 -> {
                        ypr += 5; repEasy += 7; repHard += 1; diff += 8
                    }
                }

                when (newParams.experience) {
                    "До 3х месяцев" -> {
                        ypr += 10; repEasy += 5; repHard += 2; diff += 6
                    }
                    "До полугода" -> {
                        ypr += 15; repEasy += 8; repHard += 3; diff += 10
                    }
                    "От полугода до года" -> {
                        ypr += 20; repEasy += 10; repHard += 4; diff += 12
                    }
                    "Больше года" -> {
                        ypr += 25; repEasy += 12; repHard += 5; diff += 16
                    }
                }

                when (newParams.target) {
                    "Похудение" -> {
                        ypr += 20; repEasy += 13; repHard += 2; diff += 10
                    }
                    "Рельеф" -> {
                        ypr += 10; repEasy += 10; repHard += 3; diff += 8
                    }
                    "Набор массы" -> {
                        ypr += 5; repEasy += 5; repHard += 4; diff += 6
                    }
                }

                when (newParams.difficulty) {
                    "Легкий" -> {
                        ypr += 10; repEasy += 5; repHard += 2; diff += 6
                    }
                    "Средний" -> {
                        ypr += 15; repEasy += 8; repHard += 3; diff += 10
                    }
                    "Тяжелый" -> {
                        ypr += 20; repEasy += 13; repHard += 4; diff += 12
                    }
                }

                var reps: Int = 0
                when (ypr) {
                    in 1..35 -> reps = 4
                    in 36..45 -> reps = 5
                    in 46..55 -> reps = 6
                    in 56..80 -> reps = 7
                }

                var easyRep: Int = 0
                var te:Int=0
                when (repEasy) {
                    in 1..18 -> {easyRep = 10 ; te = 2}
                    in 19..22 -> {easyRep = 12; te = 2 }
                    in 23..26 -> {easyRep = 12; te = 3}
                    in 27..36 -> {easyRep = 15; te = 3}
                    in 37..40 -> {easyRep = 18; te = 3}
                }

                var hardRep:Int=0
                var th:Int =0
                when (repHard) {
                    in 1..8 -> {hardRep = 5 ;th=3 }
                    in 9..11 -> {hardRep = 6; th= 3}
                    in 12..15 -> {hardRep = 8;th=3}
                    in 16..20 -> {hardRep = 10;th = 4 }
                }

                var type: String = ""
                var ez: Boolean = true


                for (i in 1..3) {
                    for (j in 0..reps) {
                        when (i * j) {
                            0 -> {
                                type = "press"; ez = true
                            }
                            1, 2, 3, 10, 18 -> {
                                type = "chest"; ez = false
                            }
                            5, 6, 12 -> {
                                type = "back"; ez = false
                            }
                            4, 8 -> {
                                type = "legs"; ez = true
                            }
                            9, 15 -> {
                                type = "arms"; ez = true
                            }
                        }
                        var planId =
                            queryLast<TrainingPlans> { findAll() }!!.id


                        exList.find { (it.target == type) && newExList.find { ex-> ex.title == it.title }== null && it.coef!!<diff }
                            ?.let {
                                val newEx = Exersise(
                                    null,
                                    planId,
                                    it.title,
                                    it.coef,
                                    it.target,
                                    it.item,
                                    if (ez) easyRep else hardRep,
                                    if (ez) te else th,
                                    i
                                )
                                newExList.add(newEx)
                                newEx.save()
                            }
                    }
                }

                Toast.makeText(applicationContext, "Запись произведена ", Toast.LENGTH_SHORT)
                    .show()

                setResult(RESULT_OK, Intent())
                finish()
            }
        }
    }
}