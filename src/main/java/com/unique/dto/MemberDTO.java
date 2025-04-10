package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.*;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long memberSeq;
    private String userid;
    private String username;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
}
