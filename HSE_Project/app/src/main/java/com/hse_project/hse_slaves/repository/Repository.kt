package com.hse_project.hse_slaves.repository

import com.hse_project.hse_slaves.api.RetrofitInstance
import com.hse_project.hse_slaves.model.*
import retrofit2.Response
import java.sql.Timestamp

class Repository {

    suspend fun getUser(HeaderMap: Map<String, String>, id: Int): Response<User> {
        return RetrofitInstance.api.getUser(HeaderMap, id)
    }

    suspend fun getEvent(HeaderMap: Map<String, String>, id: Int): Response<Event> {
        return RetrofitInstance.api.getEvent(HeaderMap, id)
    }

    suspend fun getImage(
        HeaderMap: Map<String, String>,
        id: Int,
        entity: String
    ): Response<List<String>> {
        return RetrofitInstance.api.getImage(HeaderMap, id, entity)
    }

    suspend fun postEvent(HeaderMap: Map<String, String>, event: EventPost): Response<Void> {
        return RetrofitInstance.api.postEvent(HeaderMap, event)
    }

    suspend fun changeUser(
        HeaderMap: Map<String, String>,
        userId: Int,
        user: UserRegistration
    ): Response<Void> {
        return RetrofitInstance.api.changeUser(HeaderMap, userId, user)
    }

    //Security

    suspend fun getToken(username: String, password: String): Response<Int> {
        return RetrofitInstance.api.getToken(mapOf("username" to username, "password" to password))
    }

    suspend fun register(event: UserRegistration): Response<Void> {
        return RetrofitInstance.api.register(event)
    }

    //Likes

    suspend fun postLike(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int
    ): Response<Void> {
        return RetrofitInstance.api.postLike(HeaderMap, userId, eventId)
    }

    suspend fun deleteLike(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int
    ): Response<Void> {
        return RetrofitInstance.api.deleteLike(HeaderMap, userId, eventId)
    }

    suspend fun checkLike(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int
    ): Response<Boolean> {
        return RetrofitInstance.api.checkLike(HeaderMap, userId, eventId)
    }

    //Subscriptions

    suspend fun postSubscription(
        HeaderMap: Map<String, String>,
        userId: Int,
        subscriptionId: Int
    ): Response<Void> {
        return RetrofitInstance.api.postSubscription(
            HeaderMap,
            mapOf("userId" to userId, "subscriptionId" to subscriptionId)
        )
    }

    suspend fun deleteSubscription(
        HeaderMap: Map<String, String>,
        userId: Int,
        subscriptionId: Int
    ): Response<Void> {
        return RetrofitInstance.api.deleteSubscription(
            HeaderMap,
            mapOf("userId" to userId, "subscriptionId" to subscriptionId)
        )
    }

    suspend fun getAllSubscriptions(
        HeaderMap: Map<String, String>,
        userId: Int,
        subscriptionId: Int
    ): Response<List<User>> {
        return RetrofitInstance.api.getAllSubscriptions(
            HeaderMap,
            mapOf("userId" to userId, "subscriptionId" to subscriptionId)
        )
    }

    //Feed

    suspend fun getEvents(
        HeaderMap: Map<String, String>,
        offset: Int,
        size: Int,
        specializations: Set<String>
    ): Response<List<Event>> {
        return RetrofitInstance.api.getEvents(
            HeaderMap,
            mapOf("offset" to offset, "size" to size),
            specializations
        )
    }

    suspend fun getEventsRecommendation(
        HeaderMap: Map<String, String>,
        userId: Int,
        offset: Int,
        size: Int,
        specializations: Set<String>
    ): Response<List<Event>> {
        return RetrofitInstance.api.getEventsRecommendation(
            HeaderMap,
            userId,
            mapOf("offset" to offset, "size" to size),
            specializations
        )
    }

    suspend fun getCreators(
        HeaderMap: Map<String, String>,
        offset: Int,
        size: Int,
        specializations: Set<String>
    ): Response<List<User>> {
        return RetrofitInstance.api.getCreators(
            HeaderMap,
            mapOf("offset" to offset, "size" to size),
            specializations
        )
    }

    //Application

    suspend fun sendApplicationToEvent(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int,
        message: String
    ): Response<Void> {
        return RetrofitInstance.api.sendApplicationToEvent(HeaderMap, userId, eventId, message)
    }

    //Organizers

    suspend fun getFutureEventsOfOrganizer(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getFutureEventsOfOrganizer(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    suspend fun getPassedEventsOfOrganizer(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getPassedEventsOfOrganizer(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    suspend fun inviteCreatorToEvent(
        HeaderMap: Map<String, String>,
        organizerId: Int,
        creatorId: Int,
        eventId: Int,
        message: String
    ): Response<Void> {
        return RetrofitInstance.api.inviteCreatorToEvent(
            HeaderMap,
            organizerId,
            creatorId,
            eventId,
            message
        )
    }

    suspend fun checkIfCreatorHasInvitationToEvent(
        HeaderMap: Map<String, String>,
        creatorId: Int,
        eventId: Int
    ): Response<Boolean> {
        return RetrofitInstance.api.checkIfCreatorHasInvitationToEvent(
            HeaderMap,
            creatorId,
            eventId
        )
    }

    suspend fun checkIfCreatorHasApplicationFromEvent(
        HeaderMap: Map<String, String>,
        creatorId: Int,
        eventId: Int
    ): Response<Boolean> {
        return RetrofitInstance.api.checkIfCreatorHasApplicationFromEvent(
            HeaderMap,
            creatorId,
            eventId
        )
    }

    //Users

    suspend fun getFutureEventsOfUser(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getFutureEventsOfUser(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    suspend fun getPassedEventsOfUser(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getPassedEventsOfUser(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    //Creators

    suspend fun getFutureEventsOfCreator(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getFutureEventsOfCreator(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    suspend fun getPassedEventsOfCreator(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getPassedEventsOfCreator(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    suspend fun getInviteEventsOfCreator(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.getInviteEventsOfCreator(
            HeaderMap,
            userId,
            Timestamp(System.currentTimeMillis())
        )
    }

    //Reply applications

    suspend fun answerInvitationToEvent(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int,
        acceptance: Boolean
    ): Response<Void> {
        return RetrofitInstance.api.answerInvitationToEvent(HeaderMap, userId, eventId, acceptance)
    }

    suspend fun answerApplicationFromCreator(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int,
        creatorId: Int,
        acceptance: Boolean
    ): Response<Void> {
        return RetrofitInstance.api.answerApplicationFromCreator(
            HeaderMap,
            userId,
            eventId,
            creatorId,
            acceptance
        )
    }

    //Status of application/invitation

    suspend fun getApplication(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int
    ): Response<Application> {
        return RetrofitInstance.api.getApplication(HeaderMap, userId, eventId)
    }

    suspend fun getInvitation(
        HeaderMap: Map<String, String>,
        userId: Int,
        eventId: Int
    ): Response<Invitation> {
        return RetrofitInstance.api.getInvitation(HeaderMap, userId, eventId)
    }

    suspend fun getAllCreatorsApplications(
        HeaderMap: Map<String, String>,
        eventId: Int
    ): Response<List<Application>> {
        return RetrofitInstance.api.getAllCreatorsApplications(HeaderMap, eventId)
    }

    //Search

    suspend fun searchUsers(
        HeaderMap: Map<String, String>,
        name: String,
        offset: Int,
        size: Int
    ): Response<List<User>> {
        return RetrofitInstance.api.searchUsers(
            HeaderMap,
            name,
            mapOf("offset" to offset, "size" to size)
        )
    }

    suspend fun searchEvents(
        HeaderMap: Map<String, String>,
        name: String,
        offset: Int,
        size: Int
    ): Response<List<Event>> {
        return RetrofitInstance.api.searchEvents(
            HeaderMap,
            name,
            mapOf("offset" to offset, "size" to size)
        )
    }

    //Notifications

    suspend fun getNotifications(
        HeaderMap: Map<String, String>,
        userId: Int
    ): Response<List<Notification>> {
        return RetrofitInstance.api.getNotifications(HeaderMap, userId)
    }

    suspend fun deleteNotifications(
        HeaderMap: Map<String, String>,
        userId: Int,
        ids: List<Long>
    ): Response<Void> {
        return RetrofitInstance.api.deleteNotifications(HeaderMap, userId, ids)
    }
}