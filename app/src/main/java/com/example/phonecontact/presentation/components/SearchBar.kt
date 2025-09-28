package com.example.phonecontact.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonecontact.R
import com.example.phonecontact.domain.model.SearchHistory
import com.example.phonecontact.ui.theme.*
import com.example.phonecontact.utils.Constants

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_placeholder),
    searchHistory: List<SearchHistory> = emptyList(),
    onHistoryItemClick: (String) -> Unit = {},
    onClearHistory: () -> Unit = {},
    modifier: Modifier = Modifier,
    onFocusChange: (Boolean) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val showHistory = isFocused && query.isEmpty() && searchHistory.isNotEmpty()

    Column(
        modifier = modifier.animateContentSize()
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = Constants.TEXT_SIZE_MEDIUM.sp,
                color = TextPrimary
            ),
            cursorBrush = SolidColor(Blue),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SurfaceWhite,
                            shape = RoundedCornerShape(Dimensions.cornerRadiusMedium)
                        )
                        .padding(
                            horizontal = Dimensions.paddingMedium,
                            vertical = Dimensions.paddingMedium - 2.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(Dimensions.iconDefault),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                    Box(modifier = Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontSize = Constants.TEXT_SIZE_MEDIUM.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                        innerTextField()
                    }

                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onQueryChange("")
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.size(Dimensions.iconDefault)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search),
                                modifier = Modifier.size(Dimensions.iconSmall),
                                tint = TextSecondary
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    onFocusChange(focusState.isFocused)
                }
        )

        if (showHistory) {
            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.elevationSmall),
                shape = RoundedCornerShape(Dimensions.cornerRadiusMedium)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = Dimensions.paddingMedium,
                                vertical = Dimensions.paddingSmall
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recent_searches),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        TextButton(
                            onClick = onClearHistory,
                            contentPadding = PaddingValues(
                                horizontal = Dimensions.spacingSmall,
                                vertical = Dimensions.paddingSmall
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.clear_history),
                                style = MaterialTheme.typography.labelSmall,
                                color = Blue
                            )
                        }
                    }

                    DividerLine(modifier = Modifier.fillMaxWidth())

                    LazyColumn(
                        modifier = Modifier.heightIn(max = Dimensions.searchHistoryMaxHeight)
                    ) {
                        items(
                            items = searchHistory.take(Constants.MAX_SEARCH_HISTORY_ITEMS),
                            key = { it.id }
                        ) { historyItem ->
                            SearchHistoryItem(
                                searchHistory = historyItem,
                                onClick = {
                                    onHistoryItemClick(historyItem.searchQuery)
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHistoryItem(
    searchHistory: SearchHistory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = Dimensions.paddingMedium,
                vertical = Dimensions.paddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(Dimensions.iconMedium),
            tint = TextSecondary
        )

        Spacer(modifier = Modifier.width(Dimensions.spacingMedium))

        Text(
            text = searchHistory.searchQuery,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}