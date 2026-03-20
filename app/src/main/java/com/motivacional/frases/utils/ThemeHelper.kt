package com.motivacional.frases.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.motivacional.frases.R

object ThemeHelper {

    data class ThemeColors(
        val primary: Int,
        val primaryDark: Int,
        val accent: Int
    )

    fun getThemeColors(activity: Activity, themeName: String): ThemeColors {
        return when (themeName) {
            "purple" -> ThemeColors(
                ContextCompat.getColor(activity, R.color.purple_primary),
                ContextCompat.getColor(activity, R.color.purple_primary_dark),
                ContextCompat.getColor(activity, R.color.purple_accent)
            )
            "green" -> ThemeColors(
                ContextCompat.getColor(activity, R.color.green_primary),
                ContextCompat.getColor(activity, R.color.green_primary_dark),
                ContextCompat.getColor(activity, R.color.green_accent)
            )
            "orange" -> ThemeColors(
                ContextCompat.getColor(activity, R.color.orange_primary),
                ContextCompat.getColor(activity, R.color.orange_primary_dark),
                ContextCompat.getColor(activity, R.color.orange_accent)
            )
            "pink" -> ThemeColors(
                ContextCompat.getColor(activity, R.color.pink_primary),
                ContextCompat.getColor(activity, R.color.pink_primary_dark),
                ContextCompat.getColor(activity, R.color.pink_accent)
            )
            "red" -> ThemeColors(
                ContextCompat.getColor(activity, R.color.red_primary),
                ContextCompat.getColor(activity, R.color.red_primary_dark),
                ContextCompat.getColor(activity, R.color.red_accent)
            )
            else -> ThemeColors(
                ContextCompat.getColor(activity, R.color.primary),
                ContextCompat.getColor(activity, R.color.primary_dark),
                ContextCompat.getColor(activity, R.color.accent)
            )
        }
    }

    fun applyThemeColors(activity: Activity, themeName: String) {
        val colors = getThemeColors(activity, themeName)

        // Aplicar cor na status bar
        activity.window?.statusBarColor = colors.primary

        // Aplicar cor na toolbar
        activity.findViewById<MaterialToolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setBackgroundColor(colors.primary)
        }

        // Aplicar cor no card de frase do dia
        activity.findViewById<MaterialCardView>(R.id.cardQuoteOfDay)?.let { card ->
            card.setCardBackgroundColor(colors.primary)
        }

        // Aplicar cor nos chips selecionados
        activity.findViewById<ChipGroup>(R.id.chipGroup)?.let { chipGroup ->
            for (i in 0 until chipGroup.childCount) {
                (chipGroup.getChildAt(i) as? Chip)?.let { chip ->
                    chip.chipBackgroundColor = ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_checked),
                            intArrayOf()
                        ),
                        intArrayOf(
                            colors.primary,
                            ContextCompat.getColor(activity, R.color.surface_light)
                        )
                    )
                    chip.setTextColor(
                        ColorStateList(
                            arrayOf(
                                intArrayOf(android.R.attr.state_checked),
                                intArrayOf()
                            ),
                            intArrayOf(
                                Color.WHITE,
                                ContextCompat.getColor(activity, R.color.text_primary_light)
                            )
                        )
                    )
                }
            }
        }
    }

    fun applyThemeToSettings(activity: Activity, themeName: String) {
        val colors = getThemeColors(activity, themeName)

        // Aplicar cor na status bar
        activity.window?.statusBarColor = colors.primary

        // Aplicar cor na toolbar
        activity.findViewById<MaterialToolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setBackgroundColor(colors.primary)
        }
    }

    fun applyThemeToSearch(activity: Activity, themeName: String) {
        val colors = getThemeColors(activity, themeName)

        // Aplicar cor na status bar
        activity.window?.statusBarColor = colors.primary

        // Aplicar cor na toolbar
        activity.findViewById<MaterialToolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setBackgroundColor(colors.primary)
        }

        // Aplicar cor nos chips de filtro
        activity.findViewById<ChipGroup>(R.id.chipGroupFilter)?.let { chipGroup ->
            for (i in 0 until chipGroup.childCount) {
                (chipGroup.getChildAt(i) as? Chip)?.let { chip ->
                    chip.chipBackgroundColor = ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_checked),
                            intArrayOf()
                        ),
                        intArrayOf(
                            colors.primary,
                            ContextCompat.getColor(activity, R.color.surface_light)
                        )
                    )
                    chip.setTextColor(
                        ColorStateList(
                            arrayOf(
                                intArrayOf(android.R.attr.state_checked),
                                intArrayOf()
                            ),
                            intArrayOf(
                                Color.WHITE,
                                ContextCompat.getColor(activity, R.color.text_primary_light)
                            )
                        )
                    )
                }
            }
        }
    }
}
