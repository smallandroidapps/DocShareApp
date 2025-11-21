package com.docshare.docshare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.docshare.docshare.ui.screens.ContactDetailsScreen
import com.docshare.docshare.ui.screens.ContactsListScreen
import com.docshare.docshare.ui.screens.DocumentRequestScreen
import com.docshare.docshare.ui.screens.HomeScreen
import com.docshare.docshare.ui.screens.LoginScreen
import com.docshare.docshare.ui.screens.PremiumPlansScreen
import com.docshare.docshare.ui.screens.RequestDetailScreen
import com.docshare.docshare.ui.screens.RequestsHistoryScreen
import com.docshare.docshare.data.local.LocalDataProvider

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CONTACTS = "contacts"
    const val SEARCH = "search"
    const val CONTACT_DETAILS = "contact_details/{contactId}"
    const val DOCUMENT_REQUEST = "document_request/{contactId}"
    const val REQUESTS_HISTORY = "requests_history/{contactId}"
    const val REQUEST_DETAIL = "request_detail/{requestId}"
    const val PREMIUM = "premium"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onGoogleSignIn = { /* stub */ },
                onSkip = { navController.navigate(Routes.HOME) }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onOpenContacts = { navController.navigate(Routes.CONTACTS) },
                onOpenRequests = {
                    // For demo, pick current user id and navigate to history
                    val currentUserId = LocalDataProvider.currentUser.id
                    navController.navigate("${Routes.REQUESTS_HISTORY.removeSuffix("/{contactId}")}/$currentUserId")
                },
                onOpenSearch = { navController.navigate(Routes.SEARCH) },
                onOpenPremium = { navController.navigate(Routes.PREMIUM) }
            )
        }
        composable(Routes.CONTACTS) {
            ContactsListScreen(
                contacts = LocalDataProvider.contacts,
                onContactClick = { contactId ->
                    navController.navigate("${Routes.CONTACT_DETAILS.removeSuffix("/{contactId}")}/$contactId")
                }
            )
        }
        composable(
            route = Routes.CONTACT_DETAILS,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            val contact = LocalDataProvider.getContactById(contactId)
            ContactDetailsScreen(
                contact = contact,
                onRequestDocs = {
                    navController.navigate("${Routes.DOCUMENT_REQUEST.removeSuffix("/{contactId}")}/$contactId")
                },
                onViewHistory = {
                    navController.navigate("${Routes.REQUESTS_HISTORY.removeSuffix("/{contactId}")}/$contactId")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DOCUMENT_REQUEST,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            DocumentRequestScreen(
                documentTypes = LocalDataProvider.documentTypes,
                onNext = { /* collect selection, stub */ },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.REQUESTS_HISTORY,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            RequestsHistoryScreen(
                requests = LocalDataProvider.getRequestsForContact(contactId),
                onOpenDetail = { requestId ->
                    navController.navigate("${Routes.REQUEST_DETAIL.removeSuffix("/{requestId}")}/$requestId")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.REQUEST_DETAIL,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: return@composable
            val request = LocalDataProvider.getRequestById(requestId)
            RequestDetailScreen(request = request, onBack = { navController.popBackStack() })
        }
        composable(Routes.PREMIUM) {
            PremiumPlansScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SEARCH) {
            com.docshare.docshare.ui.screens.SearchContactsScreen(
                contacts = LocalDataProvider.contacts,
                onContactClick = { contactId ->
                    navController.navigate("${Routes.CONTACT_DETAILS.removeSuffix("/{contactId}")}/$contactId")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
