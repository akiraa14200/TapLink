package app.taplink.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import app.taplink.android.ui.theme.TapLinkTheme

private val Ink = Color(0xFF10131A)
private val Muted = Color(0xFF8D96A8)
private val Accent = Color(0xFF83A7FF)
private val Panel = Color(0xFF1A1F2A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TapLinkTheme {
                TapLinkApp()
            }
        }
    }
}

@Composable
private fun TapLinkApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var youtube by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Ink,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                val items = listOf(
                    Triple("Home", Icons.Outlined.Home, 0),
                    Triple("Profile", Icons.Outlined.Person, 1),
                    Triple("Share", Icons.Outlined.QrCode2, 2),
                    Triple("Settings", Icons.Outlined.Settings, 3)
                )
                items.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(padding),
            color = Ink
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    displayName = displayName,
                    username = username,
                    onShare = { selectedTab = 2 },
                    onEdit = { selectedTab = 1 }
                )
                1 -> EditProfileScreen(
                    displayName = displayName,
                    onDisplayNameChange = { displayName = it },
                    username = username,
                    onUsernameChange = {
                        username = it.lowercase().filter { char ->
                            char.isLetterOrDigit() || char == '.' || char == '_'
                        }
                    },
                    bio = bio,
                    onBioChange = { bio = it },
                    instagram = instagram,
                    onInstagramChange = { instagram = it },
                    youtube = youtube,
                    onYoutubeChange = { youtube = it },
                    website = website,
                    onWebsiteChange = { website = it }
                )
                2 -> ShareScreen(username = username)
                else -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun HomeScreen(
    displayName: String,
    username: String,
    onShare: () -> Unit,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("TAPLINK", color = Accent, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text("Your identity.\nOne touch away.", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text("Your profile, ready to share anywhere.", color = Muted, style = MaterialTheme.typography.bodyMedium)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Panel)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier.size(58.dp).clip(CircleShape).background(Color(0xFF2C3C5C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = Accent, modifier = Modifier.size(30.dp))
                    }
                    Column {
                        Text(displayName.ifBlank { "Your name" }, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("@${username.ifBlank { "yourusername" }}", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text("Your public profile link", color = Muted, style = MaterialTheme.typography.labelMedium)
                Text("taplink.app/@${username.ifBlank { "yourusername" }}", color = Color.White, fontWeight = FontWeight.Medium)
                Button(
                    onClick = onShare,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Ink)
                ) {
                    Icon(Icons.Outlined.QrCode2, contentDescription = null)
                    Text("  Share my TapLink")
                }
                OutlinedButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                    Text("Edit profile")
                }
            }
        }

        Text("GET STARTED", color = Muted, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        SetupStep("1", "Create your profile", "Add your name, photo and links.")
        SetupStep("2", "Connect an NFC tag", "Let someone tap the tag to open your page.")
        SetupStep("3", "Share anywhere", "Use your QR code whenever NFC is unavailable.")
    }
}

@Composable
private fun SetupStep(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier.size(34.dp).clip(CircleShape).background(Color(0xFF29344A)),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = Accent, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(description, color = Muted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun EditProfileScreen(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    instagram: String,
    onInstagramChange: (String) -> Unit,
    youtube: String,
    onYoutubeChange: (String) -> Unit,
    website: String,
    onWebsiteChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Edit your profile", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Create the public identity people will see when they open your TapLink.", color = Muted)

        ProfileField("Display name", displayName, onDisplayNameChange, "How should people know you?")
        ProfileField(
            "Username",
            username,
            onUsernameChange,
            "yourname",
            prefix = "@",
            supporting = "Use letters, numbers, dots or underscores."
        )
        ProfileField("Bio", bio, onBioChange, "A short introduction about you.", singleLine = false)
        Text("SOCIAL LINKS", color = Muted, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        ProfileField("Instagram", instagram, onInstagramChange, "https://instagram.com/username")
        ProfileField("YouTube", youtube, onYoutubeChange, "https://youtube.com/@channel")
        ProfileField("Website", website, onWebsiteChange, "https://yourwebsite.com")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Panel)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("PROFILE PREVIEW", color = Accent, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(displayName.ifBlank { "Your display name" }, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text("@${username.ifBlank { "yourusername" }}", color = Muted)
                Text(bio.ifBlank { "Your bio will appear here." }, color = Color.White)
                val links = listOf(
                    "Instagram" to instagram,
                    "YouTube" to youtube,
                    "Website" to website
                ).filter { it.second.isNotBlank() }
                links.forEach { (label, _) ->
                    Text(label, color = Accent, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Text(
            "This is a live preview only. Your profile will be saved online after account and database setup.",
            color = Muted,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    prefix: String? = null,
    supporting: String? = null,
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Muted) },
            prefix = prefix?.let { { Text(it, color = Accent) } },
            supportingText = supporting?.let { { Text(it, color = Muted) } },
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun ShareScreen(username: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(190.dp).clip(RoundedCornerShape(28.dp)).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.QrCode2, contentDescription = "QR code placeholder", tint = Ink, modifier = Modifier.size(112.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Share your profile", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("taplink.app/@${username.ifBlank { "yourusername" }}", color = Muted)
        Spacer(Modifier.height(8.dp))
        Text("Your QR code and NFC tag controls will appear here.", color = Muted)
        Spacer(Modifier.height(18.dp))
        Button(onClick = {}, enabled = false) { Text("Generate QR code — coming next") }
    }
}

@Composable
private fun SettingsScreen() {
    PlaceholderScreen(
        title = "Settings",
        description = "Manage account, privacy, appearance and connected NFC tags.",
        note = "Settings will be connected after the profile foundation."
    )
}

@Composable
private fun PlaceholderScreen(title: String, description: String, note: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(description, color = Muted)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp)) {
            Text(note, modifier = Modifier.padding(18.dp), color = Muted)
        }
    }
}
