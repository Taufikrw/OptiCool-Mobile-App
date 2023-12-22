package com.app.opticool.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.ui.common.EyeglassesState
import com.app.opticool.ui.components.CustomSearchBar
import com.app.opticool.ui.components.EyeglassItem
import com.app.opticool.ui.components.LoadingScreen

@Composable
fun SearchScreen(
    uiState: EyeglassesState,
    retryAction: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    query: String,
    onChangeQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is EyeglassesState.Loading -> LoadingScreen()
        is EyeglassesState.Success -> SearchContent(
            eyeglasses = uiState.eyeglass,
            modifier = modifier.fillMaxWidth(),
            navigateToDetail = navigateToDetail,
            query = query,
            onChangeQuery = onChangeQuery
        )
        is EyeglassesState.Error -> ErrorScreen(
            message = uiState.error,
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    eyeglasses: List<EyeglassesResponseItem>,
    navigateToDetail: (Int) -> Unit,
    query: String,
    onChangeQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomSearchBar(query = query, onChangeQuery = onChangeQuery)
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
        ) {
            items(eyeglasses, key = { it.idEyeglass }) {
                EyeglassItem(
                    name = it.name,
                    price = it.price,
                    image = it.linkPic1,
                    modifier = Modifier
                        .clickable {
                            navigateToDetail(it.idEyeglass)
                        }
                )
            }
        }
    }
}