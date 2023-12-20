package jp.ac.it_college.std.s22007.android_step.sns

import android.content.Intent
import androidx.compose.ui.platform.LocalContext


val sendIntent: Intent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, "test")
    type = "text/plain"
}

val shereIntent = Intent.createChooser(sendIntent, null)