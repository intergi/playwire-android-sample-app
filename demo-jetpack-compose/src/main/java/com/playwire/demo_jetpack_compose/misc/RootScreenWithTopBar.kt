package com.playwire.demo_jetpack_compose.misc

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.playwire.demo_jetpack_compose.R
import com.playwire.demo_jetpack_compose.ui.theme.PlaywireandroidsampleappTheme

@Composable
fun RootScreenWithTopBar(content: @Composable (modifier: Modifier) -> Unit) {
    PlaywireandroidsampleappTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
            },
            content = {
                content(Modifier.padding(it))
            }
        )
    }
}