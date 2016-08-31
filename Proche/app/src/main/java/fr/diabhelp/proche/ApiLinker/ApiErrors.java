package fr.diabhelp.proche.ApiLinker;

/**
 * Created by Sumbers on 26/07/2016.
 */
public enum ApiErrors {

    NETWORK_ERROR("network error"),
    SERVER_ERROR("server error"),
    NO_USERS_FOUND("Users not found");

    private String serverMessage;

    ApiErrors(final String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public static ApiErrors getFromMessage(String serverMessage)
    {
        for (ApiErrors a : ApiErrors.values())
        {
            if (serverMessage.contains(a.getServerMessage()))
                return (a);
        }
        return (SERVER_ERROR);
//        throw new IllegalArgumentException("The serveur message doesn't contains any enum message : " + serverMessage);
    }

    public String getServerMessage() {
        return this.serverMessage;
    }
}
