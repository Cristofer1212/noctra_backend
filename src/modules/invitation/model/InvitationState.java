package modules.invitation.model;

public enum InvitationState {
    SIN_USAR,
    EN_USO,
    CANCELADO;

    // helper
    public static InvitationState fromString(String state) {
        try {
            return InvitationState.valueOf(state);
        } catch (Exception e) {
            return SIN_USAR; // Default
        }
    }
}
