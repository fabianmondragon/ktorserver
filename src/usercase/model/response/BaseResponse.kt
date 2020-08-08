package com.idetidev.usercase.model.response

import io.ktor.http.HttpStatusCode

class BaseResponse<T> {

    private var response: T? = null
    var codeResponse: HttpStatusCode
    var customCode: Int


    constructor(response: T?,
                codeResponse: HttpStatusCode,
                customCode: Int
    ) {
        this.response = response
        this.customCode = customCode
        this.codeResponse = codeResponse

    }

    constructor(codeResponse: HttpStatusCode, customCode: Int) {
        this.customCode = customCode
        this.codeResponse = codeResponse
    }
}