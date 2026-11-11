package app.taplink.android

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.eq
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class TapLinkProfile(
    val id: String,
    val username: String,
    @SerialName("display_name") val displayName: String = "",
    val bio: String = "",
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("instagram_url") val instagramUrl: String = "",
    @SerialName("youtube_url") val youtubeUrl: String = "",
    @SerialName("website_url") val websiteUrl: String = "",
    @SerialName("is_published") val isPublished: Boolean = false
)

object ProfileRepository {
    suspend fun load(userId: String): TapLinkProfile? =
        TapLinkBackend.client.postgrest["profiles"]
            .select {
                eq("id", userId)
            }
            .decodeList<TapLinkProfile>()
            .firstOrNull()

    suspend fun save(profile: TapLinkProfile) {
        val payload = JsonArray(listOf(Json.encodeToJsonElement(profile)))
        TapLinkBackend.client.postgrest["profiles"].insert(
            payload,
            upsert = true,
            onConflict = "id"
        )
    }
}
