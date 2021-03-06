package com.vailsys.freeclimb.api.conference.participant;

import com.google.gson.JsonSyntaxException;
import com.vailsys.freeclimb.api.APIAccountRequester;
import com.vailsys.freeclimb.api.FreeClimbException;
import com.vailsys.freeclimb.api.FreeClimbJSONException;

import java.util.HashMap;

import static com.vailsys.freeclimb.json.FreeClimbGson.gson;

/**
 * This class represents the set of wrappers around the FreeClimb Participants
 * API. It provides methods to handle all the operations supported by the
 * FreeClimb Participants API.
 */
public class ParticipantsRequester extends APIAccountRequester {
    private static final String pathHead = "Participants";
    /** The default path for the Participants endpoint. */
    private final String path;
    /** The accountId for the acting account. */
    private final String actingAccountId;

    /**
     * Creates a ParticipantsRequester. For most SDK users ParticipantsRequesters
     * will be created automatically by the
     * {@link com.vailsys.freeclimb.api.FreeClimbClient} but is available for more
     * advanced users who only require the features in this specific requester and
     * not the rest of the features of the
     * {@link com.vailsys.freeclimb.api.FreeClimbClient}.
     *
     * @param credAccountId   The accountId to use as authentication credentials in
     *                        the HTTP Basic Auth header for requests made by this
     *                        requester.
     * @param credAuthToken   The authToken to use as authentication credentials in
     *                        the HTTP Basic Auth header for requests made by this
     *                        requester.
     * @param actingAccountId The accountId to act as. This can be the same as the
     *                        {@code credAccountId} or the accountId of a subaccount
     *                        of the {@code credAccountId}.
     * @param conferencePath  The path for the conference endpoint this
     *                        ParticipantsRequester is under.
     */
    public ParticipantsRequester(String credAccountId, String credAuthToken, String actingAccountId,
            String conferencePath) {
        super(credAccountId, credAuthToken);
        this.actingAccountId = actingAccountId;
        this.path = APIAccountRequester.constructPath(conferencePath, pathHead);
    }

    /**
     * Retrieve the {@code actingAccountId}.
     *
     * @return The {@code actingAccountId}
     */
    public String getActingAccountId() {
        return this.actingAccountId;
    }

    /**
     * Retrieve the {@code path} value generated by the ParticipantsRequester. This
     * is the URL path used in requests to FreeClimb.
     *
     * @return The {@code path}
     */
    public String getPath() {
        return this.path;
    }

    private String getParticipantPath(String participantId) {
        return APIAccountRequester.constructPath(this.getPath(), participantId);
    }

    /**
     * Allows developers using the SDK to change which instance of the FreeClimb API
     * that the ParticipantsRequester points to.
     *
     * @param newUrl The new URL to use in place of the default
     *               APIRequester.FREECLIMB_URL
     */
    public void setFreeClimbUrl(String newUrl) {
        super.setFreeClimbUrl(newUrl);
    }

    /**
     * Retrieve a list of participants associated with the conference. This wraps an
     * HTTP GET request to the FreeClimb API's
     * /Accounts/$accountId/Conferences/$conferenceId/Participants endpoint. This
     * will retrieve all participants for the conference.
     *
     * @return An in-language representation of FreeClimb's paginated list response.
     *         This will be a paginated list of participant instances as returned by
     *         the FreeClimb API.
     *
     * @see com.vailsys.freeclimb.api.conference.participant.Participant
     * @throws FreeClimbException when the request fails or the JSON is invalid.
     */
    public ParticipantList get() throws FreeClimbException {
        return new ParticipantList(this.getCredentialAccountId(), this.getCredentialAuthToken(),
                this.GET(this.getPath()));
    }

    /**
     * Retrieve a list of participants associated with the conference. This wraps an
     * HTTP GET request to the FreeClimb API's
     * /Accounts/$accountId/Conferences/$conferenceId/Participants endpoint. This
     * will retrieve all participants for the conference.
     *
     * @param filters An object containing a number of possible ways to filter the
     *                participants returned by FreeClimb.
     *
     * @return An in-language representation of FreeClimb's paginated list response.
     *         This will be a paginated list of participant instances as returned by
     *         the FreeClimb API.
     *
     * @see com.vailsys.freeclimb.api.conference.participant.ParticipantsSearchFilters
     * @throws FreeClimbException when the request fails or the JSON is invalid.
     */
    public ParticipantList get(ParticipantsSearchFilters filters) throws FreeClimbException {
        HashMap<String, String> filtersMap;

        try {
            filtersMap = gson.fromJson(gson.toJson(filters), APIAccountRequester.GETMapType);
        } catch (JsonSyntaxException jse) {
            throw new FreeClimbJSONException(jse);
        }

        return new ParticipantList(this.getCredentialAccountId(), this.getCredentialAuthToken(),
                this.GET(this.getPath(), filtersMap));
    }

    /**
     * Retrieve a single participant from FreeClimb.
     *
     * @param participantId The {@code callId} of the desired participant.
     *
     * @return The participant matching the {@code callId} provided.
     * @throws FreeClimbException when the request fails or the JSON is invalid.
     */
    public Participant get(String participantId) throws FreeClimbException {
        return Participant.fromJson(this.GET(this.getParticipantPath(participantId)));
    }

    /**
     * Update the existing participant associated with the {@code callId}. This
     * wraps an HTTP POST request to the FreeClimb API's
     * /Accounts/$accountId/Conferences/$conferenceId/Participants/$callId endpoint.
     *
     * @param participantId The {@code callId} of the desired participant.
     * @param options       The {@code ParticipantUpdateOptions} to change in the
     *                      target participant.
     * @see com.vailsys.freeclimb.api.conference.participant.ParticipantUpdateOptions
     *
     * @return The participant matching the {@code callId} provided.
     * @throws FreeClimbException when the request fails or the JSON is invalid.
     */
    public Participant update(String participantId, ParticipantUpdateOptions options) throws FreeClimbException {
        return Participant.fromJson(this.POST(this.getParticipantPath(participantId), gson.toJson(options)));
    }

    /**
     * Delete the participant associated with the {@code callId}. This wraps an HTTP
     * DELETE to the FreeClimb API's
     * /Accounts/$accountId/Conferences/$conferenceId/Participants/$callId endpoint.
     *
     * @param participantId The {@code callId} of the desired participant.
     * @throws FreeClimbException when the request fails.
     */
    public void remove(String participantId) throws FreeClimbException {
        this.DELETE(this.getParticipantPath(participantId));
    }
}
