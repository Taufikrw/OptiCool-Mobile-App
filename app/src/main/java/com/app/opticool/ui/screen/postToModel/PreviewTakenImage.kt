package com.app.opticool.ui.screen.postToModel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.opticool.R
import com.app.opticool.helper.UiState
import com.app.opticool.helper.compressImage
import com.app.opticool.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.log

@Composable
fun PreviewTakenImage(
    postToModelViewModel: PostToModelViewModel,
    navController: NavController,
    navigateToFacePredictions: (String) -> Unit,
    photo: File?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCancel: () -> Unit
){
    var idFace by rememberSaveable { mutableStateOf("")    }

    // Compress the image file
    val compressedPhoto: File? by remember(photo) {
        mutableStateOf(compressImage(photo))
    }

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.your_face_preview),
                    modifier = Modifier.padding(start = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (photo != null) {
                val photoUri = Uri.fromFile(photo)
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = stringResource(R.string.pti_note),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .clip(RoundedCornerShape(16.dp)),

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.pti_note),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(50.dp), onClick = onCancel
                ) {
                    Text(stringResource(R.string.btn_cancel))
                }
                Button(modifier = Modifier
                    .height(42.dp),
                    shape = RoundedCornerShape(50.dp), onClick = {
                        if (compressedPhoto != null) {
                            postToModelViewModel.uploadImage(compressedPhoto!!)
                        }
                    }) {
                    Text(stringResource(R.string.pti_continue), textAlign = TextAlign.Justify)
                }
            }
        }
    }

    val face = postToModelViewModel.face.observeAsState().value?.prediction
    if (face != null) {
        LaunchedEffect(key1 = face) {
            navigateToFacePredictions(face)
        }
    }

    val scope = rememberCoroutineScope()
    postToModelViewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UiState.Idle -> {}

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                Log.d("SUKSES", "PreviewTakenImage: ${uiState.data.prediction}")
                Toast.makeText(context, stringResource(id = R.string.pti_success), Toast.LENGTH_SHORT).show()
                LaunchedEffect(uiState) {
                    scope.launch {
                        navController.navigate(Screen.FacePredictions.route)
                    }
                }
            }

            is UiState.Error -> {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}



