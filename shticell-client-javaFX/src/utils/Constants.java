package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CellDto;
import dto.deserializer.CellDtoDeserializer;
import dto.serializer.CellDtoSerializer;

public class Constants {
    // GSON instance
    public final static Gson GSON_INSTANCE = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(CellDto.class,new CellDtoSerializer())
            .registerTypeAdapter(CellDto.class,new CellDtoDeserializer())
            .create();

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/shticell";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE_URL = FULL_SERVER_PATH + "/sheet";



    public final static String UPDATE_CELL_URL = UPLOAD_FILE_URL + "/cell";
    public final static String RANGE_URL = UPLOAD_FILE_URL + "/range";
    public final static String GET_VERSION_URL = UPDATE_CELL_URL + "/sheet";



//    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
//    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
//    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
//    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

}
