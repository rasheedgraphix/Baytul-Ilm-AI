package com.example.data.repository

import com.example.data.model.DailyAyah
import com.example.data.model.DailyHadith
import com.example.data.model.PrayerTimeItem

class PrayerRepository {

    fun getTodayPrayerTimes(): List<PrayerTimeItem> {
        return listOf(
            PrayerTimeItem("Fajr", "04:25 AM", "الفجر", isNext = false, iconName = "wb_twilight"),
            PrayerTimeItem("Sunrise", "05:48 AM", "الشروق", isNext = false, iconName = "wb_sunny"),
            PrayerTimeItem("Dhuhr", "12:15 PM", "الظهر", isNext = true, iconName = "light_mode"),
            PrayerTimeItem("Asr", "03:45 PM", "العصر", isNext = false, iconName = "sunny_snowing"),
            PrayerTimeItem("Maghrib", "06:40 PM", "المغرب", isNext = false, iconName = "nights_stay"),
            PrayerTimeItem("Isha", "08:05 PM", "العشاء", isNext = false, iconName = "bedtime")
        )
    }

    fun getDailyHadith(): DailyHadith {
        return DailyHadith(
            textArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            textTranslation = "Actions are judged by intentions, and every person will be rewarded according to what he intended.",
            reference = "Sahih al-Bukhari #1",
            narrator = "Narrated by Umar ibn al-Khattab (R.A)"
        )
    }

    fun getDailyAyah(): DailyAyah {
        return DailyAyah(
            surahName = "Surah Al-Baqarah",
            verseNumber = 152,
            textArabic = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
            textTranslation = "So remember Me; I will remember you. And be grateful to Me and do not deny Me."
        )
    }

    fun getHijriDate(): String {
        return "14 Safar 1448 AH"
    }
}
