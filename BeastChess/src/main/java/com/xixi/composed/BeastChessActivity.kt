package com.xixi.composed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xixi.composed.ui.theme.ComposeDTheme

class BeastChessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestState()
        }
    }
}

@Composable
fun Greeting(name: String = "test") {
    Column(
    
    ) {
        Text(text = "Hello $name!", 
            modifier = Modifier
                .absolutePadding(bottom = 12.dp)
                .background(Color.Blue)
        )
        Text(text = "Hello test $name!")
        
        Surface(shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "test surface", fontSize = 13.sp)
        }
    }
}

@Composable
@Preview(showBackground = false)
fun DefaultPreview() {
    ComposeDTheme {
        Greeting("Android")
    }
}