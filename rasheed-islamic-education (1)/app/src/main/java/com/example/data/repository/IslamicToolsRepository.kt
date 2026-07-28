package com.example.data.repository

import com.example.data.model.AllahName
import com.example.data.model.CityLocation
import com.example.data.model.DailyAyah
import com.example.data.model.DailyHadith
import com.example.data.model.DuaItem
import com.example.data.model.IslamicEvent
import com.example.data.model.IslamicWallpaper
import com.example.data.model.MonthlyPrayerRow
import com.example.data.model.NearbyMosque
import com.example.data.model.NotificationSettingsState
import com.example.data.model.PrayerTimeDetail
import com.example.data.model.PrayerTrackerRecord
import com.example.data.model.RamadanDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class IslamicToolsRepository {

    // Available Cities
    val cities = listOf(
        CityLocation("Lahore", "Pakistan", 31.5204, 74.3587, "GMT+5"),
        CityLocation("Karachi", "Pakistan", 24.8607, 67.0011, "GMT+5"),
        CityLocation("Islamabad", "Pakistan", 33.6844, 73.0479, "GMT+5"),
        CityLocation("Makkah", "Saudi Arabia", 21.3891, 39.8579, "GMT+3"),
        CityLocation("Madinah", "Saudi Arabia", 24.5247, 39.5692, "GMT+3"),
        CityLocation("Riyadh", "Saudi Arabia", 24.7136, 46.6753, "GMT+3"),
        CityLocation("Dubai", "UAE", 25.2048, 55.2708, "GMT+4"),
        CityLocation("Istanbul", "Turkey", 41.0082, 28.9784, "GMT+3"),
        CityLocation("Cairo", "Egypt", 30.0444, 31.2357, "GMT+2"),
        CityLocation("London", "United Kingdom", 51.5074, -0.1278, "GMT+1"),
        CityLocation("New York", "USA", 40.7128, -74.0060, "GMT-4"),
        CityLocation("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, "GMT+8")
    )

    private val _selectedCity = MutableStateFlow(cities[0]) // Lahore default
    val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationSettingsState())
    val notificationSettings: StateFlow<NotificationSettingsState> = _notificationSettings.asStateFlow()

    // Prayer Tracker state for today
    private val todayDateStr: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _prayerTracker = MutableStateFlow(
        PrayerTrackerRecord(
            date = todayDateStr,
            fajrDone = true,
            dhuhrDone = true,
            asrDone = false,
            maghribDone = false,
            ishaDone = false,
            totalDone = 2
        )
    )
    val prayerTracker: StateFlow<PrayerTrackerRecord> = _prayerTracker.asStateFlow()

    private val _prayerStreak = MutableStateFlow(12)
    val prayerStreak: StateFlow<Int> = _prayerStreak.asStateFlow()

    // Bookmarked items
    private val _bookmarkedDuas = MutableStateFlow<Set<String>>(setOf("dua_morning_1", "dua_travel_1"))
    val bookmarkedDuas: StateFlow<Set<String>> = _bookmarkedDuas.asStateFlow()

    private val _favoriteWallpapers = MutableStateFlow<Set<String>>(setOf("wall_1", "wall_3"))
    val favoriteWallpapers: StateFlow<Set<String>> = _favoriteWallpapers.asStateFlow()

    // Fasting tracker for Ramadan
    private val _completedFasts = MutableStateFlow(14)
    val completedFasts: StateFlow<Int> = _completedFasts.asStateFlow()

    fun setSelectedCity(city: CityLocation) {
        _selectedCity.value = city
    }

    fun updateNotificationSettings(settings: NotificationSettingsState) {
        _notificationSettings.value = settings
    }

    fun togglePrayerStatus(prayerName: String) {
        val current = _prayerTracker.value
        val updated = when (prayerName.lowercase()) {
            "fajr" -> current.copy(fajrDone = !current.fajrDone)
            "dhuhr" -> current.copy(dhuhrDone = !current.dhuhrDone)
            "asr" -> current.copy(asrDone = !current.asrDone)
            "maghrib" -> current.copy(maghribDone = !current.maghribDone)
            "isha" -> current.copy(ishaDone = !current.ishaDone)
            else -> current
        }

        var count = 0
        if (updated.fajrDone) count++
        if (updated.dhuhrDone) count++
        if (updated.asrDone) count++
        if (updated.maghribDone) count++
        if (updated.ishaDone) count++

        _prayerTracker.value = updated.copy(totalDone = count)
        if (count == 5) {
            _prayerStreak.value += 1
        }
    }

    fun toggleBookmarkDua(duaId: String) {
        val current = _bookmarkedDuas.value.toMutableSet()
        if (current.contains(duaId)) {
            current.remove(duaId)
        } else {
            current.add(duaId)
        }
        _bookmarkedDuas.value = current
    }

    fun toggleFavoriteWallpaper(wallpaperId: String) {
        val current = _favoriteWallpapers.value.toMutableSet()
        if (current.contains(wallpaperId)) {
            current.remove(wallpaperId)
        } else {
            current.add(wallpaperId)
        }
        _favoriteWallpapers.value = current
    }

    fun toggleRamadanFast(dayNum: Int) {
        _completedFasts.value = (_completedFasts.value + 1).coerceAtMost(30)
    }

    // Calculation Method State
    private val _selectedCalculationMethod = MutableStateFlow("University of Islamic Sciences, Karachi (18° / 18° / Hanafi)")
    val selectedCalculationMethod: StateFlow<String> = _selectedCalculationMethod.asStateFlow()

    fun setCalculationMethod(method: String) {
        _selectedCalculationMethod.value = method
    }

    // Get Today's calculated prayer times using University of Islamic Sciences Karachi / Astronomical calculation
    fun getPrayerTimes(city: CityLocation = _selectedCity.value): List<PrayerTimeDetail> {
        return calculatePrayerTimes(
            lat = city.latitude,
            lng = city.longitude,
            tzOffsetHours = parseTimezoneOffset(city.timezone),
            calendar = Calendar.getInstance(),
            fajrAngle = 18.0, // Karachi Standard 18.0°
            ishaAngle = 18.0, // Karachi Standard 18.0°
            asrShadowRatio = 2.0 // Hanafi standard (Asr shadow factor 2.0)
        )
    }

    private fun parseTimezoneOffset(tzStr: String): Double {
        return when {
            tzStr.contains("+5") -> 5.0
            tzStr.contains("+3") -> 3.0
            tzStr.contains("+4") -> 4.0
            tzStr.contains("+2") -> 2.0
            tzStr.contains("+1") -> 1.0
            tzStr.contains("-4") -> -4.0
            tzStr.contains("+8") -> 8.0
            else -> 5.0
        }
    }

    fun calculatePrayerTimes(
        lat: Double,
        lng: Double,
        tzOffsetHours: Double,
        calendar: Calendar = Calendar.getInstance(),
        fajrAngle: Double = 18.0,
        ishaAngle: Double = 18.0,
        asrShadowRatio: Double = 2.0
    ): List<PrayerTimeDetail> {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        
        val gamma = 2 * Math.PI / 365.0 * (dayOfYear - 1)
        val eqtime = 229.18 * (0.000075 + 0.001868 * Math.cos(gamma) - 0.032077 * Math.sin(gamma)
                - 0.014615 * Math.cos(2 * gamma) - 0.040849 * Math.sin(2 * gamma))
        
        val decl = 0.006918 - 0.399912 * Math.cos(gamma) + 0.070257 * Math.sin(gamma) -
                0.006758 * Math.cos(2 * gamma) + 0.000907 * Math.sin(2 * gamma) -
                0.002697 * Math.cos(3 * gamma) + 0.00148 * Math.sin(3 * gamma)

        val latRad = Math.toRadians(lat)

        val dhuhrHours = 12.0 + tzOffsetHours - (lng / 15.0) - (eqtime / 60.0)

        fun hourAngle(angle: Double): Double {
            val alphaRad = Math.toRadians(angle)
            val cosHA = (Math.sin(-alphaRad) - Math.sin(latRad) * Math.sin(decl)) / (Math.cos(latRad) * Math.cos(decl))
            val clamped = cosHA.coerceIn(-1.0, 1.0)
            return Math.toDegrees(Math.acos(clamped)) / 15.0
        }

        val haSunrise = hourAngle(0.8333)
        val sunriseHours = dhuhrHours - haSunrise
        val maghribHours = dhuhrHours + haSunrise

        val haFajr = hourAngle(fajrAngle)
        val fajrHours = dhuhrHours - haFajr

        val haIsha = hourAngle(ishaAngle)
        val ishaHours = dhuhrHours + haIsha

        val deltaLat = Math.abs(latRad - decl)
        val shadowNoon = Math.tan(deltaLat)
        val asrAngleRad = Math.atan(1.0 / (asrShadowRatio + shadowNoon))
        val cosHAAsr = (Math.sin(asrAngleRad) - Math.sin(latRad) * Math.sin(decl)) / (Math.cos(latRad) * Math.cos(decl))
        val haAsr = Math.toDegrees(Math.acos(cosHAAsr.coerceIn(-1.0, 1.0))) / 15.0
        val asrHours = dhuhrHours + haAsr

        val nowMs = System.currentTimeMillis()
        val calNow = Calendar.getInstance()
        val curHour = calNow.get(Calendar.HOUR_OF_DAY)
        val curMin = calNow.get(Calendar.MINUTE)
        val curDecimal = curHour + curMin / 60.0

        fun formatTime(decimalHours: Double): String {
            var hrs = decimalHours
            while (hrs < 0) hrs += 24.0
            while (hrs >= 24) hrs -= 24.0

            val h = hrs.toInt()
            val m = ((hrs - h) * 60).toInt()
            val ampm = if (h >= 12) "PM" else "AM"
            val h12 = when {
                h == 0 -> 12
                h > 12 -> h - 12
                else -> h
            }
            return String.format(Locale.US, "%02d:%02d %s", h12, m, ampm)
        }

        var nextFound = false
        val checkNext = { decTime: Double ->
            if (!nextFound && decTime > curDecimal) {
                nextFound = true
                true
            } else false
        }

        return listOf(
            PrayerTimeDetail("Fajr", formatTime(fajrHours), "الفجر", nowMs, checkNext(fajrHours), curDecimal > fajrHours, "wb_twilight"),
            PrayerTimeDetail("Sunrise", formatTime(sunriseHours), "الشروق", nowMs, false, curDecimal > sunriseHours, "wb_sunny"),
            PrayerTimeDetail("Dhuhr", formatTime(dhuhrHours), "الظهر", nowMs, checkNext(dhuhrHours), curDecimal > dhuhrHours, "light_mode"),
            PrayerTimeDetail("Asr", formatTime(asrHours), "العصر", nowMs, checkNext(asrHours), curDecimal > asrHours, "sunny_snowing"),
            PrayerTimeDetail("Maghrib", formatTime(maghribHours), "المغرب", nowMs, checkNext(maghribHours), curDecimal > maghribHours, "nights_stay"),
            PrayerTimeDetail("Isha", formatTime(ishaHours), "العشاء", nowMs, checkNext(ishaHours), curDecimal > ishaHours, "bedtime")
        )
    }

    fun getMonthlyPrayerSchedule(): List<MonthlyPrayerRow> {
        val rows = mutableListOf<MonthlyPrayerRow>()
        val cal = Calendar.getInstance()
        val city = _selectedCity.value

        for (i in 1..30) {
            cal.set(Calendar.DAY_OF_MONTH, i)
            val dayTimes = calculatePrayerTimes(
                lat = city.latitude,
                lng = city.longitude,
                tzOffsetHours = parseTimezoneOffset(city.timezone),
                calendar = cal,
                fajrAngle = 18.0,
                ishaAngle = 18.0,
                asrShadowRatio = 2.0
            )

            rows.add(
                MonthlyPrayerRow(
                    dayOfMonth = i,
                    dateString = "$i Safar 1448",
                    fajr = dayTimes.find { it.name == "Fajr" }?.time ?: "04:18 AM",
                    sunrise = dayTimes.find { it.name == "Sunrise" }?.time ?: "05:42 AM",
                    dhuhr = dayTimes.find { it.name == "Dhuhr" }?.time ?: "12:16 PM",
                    asr = dayTimes.find { it.name == "Asr" }?.time ?: "03:48 PM",
                    maghrib = dayTimes.find { it.name == "Maghrib" }?.time ?: "06:42 PM",
                    isha = dayTimes.find { it.name == "Isha" }?.time ?: "08:10 PM"
                )
            )
        }
        return rows
    }

    fun getDailyAyah(): DailyAyah {
        return DailyAyah(
            surahName = "Surah Al-Baqarah",
            verseNumber = 152,
            textArabic = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
            textTranslation = "So remember Me; I will remember you. And be grateful to Me and do not deny Me."
        )
    }

    fun getDailyAyahUrdu(): String {
        return "پس تم مجھے یاد رکھو، میں تمہیں یاد رکھوں گا اور میرا شکر ادا کرو اور میری ناشکری نہ کرو۔"
    }

    fun getDailyHadith(): DailyHadith {
        return DailyHadith(
            textArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            textTranslation = "Actions are judged by intentions, and every person will be rewarded according to what he intended.",
            reference = "Sahih al-Bukhari #1",
            narrator = "Narrated by Umar ibn al-Khattab (R.A)"
        )
    }

    fun getDailyHadithUrdu(): String {
        return "اعمال کا دارومدار نیتوں پر ہے اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔"
    }

    // List of Duas
    fun getDuas(): List<DuaItem> {
        return listOf(
            DuaItem(
                id = "dua_morning_1",
                title = "Morning Protection Dua",
                category = "Morning",
                arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ",
                transliteration = "Asbahna wa asbahal-mulku lillah, walhamdu lillah",
                urdu = "ہم نے صبح کی اور اللہ کے تمام ملکی و سلطنت نے صبح کی، اور تمام تعریفیں اللہ ہی کے لیے ہیں۔",
                english = "We have entered upon a new day and with it all the dominion belongs to Allah, praise be to Allah.",
                reference = "Sahih Muslim #2723"
            ),
            DuaItem(
                id = "dua_evening_1",
                title = "Evening Protection Dua",
                category = "Evening",
                arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ",
                transliteration = "Amsayna wa amsal-mulku lillah, walhamdu lillah",
                urdu = "ہم نے شام کی اور شام کے وقت سارا ملک اللہ ہی کے لیے ہے، اور سب تعریفیں اللہ کے لیے ہیں۔",
                english = "We have reached the evening and at this time the whole kingdom belongs to Allah, praise be to Allah.",
                reference = "Sunan Abi Dawud #5071"
            ),
            DuaItem(
                id = "dua_travel_1",
                title = "Dua for Travelling",
                category = "Travel",
                arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنْقَلِبُونَ",
                transliteration = "Subhanalladhi sakhkhara lana hadha wa ma kunna lahu muqrinin wa inna ila Rabbina lamunqalibun",
                urdu = "پاک ہے وہ ذات جس نے اس (سواری) کو ہمارے لیے مسخر کر دیا، ورنہ ہم اسے قابو میں لانے والے نہ تھے، اور ہم اپنے رب کی طرف لوٹنے والے ہیں۔",
                english = "Glory be to Him Who has subjected this to us, whereas we were unable to conquer it by ourselves. And surely, to our Lord we shall return.",
                reference = "Surah Az-Zukhruf 13-14"
            ),
            DuaItem(
                id = "dua_sleeping_1",
                title = "Dua Before Sleeping",
                category = "Sleeping",
                arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                transliteration = "Bismika Allahumma amutu wa ahya",
                urdu = "اے اللہ! تیرے ہی نام کے ساتھ میں مرتا (سوتا) ہوں اور جیتا (جاگتا) ہوں۔",
                english = "In Your Name, O Allah, I die and I live.",
                reference = "Sahih al-Bukhari #6312"
            ),
            DuaItem(
                id = "dua_waking_1",
                title = "Dua Upon Waking Up",
                category = "Waking",
                arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                transliteration = "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilaihin-nushur",
                urdu = "تمام تعریفیں اس اللہ کے لیے ہیں جس نے ہمیں مارنے (سلانے) کے بعد زندہ (بیدار) کیا اور اسی کی طرف لوٹ کر جانا ہے۔",
                english = "Praise is to Allah Who gave us life after having taken it from us and unto Him is the resurrection.",
                reference = "Sahih al-Bukhari #6314"
            ),
            DuaItem(
                id = "dua_eating_1",
                title = "Dua Before Eating",
                category = "Eating",
                arabic = "بِسْمِ اللَّهِ وَعَلَى بَرَكَةِ اللَّهِ",
                transliteration = "Bismillahi wa 'ala barakatillah",
                urdu = "اللہ کے نام کے ساتھ اور اللہ کی برکت پر (میں کھانا شروع کرتا ہوں)۔",
                english = "In the name of Allah and with the blessings of Allah.",
                reference = "Al-Hisn al-Hasin"
            ),
            DuaItem(
                id = "dua_salah_1",
                title = "Dua After Fard Prayer",
                category = "After Salah",
                arabic = "اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ",
                transliteration = "Allahumma Antas-Salam wa minkas-salam, tabarakta ya Dhal-Jalali wal-Ikram",
                urdu = "اے اللہ! تو سلامتی والا ہے اور تیری ہی طرف سے سلامتی ہے، تو بڑی برکت والا ہے، اے عظمت اور عزت والے!",
                english = "O Allah, You are As-Salam and from You is all peace, blessed are You, O Possessor of majesty and honor.",
                reference = "Sahih Muslim #591"
            ),
            DuaItem(
                id = "dua_rain_1",
                title = "Dua When It Rains",
                category = "Protection",
                arabic = "اللَّهُمَّ صَيِّبًا نَافِعًا",
                transliteration = "Allahumma sayyiban nafi'an",
                urdu = "اے اللہ! اسے نفع دینے والی بارش بنا دے۔",
                english = "O Allah, make it a beneficial rain.",
                reference = "Sahih al-Bukhari #1032"
            )
        )
    }

    // 99 Names of Allah
    fun getAllahNames(): List<AllahName> {
        val list = mutableListOf<AllahName>()
        val sampleNames = listOf(
            Triple("Ar-Rahman", "الرَّحْمَنُ", "The Most Gracious") to Pair("نہایت مہربان", "He who has plenty of mercy for the believer and the blasphemer in this worldly life and exclusively for the believer in the hereafter."),
            Triple("Ar-Rahim", "الرَّحِيمُ", "The Most Merciful") to Pair("نہایت رحم فرمانے والا", "He who has a plenty of mercy for the believers in the hereafter."),
            Triple("Al-Malik", "الْمَلِكُ", "The King / Sovereign") to Pair("حقیقی بادشاہ", "The Absolute Ruler and Sovereign Lord of all the creation."),
            Triple("Al-Quddus", "الْقُدُّوسُ", "The Most Holy") to Pair("ہر عیب سے پاک", "The One who is pure from any imperfection and clear from any fault."),
            Triple("As-Salam", "السَّلاَمُ", "The Source of Peace") to Pair("سلامتی دینے والا", "The Giver of peace who frees His servants from all danger and harm."),
            Triple("Al-Mu'min", "الْمُؤْمِنُ", "The Giver of Faith") to Pair("امان دینے والا", "The One who witnessed for Himself that no one is God except Him."),
            Triple("Al-Muhaymin", "الْمُهَيْمِنُ", "The Guardian / Protector") to Pair("نگہبان", "The One who witnesses the saying and deeds of His creatures."),
            Triple("Al-Aziz", "الْعَزِيزُ", "The Almighty / Honorable") to Pair("سب پر غالب", "The Strong, the Defeater who is not defeated."),
            Triple("Al-Jabbar", "الْجَبَّارُ", "The Compeller") to Pair("زبردست", "The One that nothing happens in His Dominion except that which He willed."),
            Triple("Al-Mutakabbir", "الْمُتَكَبِّرُ", "The Supreme / Majestic") to Pair("بڑائی والا", "The One who is clear from the attributes of the creatures and from resembling them.")
        )

        for (i in 1..99) {
            val sampleIndex = (i - 1) % sampleNames.size
            val nameInfo = sampleNames[sampleIndex]
            list.add(
                AllahName(
                    number = i,
                    arabic = nameInfo.first.second,
                    transliteration = nameInfo.first.first,
                    englishMeaning = nameInfo.first.third,
                    urduMeaning = nameInfo.second.first,
                    explanation = nameInfo.second.second
                )
            )
        }
        return list
    }

    // Islamic Events
    fun getIslamicEvents(): List<IslamicEvent> {
        return listOf(
            IslamicEvent(
                id = "ev_1",
                nameEnglish = "Ramadan Mubarak 1448",
                nameArabic = "شَهْرُ رَمَضَانَ المُبَارَك",
                hijriDate = "1 Ramadan 1448 AH",
                gregorianDate = "February 8, 2027",
                description = "The holy month of fasting, prayer, Quranic recitation, and spiritual purification."
            ),
            IslamicEvent(
                id = "ev_2",
                nameEnglish = "Eid-ul-Fitr",
                nameArabic = "عِيدُ الْفِطْرِ",
                hijriDate = "1 Shawwal 1448 AH",
                gregorianDate = "March 10, 2027",
                description = "Festive celebration marking the joyous conclusion of the holy month of Ramadan."
            ),
            IslamicEvent(
                id = "ev_3",
                nameEnglish = "Day of Arafah & Hajj",
                nameArabic = "يَوْمُ عَرَفَةَ",
                hijriDate = "9 Dhul Hijjah 1448 AH",
                gregorianDate = "May 16, 2027",
                description = "The climax of the annual Hajj pilgrimage where pilgrims gather on Mount Arafat."
            ),
            IslamicEvent(
                id = "ev_4",
                nameEnglish = "Eid-ul-Adha",
                nameArabic = "عِيدُ الأَضْحَى",
                hijriDate = "10 Dhul Hijjah 1448 AH",
                gregorianDate = "May 17, 2027",
                description = "Festival of Sacrifice honoring Prophet Ibrahim's willingness to sacrifice his son in obedience to Allah."
            ),
            IslamicEvent(
                id = "ev_5",
                nameEnglish = "Ashura (10th Muharram)",
                nameArabic = "يَوْمُ عَاشُورَاءَ",
                hijriDate = "10 Muharram 1448 AH",
                gregorianDate = "July 16, 2026",
                description = "Day Prophet Musa (A.S) was saved from Pharaoh, and the martyrdom of Imam Hussain (R.A)."
            ),
            IslamicEvent(
                id = "ev_6",
                nameEnglish = "12 Rabi-ul-Awwal (Mawlid)",
                nameArabic = "مَوْلِدُ النَّبِيِّ ﷺ",
                hijriDate = "12 Rabi-ul-Awwal 1448 AH",
                gregorianDate = "September 15, 2026",
                description = "Celebration of the blessed birth of the Prophet Muhammad ﷺ."
            )
        )
    }

    // Nearby Mosques
    fun getNearbyMosques(): List<NearbyMosque> {
        return listOf(
            NearbyMosque("m_1", "Jamia Masjid Markaz-e-Ahle Sunnat", "Main Boulevard, Gulberg III", 0.4, 4.9f, 31.5204, 74.3587),
            NearbyMosque("m_2", "Badshahi Masjid (Historical)", "Walled City, Lahore", 3.2, 5.0f, 31.5882, 74.3096),
            NearbyMosque("m_3", "Grand Jamia Masjid Bahria", "Bahria Town, Sector E", 8.5, 4.9f, 31.3685, 74.1852),
            NearbyMosque("m_4", "Masjid Wazir Khan", "Chowk Wazir Khan", 4.1, 4.8f, 31.5828, 74.3213),
            NearbyMosque("m_5", "Data Darbar Jamia Masjid", "Bhati Gate, Circular Road", 3.8, 4.9f, 31.5786, 74.3045)
        )
    }

    // Wallpapers
    fun getWallpapers(): List<IslamicWallpaper> {
        return listOf(
            IslamicWallpaper("wall_1", "Holy Kaaba in Golden Dusk", "Kaaba", "img_hero_banner"),
            IslamicWallpaper("wall_2", "Al-Masjid an-Nabawi Green Dome", "Mosque", "img_hero_banner"),
            IslamicWallpaper("wall_3", "Classical Arabic Calligraphy Quran", "Calligraphy", "img_hero_banner"),
            IslamicWallpaper("wall_4", "Golden Illumination Quran Verse", "Quran", "img_hero_banner"),
            IslamicWallpaper("wall_5", "Badshahi Mosque Sunset Arch", "Mosque", "img_hero_banner"),
            IslamicWallpaper("wall_6", "Bismillah Calligraphy Art", "Calligraphy", "img_hero_banner")
        )
    }

    // Ramadan 30 Days Schedule
    fun getRamadanSchedule(): List<RamadanDay> {
        val list = mutableListOf<RamadanDay>()
        for (i in 1..30) {
            val sehri = "04:${String.format("%02d", 25 - (i / 3))}"
            val iftar = "06:${String.format("%02d", 35 + (i / 3))}"
            list.add(
                RamadanDay(
                    dayNumber = i,
                    hijriDate = "$i Ramadan 1448 AH",
                    gregorianDate = "Feb ${7 + i}, 2027",
                    sehriTime = "$sehri AM",
                    iftarTime = "$iftar PM",
                    isFastCompleted = i <= _completedFasts.value
                )
            )
        }
        return list
    }
}
