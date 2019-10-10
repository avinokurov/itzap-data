package com.itzap.data.file.utils;

import com.itzap.common.exception.IZapErrorCodes;
import com.itzap.common.exception.IZapException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UnitUtils {
    private static final Pattern SIZE_PATTER = Pattern.compile("([\\d.,]+)\\s*(\\w)");

    private UnitUtils() {
    }

    public static Long parseFileSize(String in) {
        in = in.trim();
        in = in.replaceAll(",", ".");
        try {
            return Long.parseLong(in);
        } catch (NumberFormatException e) {
        }

        final Matcher m = SIZE_PATTER.matcher(StringUtils.upperCase(in));
        if (m.find()) {
            int scale = 1;
            switch (m.group(2).charAt(0)) {
                case 'G':
                    scale *= 1024;
                case 'M':
                    scale *= 1024;
                case 'K':
                    scale *= 1024;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return Math.round(Double.parseDouble(m.group(1)) * scale);
        }

        throw new IZapException(String.format("Unknown size format [%s]", in),
                IZapErrorCodes.APPLICATION_ERROR);
    }
}
