package app.taplink.android

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        TapLinkBackend.client.from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeList<TapLinkProfile>()
            .firstOrNull()

    suspend fun save(profile: TapLinkProfile) {
        TapLinkBackend.client.from("profiles").upsert(profile) {
            onConflict = "id"
        }
    }
}
