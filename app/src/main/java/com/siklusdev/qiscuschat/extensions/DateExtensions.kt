package com.siklusdev.qiscuschat.extensions

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


private val dateFormat = ThreadLocal<SimpleDateFormat?>()
val indonesiaTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Jakarta")

fun String.formatDate(from: String, to: String): String {
    val sdf = dateFormat.get() ?: SimpleDateFormat(from, Locale("id")).apply {
        dateFormat.set(this)
    }
    with(sdf) {
        applyPattern(from)
        val date = try {
            parse(this@formatDate)
        } catch (error: ParseException) {
            return this@formatDate
        }
        applyPattern(to)
        return format(date)
    }
}

fun String.formatDate(): String {
    val indonesia = Locale("id", "ID", "ID")
    val inputFormatDate = SimpleDateFormat("yyyy-MM-d", indonesia)
    val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", indonesia)
    val date = inputFormatDate.parse(this)
    outputDateFormat.timeZone = indonesiaTimeZone
    return outputDateFormat.format(date)
}

fun String.formatDateLelang(): String {
    val indonesia = Locale("id", "ID", "ID")
    val inputFormatDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", indonesia)
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", indonesia)
    val date = inputFormatDate.parse(this)
    outputDateFormat.timeZone = indonesiaTimeZone
    return outputDateFormat.format(date)
}

fun String.formatTimeLelang(): String {
    val indonesia = Locale("id", "ID", "ID")
    val inputFormatDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", indonesia)
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm", indonesia)
    val date = inputFormatDate.parse(this)
    outputDateFormat.timeZone = indonesiaTimeZone
    return outputDateFormat.format(date)
}

fun String.formatTimeRiwayatLelang(): String {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val waktu: Date = format.parse(this)

    // set Event Date
    val currentDate = Calendar.getInstance()
    val eventDate = Calendar.getInstance()

    eventDate.time = waktu

    val diff =  currentDate.timeInMillis - eventDate.timeInMillis

    // Change the milliseconds to days, hours, minutes and seconds
    val days = diff / (24 * 60 * 60 * 1000)
    val hours = diff / (1000 * 60 * 60) % 24
    val minutes = diff / (1000 * 60) % 60
    val seconds = (diff / 1000) % 60

    val fullHours = days * 24
    val jam = fullHours + hours

    return "${jam}j ${minutes}m ${seconds}d"

}

fun String.eventLelangBerakhir() : String {

    val calendar = Calendar.getInstance()
    val currentDate = Calendar.getInstance()

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val eventBerakhir: Date = format.parse(this)
    calendar.time = eventBerakhir

    val diff = calendar.timeInMillis - currentDate.timeInMillis

    val days = diff / (24 * 60 * 60 * 1000)
    val hours = diff / (1000 * 60 * 60) % 24
    val minutes = diff / (1000 * 60) % 60
    val seconds = (diff / 1000) % 60

    val fullHours = days * 24
    val jam = fullHours + hours

//    return "${jam}j ${minutes}m ${seconds}d"
    return "Sisa waktu lelang $jam lagi"

}

fun String.convertTimeToTimeAgo(): String {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val waktu: Date = format.parse(this)

    // set Event Date
    val currentDate = Calendar.getInstance()
    val eventDate = Calendar.getInstance()

    eventDate.time = waktu

    val diff =  currentDate.timeInMillis - eventDate.timeInMillis

    // Change the milliseconds to days, hours, minutes and seconds
    val days = diff / (24 * 60 * 60 * 1000)
    val hours = diff / (1000 * 60 * 60) % 24
    val minutes = diff / (1000 * 60) % 60
    val seconds = (diff / 1000) % 60

    if (days > 0) {
        return "$days hari yang lalu"
    } else if (hours > 0) {
        return "$hours jam yang lalu"
    } else if (minutes > 0) {
        return "$minutes menit yang lalu"
    } else {
        return "$seconds detik yang lalu"
    }
}

fun Date?.getLastMessageTimestamp(): String? {
    return if (this != null) {
        val todayCalendar = Calendar.getInstance()
        val localCalendar = Calendar.getInstance()
        localCalendar.time = this
        when {
            todayCalendar.time.getDateStringFromDate()
                    == localCalendar.time.getDateStringFromDate() -> {
                this.getTimeStringFromDate()
            }
            todayCalendar[Calendar.DATE] - localCalendar[Calendar.DATE] == 1 -> {
                "Kemarin"
            }
            else -> {
                this.getDateStringFromDate()
            }
        }
    } else {
        null
    }
}

fun Date?.getTimeStringFromDate(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale("id"))
    return dateFormat.format(this!!)
}

fun Date?.getDateStringFromDate(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy",Locale("id"))
    return dateFormat.format(this!!)
}


//fun selisihDateTime(startTime: Date, endTime: Date): String? {
//    val selisihMS = Math.abs(startTime.time - endTime.time)
//    val selisihDetik = selisihMS / 1000 % 60
//    val selisihMenit = selisihMS / (60 * 1000) % 60
//    val selisihJam = selisihMS / (60 * 60 * 1000) % 24
//    val selisihHari = selisihMS / (24 * 60 * 60 * 1000)
//    return (selisihHari.toString() + " hari " + selisihJam + " Jam " + selisihMenit + " Menit " + selisihDetik + " Detik")
//}


//fun Date.formatDate(): String {
//    val indonesia = Locale("id", "ID", "ID")
//    val inputFormatDate = SimpleDateFormat("yyyy-MM-d", indonesia)
//    val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", indonesia)
//    val date = inputFormatDate.parse(this)
//    outputDateFormat.timeZone = indonesiaTimeZone
//    return outputDateFormat.format(date)
//}

fun String.toDateData(pattern: String = "yyyy-MM-dd'T'hh:mm:ss"): Date? {
    val sdf = dateFormat.get() ?: SimpleDateFormat(pattern, Locale("id")).apply {
        dateFormat.set(this)
    }
    sdf.applyPattern(pattern)
    return try {
        sdf.parse(this)
    } catch (error: ParseException) {
        return null
    }
}

fun String.toDate(pattern: String = "yyyy-MM-dd'T'hh:mm:ss"): Date? {

    val sdf = dateFormat.get() ?: SimpleDateFormat(pattern, Locale("id")).apply {
        dateFormat.set(this)
    }
    sdf.applyPattern(pattern)
    return try {
        sdf.parse(this)
    } catch (error: ParseException) {
        return null
    }
}

fun Date.formatText(pattern: String = "yyyy-MM-dd'T'hh:mm:ss"): String {
    val sdf = dateFormat.get() ?: SimpleDateFormat(pattern, Locale("id")).apply {
        dateFormat.set(this)
    }
    sdf.applyPattern(pattern)
    with(sdf) {
        return format(this@formatText)
    }
}

fun Date?.toFullDate(): String? {
    val fullDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale("id"))
    return fullDateFormat.format(this!!)
}

fun Date?.toTime(): String? {
    val fullDateFormat = SimpleDateFormat("hh:mm", Locale("id"))
    return fullDateFormat.format(this!!)
}
