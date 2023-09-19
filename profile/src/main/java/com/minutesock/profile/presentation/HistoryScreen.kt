package com.minutesock.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.R
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.WordSession
import com.minutesock.core.domain.lockInGuess
import com.minutesock.core.domain.updateState
import com.minutesock.core.presentation.SmallTopAppBar
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow
import com.minutesock.core.utils.capitalize
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import java.util.Date


@Composable
internal fun HistoryRoute(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    profileViewModel: ProfileViewModel = viewModel(),
    onBackButtonClicked: () -> Unit,
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
        historyList = profileViewModel.historyList,
        onEvent = profileViewModel::onHistoryEvent,
        onBackButtonClicked = onBackButtonClicked,
    )
}

@Composable
internal fun HistoryScreen(
    historyList: SnapshotStateList<WordSession>,
    onEvent: (HistoryScreenEvent) -> Unit,
    onBackButtonClicked: () -> Unit,
) {

    SmallTopAppBar(title = "History", onBackButtonClicked = onBackButtonClicked) {
        HistoryList(
            historyList = historyList,
            onEvent = onEvent,
            modifier = Modifier.padding(top = it.calculateTopPadding())
        )
    }
}

@Composable
internal fun HistoryList(
    historyList: SnapshotStateList<WordSession>,
    onEvent: (HistoryScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(historyList.size) { index ->
            onEvent(HistoryScreenEvent.UpdateScrollPosition(index))
            HistoryListItem(wordSession = historyList[index])
            if (index < historyList.lastIndex) {
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
internal fun HistoryListItem(
    wordSession: WordSession
) {
    val errorColor = MaterialTheme.colorScheme.error
    val a = MaterialTheme.colorScheme.outline

    val borderColor by remember(wordSession.gameState) {
        val color = when (wordSession.gameState) {
            DailyWordGameState.Success -> guessLetterGreen
            DailyWordGameState.InProgress -> guessLetterYellow
            DailyWordGameState.Failure -> errorColor
            else -> a
        }
        mutableStateOf(color)
    }

    val backgroundColor = when (wordSession.gameState) {
        DailyWordGameState.NotStarted -> MaterialTheme.colorScheme.scrim
        DailyWordGameState.InProgress -> MaterialTheme.colorScheme.secondaryContainer
        DailyWordGameState.Success -> MaterialTheme.colorScheme.primaryContainer
        DailyWordGameState.Failure -> MaterialTheme.colorScheme.errorContainer
    }

    val completeTime by remember(wordSession.completeTime) {
        mutableStateOf(
            if (wordSession.formattedTime == null) {
                "Incomplete"
            } else {
                wordSession.formattedTime?.toString() ?: ""
            }
        )
    }

    val brush =
        Brush.verticalGradient(listOf(MaterialTheme.colorScheme.outlineVariant, backgroundColor))
    val textColor = MaterialTheme.colorScheme.secondary

    val iconId by remember {
        mutableStateOf(
            if (wordSession.isDaily) {
                R.drawable.baseline_today_24
            } else {
                R.drawable.baseline_infinity
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = brush,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .blur(if (wordSession.gameState.isGameOver) 0.dp else 15.dp),
                    text = wordSession.correctWord.capitalize(),
                    color = textColor,
                    fontSize = 16.sp
                )
                Text(text = completeTime, color = textColor, fontSize = 16.sp)

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = wordSession.emojiRepresentation
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = iconId),
                    tint = textColor,
                    contentDescription = null
                )

                Text(
                    text = wordSession.formattedElapsedTime,
                    color = textColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun HistoryScreenPreview() {
    val historyList = remember {
        mutableStateListOf<WordSession>()
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
            WordSession(
                date = Date(),
                correctWord = "smooth",
                guesses = w.toImmutableList(),
                maxAttempts = maxAttempts,
                isDaily = true,
                startTime = Clock.System.now(),
                gameState = DailyWordGameState.Success
            ),

            WordSession(
                date = Date(),
                correctWord = "jumby",
                guesses = w2.toImmutableList(),
                maxAttempts = maxAttempts,
                isDaily = true,
                startTime = Clock.System.now(),
                gameState = DailyWordGameState.Failure
            ),
            WordSession(
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

    WordGameTheme(useDarkTheme = true) {
        HistoryScreen(
            historyList = historyList,
            onEvent = {},
            onBackButtonClicked = {}
        )
    }
}