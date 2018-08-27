package dto;

/**
 * The Class ChatRoomDto.
 */
public class ChatRoomDto {

    /** The action. */
    public static String roomName;

    /**
     * @return the roomName
     */
    public static String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName the roomName to set
     */
    public void setRoomName(String roomName) {
        ChatRoomDto.roomName = roomName;
    }
}
