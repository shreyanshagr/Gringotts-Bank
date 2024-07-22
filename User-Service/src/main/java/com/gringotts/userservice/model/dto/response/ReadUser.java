package com.gringotts.userservice.model.dto.response;

import com.gringotts.userservice.payload.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadUser {

    private String firstName;

    private String lastName;

    private String emailId;

    private String contactNumber;

    private Status status;
}
