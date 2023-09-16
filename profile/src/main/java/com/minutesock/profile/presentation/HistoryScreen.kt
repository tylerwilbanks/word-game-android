package com.minutesock.profile.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.DailyWordSession
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow

@Composable
internal fun HistoryScreen(
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

    HistoryList(historyList = profileViewModel.historyList)
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
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .blur(if (dailyWordSession.gameState.isGameOver) 0.dp else 15.dp),
                text = dailyWordSession.correctWord
            )
            Text(text = dailyWordSession.completeTime.toString())
        }
    }
}