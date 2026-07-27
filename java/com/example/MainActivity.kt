package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.IslamicDrawerContent
import com.example.ui.navigation.Screen
import com.example.ui.screens.admin.AdminAnalyticsScreen
import com.example.ui.screens.admin.AdminBackupSettingsScreen
import com.example.ui.screens.admin.AdminBookManagementScreen
import com.example.ui.screens.admin.AdminCertificatesScreen
import com.example.ui.screens.admin.AdminContentManagementScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminPushNotificationsScreen
import com.example.ui.screens.admin.AdminQuizManagementScreen
import com.example.ui.screens.admin.AdminRoleManagementScreen
import com.example.ui.screens.admin.AdminSecurityScreen
import com.example.ui.screens.admin.AdminShuroohTranslationsScreen
import com.example.ui.screens.admin.AdminUserManagementScreen
import com.example.ui.screens.ai.AiAssistantScreen
import com.example.ui.screens.ai.AiTeacherScreen
import com.example.ui.screens.auth.EmailVerificationScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.book.BookDetailScreen
import com.example.ui.screens.book.BookViewerScreen
import com.example.ui.screens.bookmarks.BookmarksScreen
import com.example.ui.screens.darjat.DarjatScreen
import com.example.ui.screens.downloads.DownloadsScreen
import com.example.ui.screens.downloads.OfflineScreen
import com.example.ui.screens.favorites.FavoritesScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.library.BooksScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.prayer.HijriCalendarScreen
import com.example.ui.screens.prayer.PrayerTimesScreen
import com.example.ui.screens.prayer.QiblaScreen
import com.example.ui.screens.prayer.TasbeehScreen
import com.example.ui.screens.prayer.DailyDuaScreen
import com.example.ui.screens.prayer.DailyHadithScreen
import com.example.ui.screens.prayer.DailyQuranScreen
import com.example.ui.screens.lms.LmsAchievementsScreen
import com.example.ui.screens.lms.LmsAssignmentsScreen
import com.example.ui.screens.lms.LmsCertificatesScreen
import com.example.ui.screens.lms.LmsLeaderboardScreen
import com.example.ui.screens.lms.LmsNotesScreen
import com.example.ui.screens.lms.LmsProfileEditScreen
import com.example.ui.screens.lms.LmsStudyPlannerScreen
import com.example.ui.screens.lms.StudentDashboardScreen
import com.example.ui.screens.prayer.IslamicDateConverterScreen
import com.example.ui.screens.prayer.IslamicEventsScreen
import com.example.ui.screens.prayer.IslamicToolsScreen
import com.example.ui.screens.prayer.IslamicWallpapersScreen
import com.example.ui.screens.prayer.NamesOfAllahScreen
import com.example.ui.screens.prayer.NearbyMosquesScreen
import com.example.ui.screens.prayer.RamadanScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.quiz.QuizScreen
import com.example.ui.screens.recent.RecentScreen
import com.example.ui.screens.search.SearchScreen
import com.example.ui.screens.settings.AboutContactScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.subjects.SubjectsScreen
import com.example.ui.screens.madrasa.AdminBulkOpsScreen
import com.example.ui.screens.madrasa.AudioLecturesScreen
import com.example.ui.screens.madrasa.DirectMessagingScreen
import com.example.ui.screens.madrasa.DiscussionForumScreen
import com.example.ui.screens.madrasa.ExamResultsScreen
import com.example.ui.screens.madrasa.LiveClassesScreen
import com.example.ui.screens.madrasa.OcrAssistantScreen
import com.example.ui.screens.madrasa.ParentDashboardScreen
import com.example.ui.screens.madrasa.VideoCoursesScreen
import com.example.ui.theme.BaytulIlmTheme
import com.example.ui.theme.RasheedIslamicTheme
import com.example.ui.viewmodel.AiViewModel
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaytulIlmTheme {
                BaytulIlmApp()
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaytulIlmApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val mainViewModel: MainViewModel = viewModel()
    val aiViewModel: AiViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val bottomNavItems = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Library to Icons.Default.LocalLibrary,
        Screen.Offline to Icons.Default.DownloadDone,
        Screen.Quiz to Icons.Default.Quiz,
        Screen.AiAssistant to Icons.Default.AutoAwesome,
        Screen.Profile to Icons.Default.Person
    )

    val isTopLevelRoute = bottomNavItems.any { it.first.route == currentRoute }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            IslamicDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (isTopLevelRoute && currentRoute != Screen.AiAssistant.route) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Baytul Ilm AI",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Drawer Menu"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = androidx.compose.ui.graphics.Color.White,
                            navigationIconContentColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            },
            bottomBar = {
                if (isTopLevelRoute) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        bottomNavItems.forEach { (screen, icon) ->
                            val selected = currentRoute == screen.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(text = screen.title, fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Splash.route) {
                    val userState by authViewModel.userState.collectAsState()
                    androidx.compose.runtime.LaunchedEffect(userState) {
                        val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
                        val targetRoute = when {
                            currentUser != null && !currentUser.isEmailVerified -> Screen.EmailVerification.route
                            currentUser != null && currentUser.isEmailVerified -> Screen.Home.route
                            else -> Screen.Login.route
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }

                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                composable(Screen.EmailVerification.route) {
                    EmailVerificationScreen(
                        authViewModel = authViewModel,
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    val userState by authViewModel.userState.collectAsState()
                    val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()

                    androidx.compose.runtime.LaunchedEffect(currentUser, currentUser?.isEmailVerified) {
                        if (currentUser != null && !currentUser.isEmailVerified) {
                            navController.navigate(Screen.EmailVerification.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        } else if (currentUser == null) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }

                    if (currentUser != null && currentUser.isEmailVerified) {
                        HomeScreen(
                            viewModel = mainViewModel,
                            onNavigate = { route -> navController.navigate(route) }
                        )
                    }
                }

                composable(Screen.Library.route) {
                    LibraryScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Offline.route) {
                    OfflineScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Quiz.route) {
                    QuizScreen(
                        viewModel = quizViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.AiAssistant.route) {
                    AiAssistantScreen(aiViewModel = aiViewModel, mainViewModel = mainViewModel)
                }

                composable(Screen.AiTeacher.route) {
                    AiTeacherScreen(
                        aiViewModel = aiViewModel,
                        mainViewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        viewModel = mainViewModel,
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Login.route) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Register.route) {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onRegisterSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.ForgotPassword.route) {
                    ForgotPasswordScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Darjat.route) {
                    DarjatScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.DarjaDetail.route) { backStackEntry ->
                    val darjaName = backStackEntry.arguments?.getString("darjaName") ?: ""
                    BooksScreen(
                        darjaId = darjaName,
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Subjects.route) {
                    SubjectsScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.SubjectDetail.route) { backStackEntry ->
                    val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
                    LibraryScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.BookDetail.route) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    BookDetailScreen(
                        bookId = bookId,
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.BookViewer.route) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    BookViewerScreen(
                        bookId = bookId,
                        viewModel = mainViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Bookmarks.route) {
                    BookmarksScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Recent.route) {
                    RecentScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.PrayerTimes.route) {
                    PrayerTimesScreen(viewModel = mainViewModel)
                }

                composable(Screen.Qibla.route) {
                    QiblaScreen()
                }

                composable(Screen.Tasbeeh.route) {
                    TasbeehScreen(viewModel = mainViewModel)
                }

                composable(Screen.HijriCalendar.route) {
                    HijriCalendarScreen(viewModel = mainViewModel)
                }

                composable(Screen.IslamicTools.route) {
                    IslamicToolsScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Ramadan.route) {
                    RamadanScreen(viewModel = mainViewModel)
                }

                composable(Screen.DailyQuran.route) {
                    DailyQuranScreen(viewModel = mainViewModel)
                }

                composable(Screen.DailyHadith.route) {
                    DailyHadithScreen(viewModel = mainViewModel)
                }

                composable(Screen.DailyDua.route) {
                    DailyDuaScreen(viewModel = mainViewModel)
                }

                composable(Screen.NamesOfAllah.route) {
                    NamesOfAllahScreen(viewModel = mainViewModel)
                }

                composable(Screen.IslamicEvents.route) {
                    IslamicEventsScreen(viewModel = mainViewModel)
                }

                composable(Screen.NearbyMosques.route) {
                    NearbyMosquesScreen(viewModel = mainViewModel)
                }

                composable(Screen.DateConverter.route) {
                    IslamicDateConverterScreen(viewModel = mainViewModel)
                }

                composable(Screen.IslamicWallpapers.route) {
                    IslamicWallpapersScreen(viewModel = mainViewModel)
                }

                composable(Screen.Downloads.route) {
                    DownloadsScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen()
                }

                composable(Screen.About.route) {
                    AboutContactScreen()
                }

                // Student Dashboard & LMS Routes
                composable(Screen.StudentDashboard.route) {
                    StudentDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.LmsStudyPlanner.route) {
                    LmsStudyPlannerScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsNotes.route) {
                    LmsNotesScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsAssignments.route) {
                    LmsAssignmentsScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsCertificates.route) {
                    LmsCertificatesScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsAchievements.route) {
                    LmsAchievementsScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsLeaderboard.route) {
                    LmsLeaderboardScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsProfileEdit.route) {
                    LmsProfileEditScreen(
                        viewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Digital Madrasa Extended Routes
                composable(Screen.LiveClasses.route) {
                    LiveClassesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.VideoCourses.route) {
                    VideoCoursesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.AudioLectures.route) {
                    AudioLecturesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ExamResults.route) {
                    ExamResultsScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.DiscussionForum.route) {
                    DiscussionForumScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.DirectMessaging.route) {
                    DirectMessagingScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.OcrAssistant.route) {
                    OcrAssistantScreen(
                        aiViewModel = aiViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ParentDashboard.route) {
                    ParentDashboardScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.AdminBulkOps.route) {
                    AdminBulkOpsScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Admin & CMS Routes
                composable(Screen.AdminDashboard.route) {
                    AdminDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.AdminRoleManagement.route) {
                    AdminRoleManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminBookManagement.route) {
                    AdminBookManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminShuroohTranslations.route) {
                    AdminShuroohTranslationsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminQuizManagement.route) {
                    AdminQuizManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminContentManagement.route) {
                    AdminContentManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminPushNotifications.route) {
                    AdminPushNotificationsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminUserManagement.route) {
                    AdminUserManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminAnalytics.route) {
                    AdminAnalyticsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminCertificates.route) {
                    AdminCertificatesScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminBackupSettings.route) {
                    AdminBackupSettingsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminSecurity.route) {
                    AdminSecurityScreen(viewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun RasheedIslamicApp() {
    BaytulIlmApp()
}
