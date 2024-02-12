package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class SummaryView : ViewModel() {

    var isReset: Boolean = false
    var correctAnswers = 0

    var hintsUsed = 0

}