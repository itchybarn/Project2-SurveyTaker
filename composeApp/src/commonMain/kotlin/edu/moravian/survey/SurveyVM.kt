package edu.moravian.survey

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SurveyVM: ViewModel() {
    private val _survey = MutableStateFlow<List<SurveyElement>>(emptyList())
    val survey: StateFlow<List<SurveyElement>> = _survey

    private val _showErrors = MutableStateFlow(false)
    val showErrors: StateFlow<Boolean> = _showErrors

    private val _isSetup = MutableStateFlow(false)
    val isSetup: StateFlow<Boolean> = _isSetup

    fun setup(survey: List<SurveyElement>) {
        if(!_isSetup.value) {
            _survey.value = survey
            _isSetup.value = true
        }
    }

    fun onSurveyChange(updatedSurvey: List<SurveyElement>) {
        _survey.value = updatedSurvey
    }

    fun validate(): Boolean {
        val hasErrors = _survey.value.questions.hasErrors
            _showErrors.value = hasErrors
        return !hasErrors // Returns true if no errors
    }

}