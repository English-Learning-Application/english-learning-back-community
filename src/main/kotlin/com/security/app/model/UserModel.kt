package com.security.app.model

import java.util.*

class UserModel {
    var userId: String = ""

    var email: String = ""

    var username: String = ""

    var media: MediaModel? = null

    var googleId: String? = null

    var facebookId: String? = null

    var phoneNumber: String? = null
}

class MediaModel {
    var mediaId: String = ""

    var mediaType: String = ""

    var mediaUrl: String = ""
}