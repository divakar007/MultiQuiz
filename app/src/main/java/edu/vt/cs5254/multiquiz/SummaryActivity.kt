package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivitySummaryBinding


private const val CORRECT_ANSWERS = "edu.vt.cs5254.multiquiz.correct_answers"
private const val HINTS_USED = "edu.vt.cs5254.multiquiz.hints_used"
private const val RESET_ALL = "edu.vt.cs5254.multiquiz.reset_all"
class SummaryActivity : AppCompatActivity() {

    // Name : Divakara Rao Annepu
    // PID : adivakararao

    private val summaryViewModel : SummaryView by viewModels()
    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResetAllResult(summaryViewModel.isReset)
        if (savedInstanceState == null) {
            summaryViewModel.correctAnswers = intent.getIntExtra(CORRECT_ANSWERS, 0)
            summaryViewModel.hintsUsed = intent.getIntExtra(HINTS_USED, 0)
        }

        updateUI()

        binding.resetAllButton.setOnClickListener{
            handleResetAll()
        }
    }

    private fun handleResetAll() {
        summaryViewModel.hintsUsed = 0
        summaryViewModel.correctAnswers = 0
        summaryViewModel.isReset = true
        updateUI()
        setResetAllResult(true)
    }

    private fun updateUI() {
        binding.hintsUsedCount.text = summaryViewModel.hintsUsed.toString()
        binding.correctAnswersCount.text = summaryViewModel.correctAnswers.toString()
        binding.resetAllButton.isEnabled = !summaryViewModel.isReset
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            correctAnswers: Int,
            hintsUsed: Int
        ): Intent {
            val intent = Intent(packageContext, SummaryActivity::class.java).apply {
                putExtra(CORRECT_ANSWERS, correctAnswers)
                putExtra(HINTS_USED, hintsUsed)
            }
            return intent
        }
    }

    private fun setResetAllResult(isReset: Boolean) {
        val result = Intent().apply {
            putExtra(RESET_ALL, isReset)
        }
        setResult(Activity.RESULT_OK, result)
    }
}