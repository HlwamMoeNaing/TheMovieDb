package com.homelandpay.themoviedb.appNav

object Routes {
    const val HOME_SCREEN = "home_screen"
    const val Favourite_SCREEN = "favourite_screen"
    const val DETAIL_SCREEN = "detail_screen/{itemId}"

    // Helper function to create route with arguments
    fun detailScreenWithArgs(itemId: Int): String {
        return "detail_screen/$itemId"
    }
}