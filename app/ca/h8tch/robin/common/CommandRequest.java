package ca.h8tch.robin.common;

@FunctionalInterface
public interface CommandRequest<T> {
    T getDto();
}
