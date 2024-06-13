package com.example.lezhintask.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.lezhintask.R
import com.example.lezhintask.navigation.BottomNavItem
import com.example.lezhintask.navigation.NavigationGraph
import com.example.lezhintask.ui.theme.LezhinTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LezhinTaskTheme {
                MainView()
            }
        }
    }
}

@Composable
fun MainView() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navTile = rememberSaveable { mutableStateOf(R.string.search) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Red)
                    .padding(20.dp),
            ) {
                Text(
                    text = context.getString(navTile.value),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NavigationBarItem(
                        modifier = Modifier
                            .width((LocalConfiguration.current.screenWidthDp / 2).dp)
                            .fillMaxHeight(),
                        selected = false,
                        onClick = {
                            navTile.value = R.string.search
                            navController.navigate(BottomNavItem.Search.screenRoute) {
                                popUpTo(BottomNavItem.BookMark.screenRoute) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text(
                                text = context.getString(R.string.search),
                                fontSize = 14.sp
                            )
                        })
                    NavigationBarItem(
                        modifier = Modifier
                            .width((LocalConfiguration.current.screenWidthDp / 2).dp)
                            .fillMaxHeight(),
                        selected = false,
                        onClick = {
                            navTile.value = R.string.bookmark
                            navController.navigate(BottomNavItem.BookMark.screenRoute) {
                                popUpTo(BottomNavItem.Search.screenRoute) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text(
                                text = context.getString(R.string.bookmark),
                                fontSize = 14.sp
                            )
                        })
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavigationGraph(navController = navController)
        }
    }
}

fun getDateTime(dateTime: String, context: Context): String {
    val localDateTime =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateTime)
            ?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    if (Locale.getDefault().language == "ko") {
        return localDateTime?.let {
            context.getString(
                R.string.search_item_date_text_ko,
                it.year,
                it.monthValue,
                it.dayOfMonth,
                it.hour,
                it.minute
            )
        } ?: context.getString(R.string.search_item_date_error)
    } else {
        return localDateTime?.let {
            context.getString(
                R.string.search_item_date_text_en,
                it.month,
                it.dayOfMonth,
                it.year,
                it.hour,
                it.minute
            )
        } ?: context.getString(R.string.search_item_date_error)
    }
}

@Composable
fun showNoData(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
    }
}

@Composable
fun showProgress() {
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
}

@Preview(showBackground = true)
@Composable
fun MainPreView() {
    MainView()
}