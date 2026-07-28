package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun IslamicDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        // Drawer Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_1784865374201),
                contentDescription = "Drawer Banner",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_baytul_ilm_icon_1784999011685),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Baytul Ilm AI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Your Complete AI-Powered Islamic Library & Learning Platform",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main Navigation Section
        DrawerGroupHeader("Main Navigation")
        DrawerItem(
            label = "Home",
            icon = Icons.Default.Home,
            selected = currentRoute == Screen.Home.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Home.route)
            }
        )
        DrawerItem(
            label = "Student Dashboard",
            icon = Icons.Default.School,
            selected = currentRoute == Screen.StudentDashboard.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.StudentDashboard.route)
            }
        )
        DrawerItem(
            label = "All Darjat",
            icon = Icons.Default.Grade,
            selected = currentRoute == Screen.Darjat.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Darjat.route)
            }
        )
        DrawerItem(
            label = "Library",
            icon = Icons.Default.LocalLibrary,
            selected = currentRoute == Screen.Library.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Library.route)
            }
        )
        DrawerItem(
            label = "Subjects",
            icon = Icons.Default.Category,
            selected = currentRoute == Screen.Subjects.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Subjects.route)
            }
        )

        // Digital Madrasa Section
        DrawerGroupHeader("Digital Madrasa")
        DrawerItem(
            label = "Live Classes",
            icon = Icons.Default.School,
            selected = currentRoute == Screen.LiveClasses.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.LiveClasses.route)
            }
        )
        DrawerItem(
            label = "Video Courses",
            icon = Icons.Default.MenuBook,
            selected = currentRoute == Screen.VideoCourses.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.VideoCourses.route)
            }
        )
        DrawerItem(
            label = "Audio Daroos",
            icon = Icons.Default.Book,
            selected = currentRoute == Screen.AudioLectures.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.AudioLectures.route)
            }
        )
        DrawerItem(
            label = "Q&A Forum",
            icon = Icons.Default.FormatQuote,
            selected = currentRoute == Screen.DiscussionForum.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.DiscussionForum.route)
            }
        )
        DrawerItem(
            label = "Messages & Chat",
            icon = Icons.Default.FormatQuote,
            selected = currentRoute == Screen.DirectMessaging.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.DirectMessaging.route)
            }
        )
        DrawerItem(
            label = "Book Page OCR",
            icon = Icons.Default.AutoAwesome,
            selected = currentRoute == Screen.OcrAssistant.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.OcrAssistant.route)
            }
        )
        DrawerItem(
            label = "Parent Panel",
            icon = Icons.Default.Grade,
            selected = currentRoute == Screen.ParentDashboard.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.ParentDashboard.route)
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        // Personal & Saved Section
        DrawerGroupHeader("My Library")
        DrawerItem(
            label = "Bookmarks",
            icon = Icons.Default.Bookmark,
            selected = currentRoute == Screen.Bookmarks.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Bookmarks.route)
            }
        )
        DrawerItem(
            label = "Favorites",
            icon = Icons.Default.Favorite,
            selected = currentRoute == Screen.Favorites.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Favorites.route)
            }
        )
        DrawerItem(
            label = "Recent Reading",
            icon = Icons.Default.History,
            selected = currentRoute == Screen.Recent.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Recent.route)
            }
        )
        DrawerItem(
            label = "Offline Library",
            icon = Icons.Default.Download,
            selected = currentRoute == Screen.Offline.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Offline.route)
            }
        )
        DrawerItem(
            label = "Downloads Manager",
            icon = Icons.Default.Download,
            selected = currentRoute == Screen.Downloads.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Downloads.route)
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        // Islamic Tools & Daily Devotion Section
        DrawerGroupHeader("Islamic Utilities")
        DrawerItem(
            label = "Prayer Times",
            icon = Icons.Default.Schedule,
            selected = currentRoute == Screen.PrayerTimes.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.PrayerTimes.route)
            }
        )
        DrawerItem(
            label = "Qibla Direction",
            icon = Icons.Default.CompassCalibration,
            selected = currentRoute == Screen.Qibla.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Qibla.route)
            }
        )
        DrawerItem(
            label = "Tasbeeh Counter",
            icon = Icons.Default.Repeat,
            selected = currentRoute == Screen.Tasbeeh.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Tasbeeh.route)
            }
        )
        DrawerItem(
            label = "Hijri Calendar",
            icon = Icons.Default.Event,
            selected = currentRoute == Screen.HijriCalendar.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.HijriCalendar.route)
            }
        )
        DrawerItem(
            label = "AI Scholar Assistant",
            icon = Icons.Default.AutoAwesome,
            selected = currentRoute == Screen.AiAssistant.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.AiAssistant.route)
            }
        )
        DrawerItem(
            label = "AI Teacher System",
            icon = Icons.Default.School,
            selected = currentRoute == Screen.AiTeacher.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.AiTeacher.route)
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        // Administration Section
        DrawerGroupHeader("Administration & CMS")
        DrawerItem(
            label = "Admin Panel & CMS",
            icon = Icons.Default.AdminPanelSettings,
            selected = currentRoute == Screen.AdminDashboard.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.AdminDashboard.route)
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        // Settings & Info
        DrawerGroupHeader("App Settings")
        DrawerItem(
            label = "Settings",
            icon = Icons.Default.Settings,
            selected = currentRoute == Screen.Settings.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.Settings.route)
            }
        )
        DrawerItem(
            label = "About & Contact",
            icon = Icons.Default.Info,
            selected = currentRoute == Screen.About.route,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigate(Screen.About.route)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DrawerGroupHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = label, fontSize = 14.sp) },
        icon = { Icon(imageVector = icon, contentDescription = label) },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}
