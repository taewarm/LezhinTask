package com.example.lezhintask.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lezhintask.R
import com.example.lezhintask.ui.main.MainActivity
import com.example.lezhintask.ui.main.getDateTime
import com.example.lezhintask.ui.main.showNoData
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SearchPage(viewModel: SearchViewModel = hiltViewModel(LocalContext.current as MainActivity)) {
    val context = LocalContext.current
    val searchText by viewModel.searchText.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val searchList = viewModel.searchList.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp, 10.dp, 20.dp, 0.dp),
            value = searchText,
            onValueChange = {
                viewModel.updateSearchText(it)
            },
        )
        DisposableEffect(searchText) {
            val job = snapshotFlow { searchText }
                .onEach { viewModel.showProgress(true) }
                .debounce(1000L)
                .onEach { viewModel.getSearchImage(it) }
                .launchIn(coroutineScope)
            onDispose { job.cancel() }
        }
        val progress by viewModel.progress.collectAsState()
        if (progress) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10.dp
                )
            }
            return
        }
        if (searchText.isEmpty()) {
            showNoData(text = context.getString(R.string.search_no_image))
            return
        }
        if (searchList.itemCount > 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            ) {
                items(searchList.itemCount) { index ->
                    searchList[index]?.let { item ->
                        val selected = rememberSaveable { mutableStateOf(false) }
                        selected.value = item.bookmark
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            CoilImage(
                                imageModel = item.imageUrl,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .width((LocalConfiguration.current.screenWidthDp / 5).dp)
                                    .heightIn(
                                        0.dp,
                                        (LocalConfiguration.current.screenHeightDp / 10).dp
                                    )
                            )
                            Text(
                                modifier = Modifier.padding(10.dp, 15.dp),
                                text = context.getString(
                                    R.string.search_item_text,
                                    item.collection,
                                    getDateTime(item.dateTime, context),
                                    "${item.width} x ${item.height}",
                                    item.displaySiteName
                                ),
                                fontSize = 16.sp
                            )

                            val drawableResource =
                                if (selected.value) R.drawable.icon_hearts_selected else if (isSystemInDarkTheme()) R.drawable.icon_hearts_dark_unselected else R.drawable.icon_hearts_unselected
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        if (selected.value) viewModel.deleteBookMark(
                                            searchText,
                                            arrayListOf(item.imageUrl)
                                        )
                                        else viewModel.insertBookMark(searchText, item)
                                        selected.value = !item.bookmark
                                        item.bookmark = !item.bookmark
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Image(
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(64.dp)
                                        .padding(end = 10.dp),
                                    painter = painterResource(id = drawableResource),
                                    contentDescription = "book_mark"
                                )
                            }
                        }
                    }
                }
            }
        } else {
            showNoData(text = context.getString(R.string.search_no_image))
            return
        }
    }
}