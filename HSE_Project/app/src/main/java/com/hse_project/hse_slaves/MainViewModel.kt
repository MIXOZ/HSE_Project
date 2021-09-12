package com.hse_project.hse_slaves

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse_project.hse_slaves.current.USER_ID
import com.hse_project.hse_slaves.current.USER_TOKEN
import com.hse_project.hse_slaves.model.*
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val tokenResponse: MutableLiveData<retrofit2.Response<Int>> = MutableLiveData()
    val postEventResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val registerResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val changeUserResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val eventResponse: MutableLiveData<retrofit2.Response<Event>> = MutableLiveData()
    val userResponse: MutableLiveData<retrofit2.Response<User>> = MutableLiveData()
    val imageResponse: MutableLiveData<retrofit2.Response<List<String>>> = MutableLiveData()

    val postLikeResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val deleteLikeResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val checkLikeResponse: MutableLiveData<retrofit2.Response<Boolean>> = MutableLiveData()

    val postSubscriptionResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val deleteSubscriptionResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()
    val getAllSubscriptionsResponse: MutableLiveData<retrofit2.Response<List<User>>> =
        MutableLiveData()

    var getEventsResponse: MutableLiveData<retrofit2.Response<List<Event>>> = MutableLiveData()
    val getEventsRecommendationResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()
    var getCreatorsResponse: MutableLiveData<retrofit2.Response<List<User>>> = MutableLiveData()

    val sendApplicationToEventResponse: MutableLiveData<retrofit2.Response<Void>> =
        MutableLiveData()

    val getFutureEventsOfOrganizerResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()
    val getPassedEventsOfOrganizerResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()

    val getFutureEventsOfUserResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()
    val getPassedEventsOfUserResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()

    val getFutureEventsOfCreatorResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()
    val getPassedEventsOfCreatorResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()
    val getInviteEventsOfCreatorResponse: MutableLiveData<retrofit2.Response<List<Event>>> =
        MutableLiveData()

    val inviteCreatorToEventResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()

    val checkIfCreatorHasInvitationToEventResponse: MutableLiveData<retrofit2.Response<Boolean>> =
        MutableLiveData()

    val checkIfCreatorHasApplicationFromEventResponse: MutableLiveData<retrofit2.Response<Boolean>> =
        MutableLiveData()

    val answerApplicationFromCreatorResponse: MutableLiveData<retrofit2.Response<Void>> =
        MutableLiveData()
    val answerInvitationToEventResponse: MutableLiveData<retrofit2.Response<Void>> =
        MutableLiveData()

    val getApplicationResponse: MutableLiveData<retrofit2.Response<Application>> = MutableLiveData()
    val getInvitationResponse: MutableLiveData<retrofit2.Response<Invitation>> = MutableLiveData()

    val getAllCreatorsApplicationsResponse: MutableLiveData<retrofit2.Response<List<Application>>> =
        MutableLiveData()

    var searchUsersResponse: MutableLiveData<retrofit2.Response<List<User>>> = MutableLiveData()
    var searchEventsResponse: MutableLiveData<retrofit2.Response<List<Event>>> = MutableLiveData()

    val getNotificationsResponse: MutableLiveData<retrofit2.Response<List<Notification>>> =
        MutableLiveData()
    val deleteNotificationsResponse: MutableLiveData<retrofit2.Response<Void>> = MutableLiveData()

    var userId: Int = USER_ID


    fun setNewUserId(id: Int) {
        this.userId = id
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        assert(USER_TOKEN != "")
        headerMap["Authorization"] = USER_TOKEN;
        return headerMap
    }

    fun getUser(id: Int) {
        viewModelScope.launch {
            val response = repository.getUser(getHeaderMap(), id)
            userResponse.value = response
        }
    }

    fun getMyUser() {
        viewModelScope.launch {
            val response = repository.getUser(getHeaderMap(), userId)
            userResponse.value = response
        }
    }


    fun getEvent(id: Int) {
        viewModelScope.launch {
            val response = repository.getEvent(getHeaderMap(), id)
            eventResponse.value = response
        }
    }

    fun getImage(id: Int, entity: String) {
        viewModelScope.launch {
            val response = repository.getImage(getHeaderMap(), id, entity)
            imageResponse.value = response
        }
    }

    fun postEvent(event: EventPost) {
        viewModelScope.launch {
            val response = repository.postEvent(getHeaderMap(), event)
            postEventResponse.value = response
        }
    }

    fun changeUser(user: UserRegistration) {
        viewModelScope.launch {
            val response = repository.changeUser(getHeaderMap(), userId, user)
            changeUserResponse.value = response
        }
    }

    //Security

    fun getToken(username: String, password: String) {
        viewModelScope.launch {
            val response = repository.getToken(username, password)
            tokenResponse.value = response
        }
    }

    fun register(event: UserRegistration) {
        viewModelScope.launch {
            val response = repository.register(event)
            registerResponse.value = response
        }
    }

    //Likes

    fun postLike(eventId: Int) {
        viewModelScope.launch {
            val response = repository.postLike(getHeaderMap(), userId, eventId)
            postLikeResponse.value = response
        }
    }


    fun deleteLike(eventId: Int) {
        viewModelScope.launch {
            val response = repository.deleteLike(getHeaderMap(), userId, eventId)
            deleteLikeResponse.value = response
        }
    }

    fun checkLike(eventId: Int) {
        viewModelScope.launch {
            val response = repository.checkLike(getHeaderMap(), userId, eventId)
            checkLikeResponse.value = response
        }
    }

    //Subscriptions

    fun postSubscription(subscriptionId: Int) {
        viewModelScope.launch {
            val response = repository.postSubscription(getHeaderMap(), userId, subscriptionId)
            postSubscriptionResponse.value = response
        }
    }


    fun deleteSubscription(subscriptionId: Int) {
        viewModelScope.launch {
            val response = repository.deleteSubscription(getHeaderMap(), userId, subscriptionId)
            deleteSubscriptionResponse.value = response
        }
    }

    fun getAllSubscriptions(subscriptionId: Int) {
        viewModelScope.launch {
            val response = repository.getAllSubscriptions(getHeaderMap(), userId, subscriptionId)
            getAllSubscriptionsResponse.value = response
        }
    }

    //Feed

    fun getEvents(offset: Int, size: Int, specializations: Set<String>) {
        viewModelScope.launch {
            val response = repository.getEvents(getHeaderMap(), offset, size, specializations)
            getEventsResponse.value = response
        }
    }

    fun getEventsRecommendation(offset: Int, size: Int, specializations: Set<String>) {
        viewModelScope.launch {
            val response = repository.getEventsRecommendation(
                getHeaderMap(),
                userId,
                offset,
                size,
                specializations
            )
            getEventsRecommendationResponse.value = response
        }
    }

    fun getCreators(offset: Int, size: Int, specializations: Set<String>) {
        viewModelScope.launch {
            val response = repository.getCreators(getHeaderMap(), offset, size, specializations)
            getCreatorsResponse.value = response
        }
    }

    //Application

    fun sendApplicationToEvent(eventId: Int, message: String) {
        viewModelScope.launch {
            val response =
                repository.sendApplicationToEvent(getHeaderMap(), userId, eventId, message)
            sendApplicationToEventResponse.value = response
        }
    }

    //Organizers

    fun getFutureEventsOfOrganizer() {
        viewModelScope.launch {
            val response = repository.getFutureEventsOfOrganizer(getHeaderMap(), userId)
            getFutureEventsOfOrganizerResponse.value = response
        }
    }

    fun getPassedEventsOfOrganizer() {
        viewModelScope.launch {
            val response = repository.getPassedEventsOfOrganizer(getHeaderMap(), userId)
            getPassedEventsOfOrganizerResponse.value = response
        }
    }

    fun inviteCreatorToEvent(creatorId: Int, eventId: Int, message: String) {
        viewModelScope.launch {
            val response =
                repository.inviteCreatorToEvent(getHeaderMap(), userId, creatorId, eventId, message)
            inviteCreatorToEventResponse.value = response
        }
    }

    fun checkIfCreatorHasInvitationToEvent(eventId: Int) {
        viewModelScope.launch {
            val response =
                repository.checkIfCreatorHasInvitationToEvent(getHeaderMap(), userId, eventId)
            checkIfCreatorHasInvitationToEventResponse.value = response
        }
    }

    fun checkIfCreatorHasApplicationFromEvent(eventId: Int) {
        viewModelScope.launch {
            val response =
                repository.checkIfCreatorHasApplicationFromEvent(getHeaderMap(), userId, eventId)
            checkIfCreatorHasApplicationFromEventResponse.value = response
        }
    }

    //Users

    fun getFutureEventsOfUser() {
        viewModelScope.launch {
            val response = repository.getFutureEventsOfOrganizer(getHeaderMap(), userId)
            getFutureEventsOfUserResponse.value = response
        }
    }

    fun getPassedEventsOfUser() {
        viewModelScope.launch {
            val response = repository.getPassedEventsOfOrganizer(getHeaderMap(), userId)
            getPassedEventsOfUserResponse.value = response
        }
    }

    //Creators

    fun getFutureEventsOfCreator() {
        viewModelScope.launch {
            val response = repository.getFutureEventsOfOrganizer(getHeaderMap(), userId)
            getFutureEventsOfCreatorResponse.value = response
        }
    }

    fun getPassedEventsOfCreator() {
        viewModelScope.launch {
            val response = repository.getPassedEventsOfOrganizer(getHeaderMap(), userId)
            getPassedEventsOfCreatorResponse.value = response
        }
    }

    fun getInviteEventsOfCreator() {
        viewModelScope.launch {
            val response = repository.getInviteEventsOfCreator(getHeaderMap(), userId)
            getInviteEventsOfCreatorResponse.value = response
        }
    }

    //Answers to applications

    fun answerInvitationToEvent(eventId: Int, acceptance: Boolean) {
        viewModelScope.launch {
            val response =
                repository.answerInvitationToEvent(getHeaderMap(), userId, eventId, acceptance)
            answerInvitationToEventResponse.value = response
        }
    }

    fun answerApplicationFromCreator(eventId: Int, creatorId: Int, acceptance: Boolean) {
        viewModelScope.launch {
            val response = repository.answerApplicationFromCreator(
                getHeaderMap(),
                userId,
                eventId,
                creatorId,
                acceptance
            )
            answerApplicationFromCreatorResponse.value = response
        }
    }

    //Status of application/invitation

    fun getApplication(eventId: Int) {
        viewModelScope.launch {
            val response = repository.getApplication(getHeaderMap(), userId, eventId)
            getApplicationResponse.value = response
        }
    }

    fun getInvitation(eventId: Int) {
        viewModelScope.launch {
            val response = repository.getInvitation(getHeaderMap(), userId, eventId)
            getInvitationResponse.value = response
        }
    }

    fun getAllCreatorsApplications(eventId: Int) {
        viewModelScope.launch {
            val response = repository.getAllCreatorsApplications(getHeaderMap(), eventId)
            getAllCreatorsApplicationsResponse.value = response
        }
    }

    //Search
    fun searchUsers(name: String, offset: Int, size: Int) {
        viewModelScope.launch {
            val response = repository.searchUsers(getHeaderMap(), name, offset, size)
            searchUsersResponse.value = response
        }
    }

    fun searchEvents(name: String, offset: Int, size: Int) {
        viewModelScope.launch {
            val response = repository.searchEvents(getHeaderMap(), name, offset, size)
            searchEventsResponse.value = response
        }
    }

    //Notifications
    fun getNotifications() {
        viewModelScope.launch {
            val response = repository.getNotifications(getHeaderMap(), userId)
            getNotificationsResponse.value = response
        }
    }

    fun deleteNotifications(ids: List<Long>) {
        viewModelScope.launch {
            val response = repository.deleteNotifications(getHeaderMap(), userId, ids)
            deleteNotificationsResponse.value = response
        }
    }
}