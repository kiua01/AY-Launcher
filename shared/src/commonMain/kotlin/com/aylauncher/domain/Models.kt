package com.aylauncher.domain

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val username: String,
    val type: AccountType,
    val uuid: String? = null,
    val accessToken: String? = null
)

@Serializable
enum class AccountType {
    OFFLINE,
    MICROSOFT
}

@Serializable
data class Instance(
    val id: String,
    val name: String,
    val minecraftVersion: String,
    val loader: LoaderType,
    val loaderVersion: String? = null,
    val ramMb: Int = 4096,
    val javaPath: String? = null,
    val gameDir: String? = null
)

@Serializable
enum class LoaderType {
    VANILLA,
    FABRIC,
    FORGE,
    NEOFORGE,
    QUILT,
    OPTIFINE
}

@Serializable
data class NewsItem(
    val title: String,
    val summary: String,
    val date: String,
    val imageUrl: String? = null
)
