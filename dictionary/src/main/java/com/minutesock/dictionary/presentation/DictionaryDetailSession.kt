package com.minutesock.dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.core.domain.GuessWordRowInfoView
import com.minutesock.core.domain.WordSessionInfoView
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
    // todo add background gradient based on whether gameState
    Column {
        wordSessionInfoView.guessWordRowInfoViews.forEach {
            DictionaryDetailSessionGuessWordRow(guessWordRowInfoView = it)
        }

        // todo add text views for the stats
    }
}

@Composable
internal fun DictionaryDetailSessionGuessWordRow(
    guessWordRowInfoView: GuessWordRowInfoView
) {

}