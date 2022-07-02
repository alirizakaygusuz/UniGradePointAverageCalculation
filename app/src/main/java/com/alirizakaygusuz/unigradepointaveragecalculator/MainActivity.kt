package com.alirizakaygusuz.unigradepointaveragecalculator

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.alirizakaygusuz.unigradepointaveragecalculator.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val COMMONLESSONS = arrayOf("Türk Dili",
        "Ingilizce",
        "Atatürk İlkeleri ve Ink.Tarihi",
        "Genel Matematik",
        "Genel Fizik")

    private lateinit var allLessons: ArrayList<Lesson>
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>
    private lateinit var spinnerNewAdapter: ArrayAdapter<CharSequence>

    private var isSelectedSystem: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allLessons = ArrayList()

        initalizeScreen()

        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, COMMONLESSONS)
        binding.autoTxtLesson.setAdapter(adapter)

        lessonListControl()

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, radioID ->
            if (R.id.radioBtnNewSystem == radioID) {
                spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.newNoteSystem,
                    android.R.layout.simple_spinner_item)
                spinnerNewAdapter = ArrayAdapter.createFromResource(this,
                    R.array.newNoteSystem,
                    android.R.layout.simple_spinner_item)
                isSelectedSystem = true
            } else if (R.id.radioBtnOldSystem == radioID) {
                spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.oldNoteSystem,
                    android.R.layout.simple_spinner_item)
                spinnerNewAdapter = ArrayAdapter.createFromResource(this,
                    R.array.oldNoteSystem,
                    android.R.layout.simple_spinner_item)
                isSelectedSystem = true

            }
            binding.spinnerLetter.setAdapter(spinnerAdapter)
        }




        binding.btnAddLesson.setOnClickListener {
            clickBtnAddLesson()
        }

    }

    private fun clickBtnAddLesson() {
        if (!isSelectedSystem) {
            Toast.makeText(this, "Puan Sisteminiz Seçiniz!!", Toast.LENGTH_SHORT).show()

        } else if (binding.eTxtCredit.text.toString()
                .isNotEmpty() && binding.autoTxtLesson.text.isNotEmpty()
        ) {
            val inflater = LayoutInflater.from(this@MainActivity)

            val newLessonView: View = inflater.inflate(R.layout.new_lesson_layout, null)

            val eTxtNewLesson = newLessonView.findViewById<EditText>(R.id.eTxtNewLesson)
            val btnDeleteLesson = newLessonView.findViewById<Button>(R.id.btnDeleteLesson)
            val eTxtNewCredit =
                newLessonView.findViewById<EditText>(R.id.eTxtNewCredit)
            val spinnerNewLetter =
                newLessonView.findViewById<Spinner>(R.id.spinnerNewLetter)



            val lessonName = binding.autoTxtLesson.text.toString()
            val lessonCredit = binding.eTxtCredit.text.toString()
            val lessonLetterPosition = binding.spinnerLetter.selectedItemPosition

            spinnerNewLetter?.adapter = spinnerNewAdapter




            eTxtNewLesson.setText(lessonName)
            eTxtNewCredit.setText(lessonCredit)
            spinnerNewLetter.setSelection(lessonLetterPosition)


            binding.linearLayoutMainInsideScrollView.addView(newLessonView)
            lessonListControl()
            clearInput()

            btnDeleteLesson.setOnClickListener {
                binding.linearLayoutMainInsideScrollView.removeView(newLessonView)

                lessonListControl()
            }



        } else {
            Toast.makeText(this, "Hiç Bir Alanı Boş Bırakmayınız!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInput() {
        binding.autoTxtLesson.text.clear()
        binding.eTxtCredit.text.clear()
        binding.spinnerLetter.setSelection(0)
    }

    private fun lessonListControl() {
        binding.btnHesapla.isEnabled = binding.linearLayoutMainInsideScrollView.childCount > 0

    }


    private fun initalizeScreen() {
        Glide.with(this).load(R.drawable.background).into(object :
            CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                binding.linearLayoutMain.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }


        })
    }

    fun calculateAverage(view: View) {

        var totalPoint = 0.0
        var totalCredi = 0.0

        val new_lesson_layout: View = layoutInflater.inflate(R.layout.new_lesson_layout, null)

        for (i in 0 until binding.linearLayoutMainInsideScrollView.childCount) {

            val oneLineData = binding.linearLayoutMainInsideScrollView.getChildAt(i)

            val eTxtNewLesson = oneLineData.findViewById<EditText>(R.id.eTxtNewLesson)
            val eTxtNewCredit = oneLineData.findViewById<EditText>(R.id.eTxtNewCredit)
            val spinnerNewLetter = oneLineData.findViewById<Spinner>(R.id.spinnerNewLetter)

            val tmpLesson = Lesson(eTxtNewLesson.text.toString(),
                eTxtNewCredit.text.toString(),
                spinnerNewLetter.selectedItem.toString())


            allLessons.add(tmpLesson)

        }

        for (currentLesson in allLessons) {
            totalPoint += convertLetterToScore(currentLesson.lessonLetter) * (currentLesson.lessonCredit.toDouble())
            totalCredi += currentLesson.lessonCredit.toDouble()
        }

        binding.txtOrtalama.text = "Ortalamanız:  ${((totalPoint / totalCredi).formatVariable())}"

        allLessons.clear()
    }


    fun convertLetterToScore(str: String): Double {

        var point = 0.0


        when (str) {
            "A1", "AA" -> point = 4.0
            "A2" -> point = 3.75
            "A3", "BA" -> point = 3.50
            "B1" -> point = 3.25
            "B2", "BB" -> point = 3.00
            "B3" -> point = 2.75
            "C1", "CB" -> point = 2.5
            "C2" -> point = 2.25
            "C3", "CC" -> point = 2.0
            "D1" -> point = 1.75
            "D2", "DC" -> point = 1.5
            "F1" -> point = 1.25
            "F2", "DD" -> point = 1.0
            "FD" -> point = 0.5
            "FF" -> point = 0.0
        }

        return point
    }

    @SuppressLint("DefaultLocale")
    fun Double.formatVariable() = java.lang.String.format("%.2f", this)
}