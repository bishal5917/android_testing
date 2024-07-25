package com.example.android_testing.data.remote.response

data class ImageResponse(
    val hits : List<ImageResult>,
    val total : Int,
    val totalHits : Int
)

data class ImageResult(
    val previewURL : String
)
