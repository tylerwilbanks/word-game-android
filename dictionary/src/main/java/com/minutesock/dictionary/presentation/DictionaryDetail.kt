package com.minutesock.dictionary.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
internal fun DictionaryDetail(
    modifier: Modifier = Modifier,
    word: String,
    navController: NavController,
    viewModel: DictionaryViewModel = viewModel()
) {
    Text(text = word)
}