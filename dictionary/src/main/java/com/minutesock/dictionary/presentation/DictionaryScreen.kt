package com.minutesock.dictionary.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.utils.capitalize
import com.minutesock.dictionary.domain.DictionaryEvent
import com.minutesock.dictionary.domain.WordInfoListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DictionaryScreen(
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = viewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.value.headerItems.forEach { dictionaryHeaderItem ->
            stickyHeader {
                CategoryHeader(text = dictionaryHeaderItem.char.toString())
            }
            items(dictionaryHeaderItem.listItems.size) {
                CategoryItem(item = dictionaryHeaderItem.listItems[it], onEvent = viewModel::onEvent)
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
    onEvent: (DictionaryEvent) -> Unit,
) {
    Text(
        text = item.word.capitalize(),
        fontSize = 14.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clickable {
                       onEvent(DictionaryEvent.OnWordInfoListItemClicked(item.word))
            },
        color = MaterialTheme.colorScheme.primary,
    )
}
