package com.tuppersoft.skizo.kotlin.core.customtypealias

import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure

typealias OnFailure = ((Failure) -> Unit)
typealias OnProgress = (suspend (Int) -> Unit)
typealias OnClickItem <T> = (T) -> Unit
