package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class QuestionBankView() : ViewModel() {

    private var currentQuestionIndex = 0
    private var hintsCount = 0

    var isReset = false

    private val questionBank = listOf(
        Question( R.string.question_president,
            listOf(
                Answer(R.string.answer_washington, true),
                Answer(R.string.answer_lincon,false),
                Answer(R.string.answer_jhon, false),
                Answer(R.string.answer_frank, false)
            )
        ),
        Question( R.string.question_planet,
            listOf(
                Answer(R.string.answer_venus, false),
                Answer(R.string.answer_mars,true),
                Answer(R.string.answer_jupiter, false),
                Answer(R.string.answer_saturn, false)
            )
        ),
        Question( R.string.question_highestruns,
            listOf(
                Answer(R.string.answer_warner,false),
                Answer(R.string.answer_babar, false),
                Answer(R.string.answer_kohli, true),
                Answer(R.string.answer_butler, false)
            )
        ),
        Question( R.string.question_capital,
            listOf(
                Answer(R.string.answer_berlin,false),
                Answer(R.string.answer_london, false),
                Answer(R.string.answer_rome, false),
                Answer(R.string.answer_paris, true)
            )
        )
    )


    fun getCurrentQuestion(): Question {
        return questionBank[currentQuestionIndex]
    }

    fun setCurrentQuestion(){
        currentQuestionIndex = (currentQuestionIndex + 1) % questionBank.size
    }

    fun isEndOfQuestions(): Boolean {
        return currentQuestionIndex == questionBank.size - 1
    }

    fun setHintsCount() {
        hintsCount += 1
    }

    fun getHintsCount(): Int{
        return hintsCount
    }

    fun handleResetAll() {
        if (isReset) {
            hintsCount = 0
            correctAnswersCount = 0
            questionBank.forEach { it ->
                it.answerList.forEach {
                    it.isSelected = false
                    it.isEnabled = true
                }
            }
        }
    }
    var correctAnswersCount : Int = 0

    fun setCorrectAnswersCount(){
        correctAnswersCount += 1
    }
}