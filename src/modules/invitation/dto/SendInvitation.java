package modules.invitation.dto;

public class SendInvitation {


    private String gender;
    private String phoneGuest;

    public SendInvitation(String gender, String phoneGuest) {
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
