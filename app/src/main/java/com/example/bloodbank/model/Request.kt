package com.example.bloodbank.model

data class Request(
 val patientName: String,
 val hospitalName: String,
 val hospitalAddress: String,
 val bloodType: String,
 val requiredUnits: String,
 val hyperLink : String,
 val status : String,
 val id : String,
 var referenceId : String,
 var declineCount : String
) {
    constructor() : this("","","", "", "", "", "" ,"", "","") {}
}