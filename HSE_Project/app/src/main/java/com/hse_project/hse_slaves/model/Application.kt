package com.hse_project.hse_slaves.model

data class Application(
    var eventId: Long,
    var creatorId: Long,
    var message: String,
    var accepted: Boolean
)