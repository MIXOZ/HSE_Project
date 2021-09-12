package com.hse_project.hse_slaves.model

data class Invitation(
    var creatorId: Long,
    var eventId: Long,
    var organizerId: Long,
    var message: String,
    var accepted: Boolean
)