package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.Phone;
import com.pedro.clientesapi.entity.PhoneType;

public record PhoneResponse(
        Integer id,
        Integer personId,
        String number,
        PhoneType type
) {
    public static PhoneResponse from(Phone phone) {
        return new PhoneResponse(phone.getId(), phone.getPerson().getId(), phone.getNumber(), phone.getType());
    }
}
