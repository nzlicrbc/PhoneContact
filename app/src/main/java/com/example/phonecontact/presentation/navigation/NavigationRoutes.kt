package com.example.phonecontact.presentation.navigation

sealed class Screen(val route: String) {
    data object Contacts : Screen("contacts")
    data object AddContact : Screen("add_contact")
    data object EditContact : Screen("edit_contact/{contactId}") {
        fun createRoute(contactId: String) = "edit_contact/$contactId"
    }
    data object ContactProfile : Screen("contact_profile/{contactId}") {
        fun createRoute(contactId: String) = "contact_profile/$contactId"
    }
}

object NavigationArgs {
    const val CONTACT_ID = "contactId"
}