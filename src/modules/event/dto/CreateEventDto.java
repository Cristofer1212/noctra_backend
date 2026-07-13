package modules.event.dto;

import java.time.LocalDateTime;

public class CreateEventDto {

  private Integer userId;
  private String name;
  private String address;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer capacity;
  private String state;

  public int getUserId() {
    return userId;
  }
  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public String getName() {
    return name;

  }

  public String getAddress() {
    return address;

  }

  public LocalDateTime getStartDate() {
    return startDate;

  }

  public LocalDateTime getEndDate() {
    return endDate;

  }

  public int getCapacity() {
    return capacity;

  }

  public String getState() {
    return state;
  }



}
