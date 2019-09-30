package net.nicbell.dogbreeds.api

data class ApiResponse<T>(val message: T, val status: String)