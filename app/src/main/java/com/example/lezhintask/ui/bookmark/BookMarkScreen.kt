package com.example.lezhintask.ui.bookmark

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lezhintask.R
import com.example.lezhintask.ui.main.MainActivity
import com.example.lezhintask.ui.main.getDateTime
import com.example.lezhintask.ui.main.showNoData
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun BookMarkPage(viewModel: BookMarkViewModel = hiltViewModel(LocalContext.current as MainActivity)) {
    val context = LocalContext.current
    val bookMarkText by viewModel.bookMarkText.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val editBookMark = rememberSaveable { mutableStateOf(false) }
    val deleteCheckBookMark = rememberSaveable { mutableListOf<String>() }
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp, 10.dp, 20.dp, 0.dp),
            value = bookMarkText,
            onValueChange = {
                viewModel.updateBookMarkText(it)
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp)
                    .clickable {
                        if (editBookMark.value) {
                            viewModel.deleteBookMark(bookMarkText, deleteCheckBookMark)
                            editBookMark.value = false
                        } else editBookMark.value = true
                    },
                text = if (editBookMark.value) context.getString(R.string.confirm)
                else context.getString(R.string.edit)
            )
            if (editBookMark.value) {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, end = 20.dp)
                        .clickable {
                            editBookMark.value = false
                        }, text = context.getString(R.string.cancel)
                )
            }
        }
        DisposableEffect(bookMarkText) {
            val job = snapshotFlow { bookMarkText }
                .onEach { viewModel.showProgress(true) }
                .debounce(1000L)
                .onEach {
                    viewModel.getBookMarkList(it)
                }
                .launchIn(coroutineScope)
            onDispose {
                job.cancel()
            }
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
        if (viewModel.bookMarkListState.isEmpty()) {
            showNoData(text = context.getString(R.string.bookmark_no_image))
            return
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            itemsIndexed(viewModel.bookMarkListState) { index, item ->
                val check = remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (editBookMark.value) {
                                check.value = !check.value
                                if (check.value) {
                                    deleteCheckBookMark.add(item.imageUrl)
                                } else {
                                    deleteCheckBookMark.remove(item.imageUrl)
                                }
                            }
                        }
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    if (editBookMark.value) {
                        Checkbox(
                            checked = check.value,
                            onCheckedChange = {
                                if (it) {
                                    deleteCheckBookMark.add(item.imageUrl)
                                } else {
                                    deleteCheckBookMark.remove(item.imageUrl)
                                }
                                check.value = it
                            }
                        )
                    } else {
                        check.value = false
                    }
                    CoilImage(
                        imageModel = item.imageUrl,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width((LocalConfiguration.current.screenWidthDp / 5).dp)
                            .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp / 10).dp)
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
                }
            }
        }
    }
}