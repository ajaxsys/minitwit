package com.minitwit.util;

import java.util.Optional;

public class StringUtil {

    @FunctionalInterface
    interface IWhenInt {
        void apply(int integer);
    }
    @FunctionalInterface
    interface IWhenNotInt {
        void apply();
    }
    public static void isInt(
            String str,
            IWhenInt doWhenInt,
            IWhenNotInt doWhenNotInt) {

        if (isInteger(str, 10)) {
            doWhenInt.apply(Integer.parseInt(str));
        } else {
            doWhenNotInt.apply();
        }
    }

    public static boolean isInt(String str) {
        return isInteger(str, 10);
    }

    private static boolean isInteger(String s, int radix) {
        if(s==null || s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public interface IVoid {
        void apply();
    }
    public static
    void
    allEmpty(
        IVoid doVoid,
        Optional<?>... optionals) {

        for (Optional<?> optional : optionals) {
            if (optional.isPresent())
                return;
        }
        doVoid.apply();
    }
    public static
    void
    allPresent(
        IVoid doVoid,
        Optional<?>... optionals) {

        for (Optional<?> optional : optionals) {
            if (!optional.isPresent())
                return;
        }
        doVoid.apply();
    }
    public static
    void
    anyEmpty(
        IVoid doVoid,
        Optional<?>... optionals) {

        for (Optional<?> optional : optionals) {
            if (!optional.isPresent())
                doVoid.apply();
                return;
        }
    }
    public static
    void
    anyPresent(
        IVoid doVoid,
        Optional<?>... optionals) {

        for (Optional<?> optional : optionals) {
            if (optional.isPresent())
                doVoid.apply();
                return;
        }
    }

    public static
    <T extends Comparable<? super T>>
    boolean
    eq(T a, T b) {

        return a.compareTo(b) == 0;
    }

    public static
    boolean
    isBlank(
        String value) {

        return value == null || eq("", value.trim());
    }
}
