package me.taling.live.core.domain;

import com.amazonaws.services.chime.model.Attendee;
import com.amazonaws.services.chime.model.Meeting;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Chime {
    private Meeting meeting;
    private Attendee attendee;
}
