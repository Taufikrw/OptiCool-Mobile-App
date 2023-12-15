package com.app.opticool.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.opticool.R
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.ui.common.EyeglassState
import com.app.opticool.ui.components.LoadingScreen
import com.app.opticool.ui.theme.interFontFamily

@Composable
fun DetailScreen(
    uiState: EyeglassState,
    retryAction: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is EyeglassState.Loading -> LoadingScreen()
        is EyeglassState.Success -> DetailScreen(
            eyeglass = uiState.eyeglass,
            onBackClick = navigateBack,
            modifier = modifier.fillMaxWidth()
        )
        is EyeglassState.Error -> ErrorScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    eyeglass: EyeglassesResponseItem,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Detail Kacamata",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .shadow(elevation = 5.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
        ) {
            item {
                AsyncImage(
                    model = eyeglass.linkPic1,
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .height(136.dp)
                        .fillMaxWidth()
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                ) {
                    Text(
                        text = eyeglass.name,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(
                            R.string.match_shape, eyeglass.faceShape.capitalize(),
                        ),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = stringResource(
                            R.string.price_tag, eyeglass.price
                        ),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 36.sp
                    )
                }
            }
        }
    }
}