package com.minutesock.dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val sessions by viewModel.wordSessionInfoViews.collectAsStateWithLifecycle()

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

    val tabs by remember {
        mutableStateOf(
            DictionaryDetailTabs.values()
        )
    }

    var selectedTab by remember {
        mutableStateOf(DictionaryDetailTabs.Definition)
    }


    SmallTopAppBar(
        title = displayWord,
        onBackButtonClicked = navController::popBackStack
    ) { padding ->
        Column(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
        ) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal
            ) {
                tabs.forEachIndexed { index: Int, _: DictionaryDetailTabs ->
                    Tab(
                        selected = selectedTab.ordinal == index,
                        onClick = {
                            selectedTab = DictionaryDetailTabs.fromInt(index)
                        },
                        text = { Text(text = tabs[index].displayName) }
                    )
                }
            }
            when (selectedTab) {
                DictionaryDetailTabs.Definition -> WordDefinitionList(wordInfos = state.wordInfos)
                DictionaryDetailTabs.Sessions -> {
                    DictionaryDetailSession(sessionInfoViews = sessions)
                }
            }

        }
    }
}

@Composable
private fun WordDefinitionList(
    wordInfos: ImmutableList<WordInfo>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
            )
    ) {
        items(wordInfos.size) { i ->
            if (i == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
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

private enum class DictionaryDetailTabs {
    Definition,
    Sessions;

    val displayName: String
        get() = when (this) {
            Definition -> "Definition"
            Sessions -> "Sessions"
        }

    companion object {
        fun fromInt(value: Int) = DictionaryDetailTabs.values().first { it.ordinal == value }
    }
}