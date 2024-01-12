package com.app.opticool.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.opticool.R
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.ui.common.EyeglassesState
import com.app.opticool.ui.components.EyeglassItem
import com.app.opticool.ui.components.FeatureBanner
import com.app.opticool.ui.components.LoadingScreen
import com.app.opticool.ui.components.SearchBanner
import com.app.opticool.ui.theme.interFontFamily

@Composable
fun HomeScreen(
    uiState: EyeglassesState,
    retryAction: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    uiState.let { state ->
        when (state) {
            is EyeglassesState.Loading -> LoadingScreen()

            is EyeglassesState.Success -> HomeContent(
                eyeglasses = state.eyeglass,
                modifier = modifier.fillMaxWidth(),
                navigateToDetail = navigateToDetail,
                navigateToSearch = navigateToSearch
            )

            is EyeglassesState.Error -> ErrorScreen(
                message = state.error,
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = message, modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("retry")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    eyeglasses: List<EyeglassesResponseItem>,
    navigateToDetail: (Int) -> Unit,
    navigateToSearch: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 5.dp)
                    .background(color = Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hello, Coolers!",
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )
                Image(
                    painter = painterResource(id = R.drawable.logo_opticool),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(39.dp)
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                FeatureBanner(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "Rekomendasi Untukmu",
                    fontFamily = interFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .background(Color(0xFFF5F5F5))
                ) {
                    items(items = eyeglasses, key = { it.idEyeglass }) {
                        EyeglassItem(
                            name = it.name,
                            price = it.price,
                            image = it.linkPic1,
                            modifier = Modifier
                                .width(190.dp)
                                .clickable {
                                    navigateToDetail(it.idEyeglass)
                                }
                        )
                    }
                }
                SearchBanner(
                    modifier = Modifier
                        .clickable {
                            navigateToSearch()
                        }
                )
                Text(
                    text = "Berdasarkan bentuk wajah",
                    fontFamily = interFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}