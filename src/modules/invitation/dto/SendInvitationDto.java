package modules.invitation.dto;

public class SendInvitationDto {


    private String gender;
    private String phoneGuest;

    public SendInvitationDto(String gender, String phoneGuest) {
        this.gender = gender;
        this.phoneGuest = phoneGuest;
    }

    public String getGender() {
        return gender;
    }


    public String getPhoneGuest() {
        return phoneGuest;
    }



}
