package com.example.bloodbank.utils

import com.example.bloodbank.model.Request

interface BloodRequestInterface {

    fun onAcceptPressed(request: Request)
    fun onDeclinePressed(request: Request )

}