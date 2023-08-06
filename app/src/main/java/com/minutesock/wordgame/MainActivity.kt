package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.minutesock.wordgame.presentation.DailyWordScreen
import com.minutesock.wordgame.ui.theme.WordGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordGameTheme {
                DailyWordScreen()
            }
        }
    }
}



