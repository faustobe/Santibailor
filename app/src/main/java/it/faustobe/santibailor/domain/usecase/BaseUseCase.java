package it.faustobe.santibailor.domain.usecase;

public interface BaseUseCase<I, O> {
    O execute(I input);
}
