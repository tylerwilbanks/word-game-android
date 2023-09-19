package com.minutesock.infinity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.presentation.WordGameScreen

@Composable
internal fun InfinityScreen(
    modifier: Modifier = Modifier
) {
    WordGameScreen(gameMode = WordGameMode.Inifinity, modifier = modifier)
}