package edu.moravian.survey

import edu.moravian.survey.data.SurveyAnswerEntity
import edu.moravian.survey.data.SurveyDatabase
import edu.moravian.survey.data.SurveyEntity

/**
 * Saves the current survey result to the repository. This should be called when the user completes
 * the survey.
 */
suspend fun Survey.save(database: SurveyDatabase) {
    val timestamp = currentTimeMillis()
    val totalScore = questions.score

    val surveyEntity = SurveyEntity(
        dateTime = timestamp,
        score = totalScore
    )

    val answerEntities = questions.map { question ->
        when (question) {
            is QuestionWithSingleOption -> {
                SurveyAnswerEntity(
                    questionId = question.id,
                    selection = question.answer?.let { setOf(it) },
                )
            }
            is QuestionWithMultiOptions -> {
                SurveyAnswerEntity(
                    questionId = question.id,
                    selection = question.answer,
                )
            }
            is QuestionWithMultiOptionsAndOther -> {
                SurveyAnswerEntity(
                    questionId = question.id,
                    selection = question.answer?.first,
                    otherText = question.answer?.second,
                )
            }
        }
    }

    database.getDao().insertSurveyWithAnswers(surveyEntity, answerEntities)
}

fun Survey.applyAnswers(savedAnswers: List<SurveyAnswerEntity>): Survey {
    val answerMap = savedAnswers.associateBy { it.questionId }

    return map { item ->
        val savedAnswer = answerMap[item.id] ?: return@map item

        when (item) {
            is Instruction -> item
            is QuestionWithSingleOption -> {
                item.copy(answer = savedAnswer.selection?.firstOrNull())
            }
            is QuestionWithMultiOptions -> {
                item.copy(answer = savedAnswer.selection)
            }
            is QuestionWithMultiOptionsAndOther -> {
                item.copy(answer = (savedAnswer.selection ?: emptySet()) to (savedAnswer.otherText ?: ""))
            }
        }
    }
}

/**
 * Loads the survey result with the given ID from the repository and maps it back to a Survey.
 */
suspend fun Survey.load(surveyId: Long, database: SurveyDatabase): Survey {
    val savedAnswers = database.getDao().getAnswersForSurvey(surveyId)
    return this.applyAnswers(savedAnswers)
}
