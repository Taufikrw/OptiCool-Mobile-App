package com.app.opticool.ui.screen.facePredictions

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.app.opticool.ui.components.PredictionsBanner
import com.app.opticool.ui.components.SearchBanner
import com.app.opticool.ui.screen.ErrorScreen
import com.app.opticool.ui.screen.HomeContent
import com.app.opticool.ui.screen.RecommendViewModel
import com.app.opticool.ui.theme.interFontFamily

@Composable
fun FacePredictionsScreen(
    uiState: EyeglassesState,
    retryAction: (String) -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    when (uiState) {
        is EyeglassesState.Loading -> LoadingScreen()
        is EyeglassesState.Success -> FacePredictionsContent(
            eyeglasses = uiState.eyeglass,
            modifier = modifier.fillMaxWidth(),
            navigateToDetail = navigateToDetail
        )
        is EyeglassesState.Error -> ErrorScreen(
            message = uiState.error,
            retryAction = { retryAction("heart") },
            modifier = modifier.fillMaxSize()
        )
    }

}

@Composable
fun ErrorScreen(
    message: String,
    retryAction: (String) -> Unit,
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
        Button(onClick = { retryAction("heart") }) {
            Text("retry")
        }
    }
}

@Composable
fun FacePredictionsEXP(
    faceShape: String
) {
    PredictionsBanner(faceShape = faceShape)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacePredictionsContent(
    modifier: Modifier = Modifier,
    eyeglasses: List<EyeglassesResponseItem>,
    navigateToDetail: (Int) -> Unit
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2) ,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
    ){
        items(items = eyeglasses, key = { it.idEyeglass}){
            EyeglassItem(
                name = it.name,
                price = it.price,
                image = it.linkPic1,
                modifier = Modifier.clickable {
                    navigateToDetail(it.idEyeglass)
                })
        }
    }
}

