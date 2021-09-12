package com.hse_project.hse_slaves.model

data class Notification(
    var notificationId: Long,
    var notificationReceiverId: Long,
    var notificationProducerId: Long,
    var notificationType: String
)
