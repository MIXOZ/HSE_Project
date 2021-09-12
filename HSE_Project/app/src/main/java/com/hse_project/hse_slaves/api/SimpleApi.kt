package com.hse_project.hse_slaves.api

import com.hse_project.hse_slaves.model.*
import retrofit2.Response
import retrofit2.http.*
import java.sql.Timestamp

interface SimpleApi {

    //Events
    @GET("/api/events/{id}")
    suspend fun getEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<Event>

    @POST("/api/events")
    suspend fun postEvent(
        @HeaderMap headers: Map<String, String>,
        @Body event: EventPost
    ): Response<Void>

    //Feed

    //Image
    @GET("/api/images/{id}")
    suspend fun getImage(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int,
        @Query("entity") entity: String
    ): Response<List<String>>

    //User

    @GET("/api/users/{id}")
    suspend fun getUser(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<User>

    @PUT("/api/users/{userId}")
    suspend fun changeUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Body event: UserRegistration
    ): Response<Void>

    //Security

    @POST("/api/registration")
    suspend fun register(
        @Body event: UserRegistration
    ): Response<Void>

    @POST("/api/authentication")
    suspend fun getToken(
        @QueryMap filters: Map<String, String>
    ): Response<Int>

    //Likes

    @POST("/api/users/{userId}/likes/{eventId}")
    suspend fun postLike(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Path("eventId") eventId: Int
    ): Response<Void>

    @DELETE("/api/users/{userId}/likes/{eventId}")
    suspend fun deleteLike(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Path("eventId") eventId: Int
    ): Response<Void>

    @GET("/api/users/{userId}/likes/{eventId}")
    suspend fun checkLike(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Path("eventId") eventId: Int
    ): Response<Boolean>

    //Subscriptions
    @POST("/api/subscriptions")
    suspend fun postSubscription(
        @HeaderMap headers: Map<String, String>,
        @QueryMap filters: Map<String, Int>
    ): Response<Void>

    @DELETE("/api/subscriptions")
    suspend fun deleteSubscription(
        @HeaderMap headers: Map<String, String>,
        @QueryMap filters: Map<String, Int>
    ): Response<Void>

    @GET("/api/subscriptions")
    suspend fun getAllSubscriptions(
        @HeaderMap headers: Map<String, String>,
        @QueryMap filters: Map<String, Int>
    ): Response<List<User>>

    //Feed
    @GET("/api/feed/events")
    suspend fun getEvents(
        @HeaderMap headers: Map<String, String>,
        @QueryMap filters: Map<String, Int>,
        @Query("specializations") specializations: Set<String>
    ): Response<List<Event>>

    @GET("/api/feed/{userId}/events/recommendations")
    suspend fun getEventsRecommendation(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @QueryMap filters: Map<String, Int>,
        @Query("specializations") specializations: Set<String>
    ): Response<List<Event>>

    @GET("/api/feed/creators")
    suspend fun getCreators(
        @HeaderMap headers: Map<String, String>,
        @QueryMap filters: Map<String, Int>,
        @Query("specializations") specializations: Set<String>
    ): Response<List<User>>

    //Application

    @POST("/api/creators/{creatorId}/applications/{eventId}")
    suspend fun sendApplicationToEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") userId: Int,
        @Path("eventId") eventId: Int,
        @Body message: String
    ): Response<Void>

    //Organizers

    @GET("/api/organizers/{organizerId}/futureEvents/{time}")
    suspend fun getFutureEventsOfOrganizer(
        @HeaderMap headers: Map<String, String>,
        @Path("organizerId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    @GET("/api/organizers/{organizerId}/passedEvents/{time}")
    suspend fun getPassedEventsOfOrganizer(
        @HeaderMap headers: Map<String, String>,
        @Path("organizerId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    @POST("/api/organizers/{organizerId}/invites/{creatorId}")
    suspend fun inviteCreatorToEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("organizerId") organizerId: Int,
        @Path("creatorId") creatorId: Int,
        @Query("eventId") eventId: Int,
        @Body message: String
    ): Response<Void>

    @GET("/api/creators/{creatorId}/invitations/{eventId}/check")
    suspend fun checkIfCreatorHasInvitationToEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") creatorId: Int,
        @Path("eventId") eventId: Int
    ): Response<Boolean>

    @GET("/api/creators/{creatorId}/applications/{eventId}/check")
    suspend fun checkIfCreatorHasApplicationFromEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") creatorId: Int,
        @Path("eventId") eventId: Int
    ): Response<Boolean>

    //Users

    @GET("/api/users/{userId}/participations/future/{time}")
    suspend fun getFutureEventsOfUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    @GET("/api/users/{userId}/participations/passed/{time}")
    suspend fun getPassedEventsOfUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>


    //Creators

    @GET("/api/creators/{creatorId}/applications/futureEvents/{time}")
    suspend fun getFutureEventsOfCreator(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    @GET("/api/creators/{creatorId}/applications/passedEvents/{time}")
    suspend fun getPassedEventsOfCreator(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    @GET("api/creators/{creatorId}/invitations/futureEvents/{time}")
    suspend fun getInviteEventsOfCreator(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") userId: Int,
        @Path("time") time: Timestamp
    ): Response<List<Event>>

    //Reply application
    @POST("api/creators/{creatorId}/invites/{eventId}")
    suspend fun answerInvitationToEvent(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") creatorId: Int,
        @Path("eventId") eventId: Int,
        @Query("acceptance") acceptance: Boolean
    ): Response<Void>

    @POST("api/organizers/{organizerId}/events/{eventId}/applications/{creatorId}")
    suspend fun answerApplicationFromCreator(
        @HeaderMap headers: Map<String, String>,
        @Path("organizerId") organizerId: Int,
        @Path("eventId") eventId: Int,
        @Path("creatorId") creatorId: Int,
        @Query("acceptance") acceptance: Boolean
    ): Response<Void>

    //Status of application/invitation

    @GET("api/creators/{creatorId}/applications/{eventId}")
    suspend fun getApplication(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") creatorId: Int,
        @Path("eventId") eventId: Int
    ): Response<Application>


    @GET("api/creators/{creatorId}/invitations/{eventId}")
    suspend fun getInvitation(
        @HeaderMap headers: Map<String, String>,
        @Path("creatorId") creatorId: Int,
        @Path("eventId") eventId: Int
    ): Response<Invitation>

    @GET("api/events/{eventId}/applications")
    suspend fun getAllCreatorsApplications(
        @HeaderMap headers: Map<String, String>,
        @Path("eventId") eventId: Int
    ): Response<List<Application>>

    //Search

    @GET("api/users/search")
    suspend fun searchUsers(
        @HeaderMap headers: Map<String, String>,
        @Query("username") name: String,
        @QueryMap filters: Map<String, Int>
    ): Response<List<User>>

    @GET("api/events/search")
    suspend fun searchEvents(
        @HeaderMap headers: Map<String, String>,
        @Query("eventName") name: String,
        @QueryMap filters: Map<String, Int>
    ): Response<List<Event>>

    //Notifications

    @GET("api/users/{userId}/notifications")
    suspend fun getNotifications(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") eventId: Int
    ) :Response<List<Notification>>

    @DELETE("api/users/{userId}/notifications")
    suspend fun deleteNotifications(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") eventId: Int,
        @Body ids : List<Long>
    ) : Response<Void>
}