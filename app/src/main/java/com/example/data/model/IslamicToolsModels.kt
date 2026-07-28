package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_history")
data class PrayerTrackerRecord(
    @PrimaryKey val date: String, // format "YYYY-MM-DD"
    val fajrDone: Boolean = false,
    val dhuhrDone: Boolean = false,
    val asrDone: Boolean = false,
    val maghribDone: Boolean = false,
    val ishaDone: Boolean = false,
    val totalDone: Int = 0
)

data class CityLocation(
    val cityName: String,
    val countryName: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)

data class PrayerTimeDetail(
    val name: String,
    val time: String, // e.g. "04:30 AM"
    val arabicName: String,
    val timestampSeconds: Long,
    val isNext: Boolean = false,
    val isPassed: Boolean = false,
    val iconName: String = "wb_sunny"
)

data class MonthlyPrayerRow(
    val dayOfMonth: Int,
    val dateString: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

data class DuaItem(
    val id: String,
    val title: String,
    val category: String, // "Morning", "Evening", "Travel", "Sleeping", "Eating", "Waking", "After Salah", "Protection"
    val arabic: String,
    val transliteration: String,
    val urdu: String,
    val english: String,
    val reference: String = "",
    val isBookmarked: Boolean = false
)

data class AllahName(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val englishMeaning: String,
    val urduMeaning: String,
    val explanation: String
)

data class IslamicEvent(
    val id: String,
    val nameEnglish: String,
    val nameArabic: String,
    val hijriDate: String,
    val gregorianDate: String,
    val description: String,
    val category: String = "Holiday"
)

data class NearbyMosque(
    val id: String,
    val name: String,
    val address: String,
    val distanceKm: Double,
    val rating: Float,
    val latitude: Double,
    val longitude: Double
)

data class IslamicWallpaper(
    val id: String,
    val title: String,
    val category: String, // "Calligraphy", "Mosque", "Kaaba", "Quran"
    val imageUrl: String,
    val isFavorite: Boolean = false
)

data class RamadanDay(
    val dayNumber: Int,
    val hijriDate: String,
    val gregorianDate: String,
    val sehriTime: String,
    val iftarTime: String,
    val isFastCompleted: Boolean = false
)

data class NotificationSettingsState(
    val notifyBeforeMinutes: Int = 10,
    val notifyAtPrayerTime: Boolean = true,
    val soundOption: String = "Adhan Makkah", // "Adhan Makkah", "Adhan Madinah", "Chime", "Silent"
    val silentMode: Boolean = false,
    val vibration: Boolean = true,
    val calculationMethod: String = "University of Islamic Sciences, Karachi"
)
