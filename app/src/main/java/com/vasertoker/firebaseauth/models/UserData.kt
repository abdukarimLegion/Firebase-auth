package com.vasertoker.firebaseauth.models

 class UserData{
    var email: String?= null
    var metadata: String?= null
    var phoneNumber: String?= null
    var tenantId: String?= null
    var photoUrl: String?= null
    var displayName: String?= null
    var uid: String?= null

     constructor(
         email: String?,
         metadata: String?,
         phoneNumber: String?,
         tenantId: String?,
         photoUrl: String?,
         displayName: String?,
         uid: String?
     ) {
         this.email = email
         this.metadata = metadata
         this.phoneNumber = phoneNumber
         this.tenantId = tenantId
         this.photoUrl = photoUrl
         this.displayName = displayName
         this.uid = uid
     }

     constructor()
 }
