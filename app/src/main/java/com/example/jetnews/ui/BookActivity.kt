package com.example.jetnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.jetnews.JetnewsApplication
import com.example.jetnews.presentation.ui.BookSearchApp

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appLocator = (application as JetnewsApplication).locator
        setContent {
            BookSearchApp(appLocator)
        }
    }
}