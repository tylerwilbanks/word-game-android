package com.minutesock.dictionary.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.core.uiutils.blendColors
import com.minutesock.core.uiutils.shimmerEffect
import com.minutesock.core.utils.capitalize
import com.minutesock.dictionary.domain.DictionaryEvent
import com.minutesock.dictionary.domain.WordInfoListItem
import com.minutesock.dictionary.navigation.navigateToDictionaryDetail

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun DictionaryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DictionaryViewModel = viewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.updateList()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                    .padding(10.dp),
                text = "Discovered words: ${state.value.unlockedWordCount} / ${state.value.totalWordCount}",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.value.headerItems.forEach { dictionaryHeaderItem ->
                stickyHeader {
                    CategoryHeader(text = dictionaryHeaderItem.char.toString())
                }
                items(dictionaryHeaderItem.listItems.size) {
                    CategoryItem(
                        item = dictionaryHeaderItem.listItems[it],
                        onClick = navController::navigateToDictionaryDetail
                    )
                }
            }
        }
    }

}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun CategoryItem(
    item: WordInfoListItem,
    modifier: Modifier = Modifier,
    onClick: (word: String) -> Unit,
) {
    Text(
        text = item.word.capitalize(),
        fontSize = 16.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clickable {
                onClick(item.word)
            }
            .shimmerEffect(
                color1 = MaterialTheme.colorScheme.background,
                color2 = blendColors(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary,
                    0.15f
                ),
                duration = 3_000
            ),
        color = MaterialTheme.colorScheme.primary,
    )
}
