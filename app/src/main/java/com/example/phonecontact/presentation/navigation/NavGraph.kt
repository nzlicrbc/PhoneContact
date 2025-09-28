package com.example.phonecontact.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.phonecontact.presentation.addcontact.NewContactScreen
import com.example.phonecontact.presentation.contacts.ContactsScreen
import com.example.phonecontact.presentation.profile.ProfileScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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
                },
                onNavigateToEditContact = { contactId ->
                    navController.navigate(Screen.EditContact.createRoute(contactId))
                }
            )
        }

        composable(route = Screen.AddContact.route) {
            NewContactScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navController.popBackStack(
                        route = Screen.Contacts.route,
                        inclusive = false
                    )
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
        ) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = { contactId ->
                    navController.navigate(Screen.EditContact.createRoute(contactId))
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
            val contactId = backStackEntry.arguments?.getString(NavigationArgs.CONTACT_ID) ?: return@composable

            NewContactScreen(
                contactId = contactId,
                isEditMode = true,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}