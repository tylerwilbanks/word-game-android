package com.minutesock.profile.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.DailyWordSession
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.lockInGuess
import com.minutesock.core.domain.updateState
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow
import com.minutesock.core.utils.capitalize
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Date


@Composable
internal fun HistoryRoute(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    profileViewModel: ProfileViewModel = viewModel()
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                profileViewModel.loadPaginatedHistory()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    HistoryScreen(
        historyList = profileViewModel.historyList
    )
}

@Composable
internal fun HistoryScreen(
    historyList: SnapshotStateList<DailyWordSession>
) {

    HistoryList(historyList = historyList)
}

@Composable
internal fun HistoryList(
    historyList: SnapshotStateList<DailyWordSession>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(historyList.size) { index ->
            HistoryListItem(dailyWordSession = historyList[index])
            if (index < historyList.lastIndex) {
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
internal fun HistoryListItem(
    dailyWordSession: DailyWordSession
) {
   val errorColor = MaterialTheme.colorScheme.error
    val a = MaterialTheme.colorScheme.outline

    val borderColor by remember(dailyWordSession.gameState) {
        val color = when (dailyWordSession.gameState) {
            DailyWordGameState.Success -> guessLetterGreen
            DailyWordGameState.InProgress -> guessLetterYellow
            DailyWordGameState.Failure -> errorColor
            else -> a
        }
        mutableStateOf(color)
    }

    val backgroundColor = when (dailyWordSession.gameState) {
        DailyWordGameState.NotStarted -> MaterialTheme.colorScheme.outlineVariant
        DailyWordGameState.InProgress -> MaterialTheme.colorScheme.outlineVariant
        DailyWordGameState.Success -> MaterialTheme.colorScheme.primaryContainer
        DailyWordGameState.Failure -> MaterialTheme.colorScheme.errorContainer
    }

    val completeTime by remember(dailyWordSession.completeTime) {
        mutableStateOf(
            if (dailyWordSession.formattedTime == null) {
                "Incomplete"
            } else {
                dailyWordSession.formattedTime?.toString() ?: ""
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .blur(if (dailyWordSession.gameState.isGameOver) 0.dp else 15.dp),
                text = dailyWordSession.correctWord.capitalize()
            )
            Text(text = completeTime)
        }
    }
}

@Preview
@Composable
internal fun HistoryScreenPreview() {
    val historyList = remember {
        mutableStateListOf<DailyWordSession>()
    }

    val maxAttempts = 6
    val wordLength = 5

    val w = List(maxAttempts) {
        GuessWord(
            List(wordLength) {
                GuessLetter()
            }.toImmutableList()
        )
    }.toMutableList()

    w[2] = w[2].lockInGuess("     ", true)

    val w2 = List(maxAttempts) {
        GuessWord(
            List(wordLength) {
                GuessLetter()
            }.toImmutableList()
        )
    }.toMutableList()

    w2[w2.lastIndex] = w2.last().lockInGuess("     ", true)

    val w3 = List(maxAttempts) {
        GuessWord(
            List(wordLength) {
                GuessLetter()
            }.toImmutableList()
        )
    }.toMutableList()

    w[3] = w[3].updateState(GuessWordState.Editing)

    historyList.addAll(
        listOf(
            DailyWordSession(
                date = Date(),
                correctWord = "smooth",
                guesses = w.toImmutableList(),
                maxAttempts = maxAttempts,
                isDaily = true,
                startTime = Clock.System.now(),
                gameState = DailyWordGameState.Success
            ),

            DailyWordSession(
                date = Date(),
                correctWord = "jumby",
                guesses = w2.toImmutableList(),
                maxAttempts = maxAttempts,
                isDaily = true,
                startTime = Clock.System.now(),
                gameState = DailyWordGameState.Failure
            ),
            DailyWordSession(
                date = Date(),
                correctWord = "smack",
                guesses = w3.toImmutableList(),
                maxAttempts = maxAttempts,
                isDaily = true,
                startTime = Clock.System.now(),
                gameState = DailyWordGameState.InProgress
            )
        )
    )

    WordGameTheme {
        HistoryScreen(
            historyList = historyList
        )
    }
}

private fun createDummyWordSessions() {

}