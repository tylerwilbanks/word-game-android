package com.minutesock.dictionary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.core.R
import com.minutesock.core.domain.GuessWordRowInfoView
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordSessionInfoView
import com.minutesock.core.presentation.components.LetterBox
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DictionaryDetailSession(
    sessionInfoViews: ImmutableList<WordSessionInfoView>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
            )
    ) {
        items(sessionInfoViews.size) { i ->
            if (i == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            val session = sessionInfoViews[i]
            if (i > 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            DictionaryDetailSessionListItem(wordSessionInfoView = session)
            if (i < sessionInfoViews.size - 1) {
                Divider()
            }
        }
    }
}

@Composable
internal fun DictionaryDetailSessionListItem(
    wordSessionInfoView: WordSessionInfoView
) {

    val iconId by remember {
        mutableStateOf(
            if (wordSessionInfoView.gameMode == WordGameMode.Daily) {
                R.drawable.baseline_today_24
            } else {
                R.drawable.baseline_infinity
            }
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = wordSessionInfoView.displayDate
            )

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = iconId),
                contentDescription = null
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        wordSessionInfoView.guessWordRowInfoViews.forEach {
            Row {
                DictionaryDetailSessionGuessWordRow(guessWordRowInfoView = it)
            }
        }
    }
}

@Composable
internal fun DictionaryDetailSessionGuessWordRow(
    guessWordRowInfoView: GuessWordRowInfoView
) {

    val textColor = when (guessWordRowInfoView.guessWord.state) {
        GuessWordState.Unused -> MaterialTheme.colorScheme.secondary
        GuessWordState.Editing -> MaterialTheme.colorScheme.secondary
        GuessWordState.Complete -> MaterialTheme.colorScheme.secondary
        GuessWordState.Correct -> MaterialTheme.colorScheme.primary
        GuessWordState.Failure -> MaterialTheme.colorScheme.error
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        guessWordRowInfoView.guessWord.letters.forEach {
            Row {
                LetterBox(
                    letter = it,
                    guessWordState = guessWordRowInfoView.guessWord.state,
                    onEvent = {},
                    flipAnimDelay = 0,
                    isFinalLetterInRow = false
                )
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = guessWordRowInfoView.displayTimestamp,
                textAlign = TextAlign.Right,
                color = textColor
            )
        }
    }
}