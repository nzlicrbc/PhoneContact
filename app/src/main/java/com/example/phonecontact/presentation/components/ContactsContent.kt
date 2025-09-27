package com.example.phonecontact.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.phonecontact.domain.model.Contact

@Composable
fun ContactsContent(
    groupedContacts: Map<Char, List<Contact>>,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        groupedContacts.forEach { (letter, contactsForLetter) ->
            item(key = "header_$letter") {
                SectionHeader(letter = letter.toString())
            }

            items(
                count = contactsForLetter.size,
                key = { index -> contactsForLetter[index].id }
            ) { index ->
                val contact = contactsForLetter[index]
                val isLast = index == contactsForLetter.size - 1

                ContactListItem(
                    contact = contact,
                    onClick = { onContactClick(contact) },
                    showDivider = !isLast,
                    isInDeviceContacts = contact.isInDeviceContacts
                )
            }
        }
    }
}