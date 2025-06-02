package com.rcl.kduopass.presentation.screens.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcon: ImageVector
    get() {
        if (_AppIcon != null) {
            return _AppIcon!!
        }
        _AppIcon = ImageVector.Builder(
            name = "Icon",
            defaultWidth = 100.dp,
            defaultHeight = 100.dp,
            viewportWidth = 100f,
            viewportHeight = 100f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF2196F3)),
                strokeLineWidth = 4f
            ) {
                moveTo(50f, 50f)
                moveToRelative(-40f, 0f)
                arcToRelative(40f, 40f, 0f, isMoreThanHalf = true, isPositiveArc = true, 80f, 0f)
                arcToRelative(40f, 40f, 0f, isMoreThanHalf = true, isPositiveArc = true, -80f, 0f)
            }
            path(
                stroke = SolidColor(Color(0xFF2196F3)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(50f, 10f)
                lineTo(50f, 15f)
            }
            path(
                stroke = SolidColor(Color(0xFF2196F3)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(10f, 50f)
                lineTo(15f, 50f)
            }
            path(
                stroke = SolidColor(Color(0xFF2196F3)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(50f, 90f)
                lineTo(50f, 85f)
            }
            path(
                stroke = SolidColor(Color(0xFF2196F3)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(90f, 50f)
                lineTo(85f, 50f)
            }
            path(
                stroke = SolidColor(Color(0xFF0D47A1)),
                strokeLineWidth = 6f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(50f, 50f)
                lineTo(30f, 50f)
            }
            path(
                stroke = SolidColor(Color(0xFF0D47A1)),
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(50f, 50f)
                lineTo(50f, 20f)
            }
            path(
                fill = SolidColor(Color(0xFF4CAF50)),
                stroke = SolidColor(Color(0xFF2E7D32)),
                strokeLineWidth = 1f
            ) {
                moveTo(35f, 65f)
                quadTo(50f, 55f, 65f, 65f)
                quadTo(65f, 80f, 50f, 90f)
                quadTo(35f, 80f, 35f, 65f)
                close()
            }
        }.build()

        return _AppIcon!!
    }

@Suppress("ObjectPropertyName")
private var _AppIcon: ImageVector? = null
