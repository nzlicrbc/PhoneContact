package com.example.phonecontact.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.phonecontact.presentation.contacts.ContactsScreen
import com.example.phonecontact.presentation.addcontact.AddContactScreen
import com.example.phonecontact.presentation.editcontact.EditContactScreen
import com.example.phonecontact.presentation.profile.ContactProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Contacts.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Contacts.route) {
            ContactsScreen(
                onNavigateToAddContact = {
                    navController.navigate(Screen.AddContact.route)
                },
                onNavigateToProfile = { contactId ->
                    navController.navigate(Screen.ContactProfile.createRoute(contactId))
                }
            )
        }

        composable(route = Screen.AddContact.route) {
            AddContactScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditContact.route,
            arguments = listOf(
                navArgument(NavigationArgs.CONTACT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString(NavigationArgs.CONTACT_ID) ?: ""
            EditContactScreen(
                contactId = contactId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactUpdated = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ContactProfile.route,
            arguments = listOf(
                navArgument(NavigationArgs.CONTACT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString(NavigationArgs.CONTACT_ID) ?: ""
            ContactProfileScreen(
                contactId = contactId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = {
                    navController.navigate(Screen.EditContact.createRoute(contactId))
                }
            )
        }
    }
}