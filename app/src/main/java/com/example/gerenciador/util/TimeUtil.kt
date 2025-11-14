package com.example.gerenciador.util

object TimeUtil {

    /**
     * Formata milissegundos para HH:MM:SS
     */
    fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))

        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%02d:%02d", minutes, seconds)
        }
    }

    /**
     * Formata milissegundos para descrição legível
     * Ex: "2h 30min", "45min", "10s"
     */
    fun formatTimeReadable(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))

        return buildString {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}min ")
            if (hours == 0L && minutes == 0L && seconds > 0) append("${seconds}s")
            if (isEmpty()) append("0s")
        }.trim()
    }
}