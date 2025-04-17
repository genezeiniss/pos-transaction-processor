package com.genezeiniss.pos_transaction_processor.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class ValidatorUtils {

    private static final String COURIER = "courier";
    private static final String BANK = "bank";
    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String CHEQUE_NUMBER = "chequeNumber";
    private static final String LAST4 = "last4";

    private static final Pattern BANK_NAME_PATTERN = Pattern.compile("[a-zA-Z\\s]{3,40}");
    private static final Pattern LAST4_PATTERN = Pattern.compile("\\d{4}");
    private static final Pattern BASIC_ACCOUNT_PATTERN = Pattern.compile("\\d{6,17}");
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]{10,30}$");
    private static final Pattern CHEQUE_NUMBER_PATTERN = Pattern.compile("\\d{6,12}");

    private static final String INVALID_VALUE_MSG = "Invalid %s value";
    private static final String MISSING_FIELD_MSG = "Missing required field: %s";
    private static final String UNSUPPORTED_COURIER_MSG = "Courier %s does not accept this payment method";

    public static void validateBank(Map<String, String> additionalInfo, List<String> errors) {
        validateField(additionalInfo, BANK, BANK_NAME_PATTERN, errors);
    }

    public static void validateAccountNumber(Map<String, String> additionalInfo, List<String> errors) {
        getAdditionalInfoField(additionalInfo, ACCOUNT_NUMBER)
                .ifPresentOrElse(
                        value -> {
                            String normalized = normalizePaymentString(value);
                            if (!isValidAccountNumber(normalized)) {
                                errors.add(String.format(INVALID_VALUE_MSG, ACCOUNT_NUMBER));
                            }
                        },
                        () -> errors.add(String.format(MISSING_FIELD_MSG, ACCOUNT_NUMBER)));
    }

    public static void validateChequeNumber(Map<String, String> additionalInfo, List<String> errors) {
        getAdditionalInfoField(additionalInfo, CHEQUE_NUMBER)
                .ifPresentOrElse(
                        value -> {
                            String normalized = normalizePaymentString(value);
                            if (!CHEQUE_NUMBER_PATTERN.matcher(normalized).matches()) {
                                errors.add(String.format(INVALID_VALUE_MSG, CHEQUE_NUMBER));
                            }
                        },
                        () -> errors.add(String.format(MISSING_FIELD_MSG, CHEQUE_NUMBER)));
    }

    public static void validateCardLastDigits(Map<String, String> additionalInfo, List<String> errors) {
        validateField(additionalInfo, LAST4, LAST4_PATTERN, errors);
    }

    public static void validateCourier(Map<String, String> additionalInfo, List<String> allowedCouriers, List<String> errors) {
        getAdditionalInfoField(additionalInfo, COURIER)
                .ifPresentOrElse(
                        value -> {
                            if (!allowedCouriers.contains(value.toLowerCase())) {
                                errors.add(String.format(UNSUPPORTED_COURIER_MSG, value));
                            }
                        },
                        () -> errors.add(String.format(MISSING_FIELD_MSG, COURIER)));
    }

    private static void validateField(Map<String, String> additionalInfo, String fieldName, Pattern pattern, List<String> errors) {
        getAdditionalInfoField(additionalInfo, fieldName)
                .ifPresentOrElse(
                        value -> {
                            if (!pattern.matcher(value).matches()) {
                                errors.add(String.format(INVALID_VALUE_MSG, fieldName));
                            }
                        },
                        () -> errors.add(String.format(MISSING_FIELD_MSG, fieldName)));
    }

    private static boolean isValidAccountNumber(String normalizedValue) {
        return BASIC_ACCOUNT_PATTERN.matcher(normalizedValue).matches() ||
                IBAN_PATTERN.matcher(normalizedValue.toUpperCase()).matches();
    }

    private static String normalizePaymentString(String value) {
        return value.replaceAll("[\\s-]", "");
    }

    private static Optional<String> getAdditionalInfoField(Map<String, String> additionalInfo, String field) {
        return Optional.ofNullable(additionalInfo)
                .map(info -> info.get(field))
                .filter(Strings::isNotBlank);
    }
}