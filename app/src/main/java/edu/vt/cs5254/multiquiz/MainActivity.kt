package edu.vt.cs5254.multiquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val RESET_ALL = "edu.vt.cs5254.multiquiz.reset_all"
class MainActivity : AppCompatActivity() {

    // Name : Divakara Rao Annepu
    // PID : adivakararao

    private lateinit var binding: ActivityMainBinding

    private val resetLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            questionBankView.isReset =
                result.data?.getBooleanExtra(RESET_ALL, false) ?: false
            this.updateUIonResetALL(questionBankView.isReset)
        }
    }

    private fun updateUIonResetALL(isReset: Boolean) {
        if (isReset){
            questionBankView.handleResetAll()
            answerButtonsList().forEach {
                it.isSelected = false
                it.isEnabled = true
            }
        }
        setUpAnswerButtons()
    }

    private val questionBankView: QuestionBankView by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAnswerButtons()

        binding.submitButton.setOnClickListener{
            handleSubmitButton()
        }

        binding.hintButton.setOnClickListener{
            handleHintButton()
        }
    }

    private fun handleSubmitButton() {
        var currentQuestion = questionBankView.getCurrentQuestion()
        var buttons = answerButtonsList()
        currentQuestion.answerList.forEachIndexed{
                ind, answer ->
            if (buttons[ind].isSelected && answer.isCorrect) questionBankView.setCorrectAnswersCount()
        }

        if (questionBankView.isEndOfQuestions()){
            questionBankView.setCurrentQuestion()
            Log.d(TAG, "isEndOfQuestions called")
            val intent = SummaryActivity.newIntent(this,questionBankView.correctAnswersCount,questionBankView.getHintsCount())
            resetLauncher.launch(intent)
        }
        else {
            questionBankView.setCurrentQuestion()
            setUpAnswerButtons()
            binding.hintButton.isEnabled = true
        }
    }

    private fun handleHintButton() {

        questionBankView.setHintsCount()
        val currentQuestion = questionBankView.getCurrentQuestion()

        currentQuestion.answerList.filter { answer -> !answer.isCorrect && answer.isEnabled }
            .shuffled().take(1).forEach {
                it.isEnabled = false
                it.isSelected = false
            }
        answerButtonsList().forEachIndexed{ind,button ->
            button.isEnabled = currentQuestion.answerList[ind].isEnabled
            button.isSelected = currentQuestion.answerList[ind].isSelected
            button.updateColor()
        }

        binding.hintButton.isEnabled = currentQuestion.answerList.count{ it.isEnabled } != 1
    }

    private fun setUpAnswerButtons() {
        setUpQuestionUI()

        val submitButton = binding.submitButton

        val currentQuestion = questionBankView.getCurrentQuestion()
        binding.hintButton.isEnabled = currentQuestion.answerList.count{ it.isEnabled } != 1
        submitButton.isEnabled = false
        currentQuestion.answerList.forEach {
            submitButton.isEnabled = submitButton.isEnabled or it.isSelected
        }

        answerButtonsList().forEachIndexed{ind, button ->
            button.isEnabled = currentQuestion.answerList[ind].isEnabled
            button.isSelected = currentQuestion.answerList[ind].isSelected

            button.setOnClickListener{
                updateAnswerButtonsUI(ind)
                currentQuestion.answerList.forEachIndexed{index,ans ->
                    ans.isSelected = index == ind && button.isSelected
                }

                submitButton.isEnabled =  button.isSelected
            }
            button.updateColor()
        }

    }

    private fun setUpQuestionUI() {
        val currentQuestion = questionBankView.getCurrentQuestion()
        binding.questionTextView.text = getString(currentQuestion.questionResId)
        answerButtonsList().zip(currentQuestion.answerList).forEach {
           it.first.text = getString( it.second.textResId )
            it.first.isSelected = it.second.isSelected
            it.first.isEnabled = it.second.isEnabled

        }
    }

    private fun updateAnswerButtonsUI(ind: Int) {
        answerButtonsList().forEachIndexed{
                index,button ->
            button.isSelected = ind == index && !button.isSelected
            button.updateColor()
        }
    }


    private fun answerButtonsList(): Array<Button> {
        return arrayOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        )
    }

}