package com.idetidev.usercase.model.response

import kotlin.reflect.jvm.internal.impl.protobuf.UninitializedMessageException

data class CustomError (val message: String,
val messageException: String)