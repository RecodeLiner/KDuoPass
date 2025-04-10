package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.utils.TOTPUtils
import me.tatarka.inject.annotations.Inject

@Inject
class GenerateCodeUseCase {
    operator fun invoke(secret: String): String {
        return TOTPUtils.generateTOTP(secret)
    }
}