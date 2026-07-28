package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val subject: String,
    val darja: String, // e.g. "Darja Ula", "Darja Sania", "Darja Salisa", "Darja Rabia"
    val language: String, // "Arabic", "Urdu", "Arabic/Urdu"
    val type: String, // "Main Book", "Shurooh", "Translation", "PDF"
    val description: String,
    val coverResName: String = "img_hero_banner",
    val pdfUrl: String = "",
    val coverUrl: String = "",
    val pageCount: Int = 120,
    val lastReadPage: Int = 1,
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val rating: Float = 4.8f,
    val year: String = "1445 AH"
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookTitle: String,
    val pageNumber: Int,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "recent_readings")
data class RecentReadingEntity(
    @PrimaryKey val bookId: String,
    val bookTitle: String,
    val author: String,
    val pageNumber: Int,
    val totalPages: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbeeh_records")
data class TasbeehRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dhikrName: String,
    val count: Int,
    val target: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class PrayerTimeItem(
    val name: String,
    val time: String,
    val arabicName: String,
    val isNext: Boolean = false,
    val iconName: String = "wb_sunny"
)

data class DailyHadith(
    val textArabic: String,
    val textTranslation: String,
    val reference: String,
    val narrator: String
)

data class DailyAyah(
    val surahName: String,
    val verseNumber: Int,
    val textArabic: String,
    val textTranslation: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val category: String
)
