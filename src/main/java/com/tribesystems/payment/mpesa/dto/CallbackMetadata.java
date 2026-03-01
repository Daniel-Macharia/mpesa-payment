package com.tribesystems.payment.mpesa.dto;

import java.util.List;

public record CallbackMetadata(
        List<CallbackMetadataItem> item
) {
}
