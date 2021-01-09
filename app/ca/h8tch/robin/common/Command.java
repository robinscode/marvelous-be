package ca.h8tch.robin.common;

@FunctionalInterface
public interface Command<I, O> {
    O execute(I request) throws CommandExecutionException;
}
