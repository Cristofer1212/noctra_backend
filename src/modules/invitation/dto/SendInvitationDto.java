package modules.invitation.dto;

public class SendInvitationDto {

    private Integer eventId;
    private String gender;
    private String phoneGuest;

    public SendInvitationDto(String gender, String phoneGuest) {
        this.gender = gender;
        this.phoneGuest = phoneGuest;
    }

    public  Integer getEventId() { return eventId; }

    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getGender() {
        return gender;
    }


    public String getPhoneGuest() {
        return phoneGuest;
    }



}
