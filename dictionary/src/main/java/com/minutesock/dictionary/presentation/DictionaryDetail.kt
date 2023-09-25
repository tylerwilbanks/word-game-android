package com.minutesock.dictionary.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.core.domain.WordInfo
import com.minutesock.core.presentation.SmallTopAppBar
import com.minutesock.core.presentation.components.WordInfoItem
import com.minutesock.core.utils.capitalize
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DictionaryDetail(
    modifier: Modifier = Modifier,
    word: String,
    navController: NavController,
    viewModel: DictionaryDetailViewModel = viewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.loadDictionaryDetail(word)
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val displayWord by remember {
        mutableStateOf(word.capitalize())
    }
    SmallTopAppBar(
        title = displayWord,
        onBackButtonClicked = navController::popBackStack
    ) { padding ->
        WordDefinitionList(padding = padding, wordInfos = state.wordInfos)
    }
}

@Composable
private fun WordDefinitionList(
    padding: PaddingValues,
    wordInfos: ImmutableList<WordInfo>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = padding.calculateBottomPadding(),
                top = padding.calculateTopPadding(),
                start = 20.dp,
                end = 20.dp
            )
    ) {
        items(wordInfos.size) { i ->
            val wordInfo = wordInfos[i]
            if (i > 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            WordInfoItem(
                wordInfo = wordInfo,
                wordColor = MaterialTheme.colorScheme.primary
            )
            if (i < wordInfos.size - 1) {
                Divider()
            }
        }
    }
}