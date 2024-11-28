package com.example.emvtagsdictionary.emvTagModule.view

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emvtagsdictionary.R
import com.example.emvtagsdictionary.emvTagModule.model.EMVTag
import com.example.emvtagsdictionary.emvTagModule.viewModel.EMVTagViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EMVTagListScreen(viewModel: EMVTagViewModel, current: Context) {
    val context = LocalContext.current
    val emvTagList by viewModel.emvTagList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val expandedTagIndex by viewModel.expandedTagIndex.collectAsState()

    val filteredList = remember(searchQuery, emvTagList) {
        emvTagList.filter { it.tag.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        viewModel.loadEMVTags(context, "EMVTagsList.json")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.emv_tags), style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            EMVTagSearchBar(
                query = searchQuery,
                onQueryChanged = viewModel::updateSearchQuery,
                onClearQuery = { viewModel.updateSearchQuery("") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filteredList) { emvTag ->
                    val index = filteredList.indexOf(emvTag)
                    EMVTagCard(
                        emvTag = emvTag,
                        isExpanded = index == expandedTagIndex,
                        onClick = { viewModel.updateExpandedIndex(index) }
                    )
                }
            }
        }
    }
}


@Composable
fun EMVTagCard(emvTag: EMVTag, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tag: ${emvTag.tag}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            AnimatedVisibility (isExpanded) {
                Column { // Ensure proper vertical stacking of the text elements
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text =  stringResource(id = R.string.length_Tag, emvTag.length), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.description ,emvTag.description), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.value_of_tag,emvTag.value), fontSize = 14.sp)
                }            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
//    TextField(
//        value = query,
//        onValueChange = onQueryChanged,
//        placeholder = { Text("Search by tag") },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        singleLine = true,
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
//        ),
//        shape = RoundedCornerShape(8.dp)
//    )
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EMVTagSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search by tag") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Search",
                    modifier = Modifier.clickable { onClearQuery() }
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    )
}
