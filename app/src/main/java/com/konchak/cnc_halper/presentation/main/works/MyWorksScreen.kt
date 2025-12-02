package com.konchak.cnc_halper.presentation.main.works

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.presentation.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyWorksScreen(
    navController: NavController,
    viewModel: MyWorksViewModel = hiltViewModel()
) {
    val works by viewModel.works.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredWorks = works.filter { it.name.contains(searchQuery, ignoreCase = true) }
    val pagerState = rememberPagerState(pageCount = { filteredWorks.size })

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddWork.route)
            }) {
                Icon(Icons.Filled.Add, "Добавить новую работу")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Поиск работ") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
            if (filteredWorks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Работы не найдены.")
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 64.dp),
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    val scale by animateFloatAsState(
                        targetValue = 1f - pageOffset.absoluteValue * 0.25f,
                        label = "scale"
                    )
                    val alpha by animateFloatAsState(
                        targetValue = 1f - pageOffset.absoluteValue * 0.5f,
                        label = "alpha"
                    )

                    FolderItem(
                        work = filteredWorks[page],
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            }
                            .aspectRatio(0.7f),
                        onClick = {
                            navController.navigate("work_detail/${filteredWorks[page].id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FolderItem(work: Work, modifier: Modifier = Modifier, onClick: (Work) -> Unit) {
    Card(
        modifier = modifier.clickable { onClick(work) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = work.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Изменено: ${formatDate(work.endDate ?: work.startDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
