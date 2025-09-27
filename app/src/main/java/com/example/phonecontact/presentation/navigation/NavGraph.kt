package com.example.phonecontact.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.phonecontact.presentation.contacts.ContactsScreen

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
                }
            )
        }
    }
}