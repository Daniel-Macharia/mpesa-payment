package com.tribesystems.payment.mpesa.dto;

import java.util.List;

public record ResultParameters(
        List<KeyValuePair> ResultParameter
) {
}
