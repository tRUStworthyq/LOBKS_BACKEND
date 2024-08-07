package com.project.lobks.entity;

import com.project.lobks.entity.enums.StatusBook;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {

    @EmbeddedId
    private UserBookEmbeddable userBookEmbeddable;

    @Enumerated(EnumType.STRING)
    private StatusBook statusBook;

}
