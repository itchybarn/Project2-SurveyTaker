package edu.moravian.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.survey.data.SurveyDatabase
import edu.moravian.survey.data.SurveyEntity
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.*

/**
 * The home screen destination, which shows the current status and allows the user to take a survey
 * or view their history.
 */
@Serializable
data object HomeScreen

/**
 * The home screen, which shows the current status and allows the user to take a survey or view
 * their history.
 */
@Composable
fun HomeScreen(
    onTakeSurvey: () -> Unit,
    onOpenHistory: () -> Unit,
    database: SurveyDatabase,
) {
    val surveys by database.getDao().getAllSurveys().collectAsState(initial = emptyList())
    val lastSurvey = surveys.firstOrNull()

    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatusText(lastSurvey)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTakeSurvey) { Text(stringResource(Res.string.take_survey)) }
        TextButton(onClick = onOpenHistory) { Text(stringResource(Res.string.view_history)) }
    }
}

@Composable
private fun StatusText(
    lastSurvey: SurveyEntity?,
) {
    val now = currentTimeMillis()

    if (lastSurvey == null) {
        Text(stringResource(Res.string.no_survey_results_yet))
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                stringResource(Res.string.last_completed, formatEpochMillis(lastSurvey.dateTime)),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                stringResource(Res.string.last_score, lastSurvey.score),
                style = MaterialTheme.typography.bodyLarge,
            )

            val reminder = reminderMessage(now, lastSurvey.dateTime)
            if (reminder != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(reminder),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
