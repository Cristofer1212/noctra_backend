package modules.scan.model;

import java.time.LocalDateTime;

public class Scan {
    private Integer id;
    private Integer invitationId;
    private Integer scannedBy;
    private String movementType;
    private String result;
    private LocalDateTime scanAt;

    public Scan(Integer invitationId, Integer scannedBy, String movementType, String result) {
        this.invitationId = invitationId;
        this.scannedBy = scannedBy;
        this.movementType = movementType;
        this.result = result;
    }

    // Getters
    public Integer getInvitationId() { return invitationId; }
    public Integer getScannedBy() { return scannedBy; }
    public String getMovementType() { return movementType; }
    public String getResult() { return result; }
}