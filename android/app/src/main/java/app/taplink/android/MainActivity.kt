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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val titles = listOf("Home", "Edit profile", "Share", "Settings")

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
                0 -> HomeScreen(onShare = { selectedTab = 2 }, onEdit = { selectedTab = 1 })
                1 -> EditProfileScreen()
                2 -> ShareScreen()
                else -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun HomeScreen(onShare: () -> Unit, onEdit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 24.dp),
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
                        Text("Your name", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("@yourusername", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text("Your public profile link", color = Muted, style = MaterialTheme.typography.labelMedium)
                Text("taplink.app/@yourusername", color = Color.White, fontWeight = FontWeight.Medium)
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
private fun EditProfileScreen() {
    PlaceholderScreen(
        title = "Build your profile",
        description = "Your name, username, bio, profile photo and social links will be managed here.",
        note = "Profile editing UI is the next implementation step."
    )
}

@Composable
private fun ShareScreen() {
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
