package ru.spbpu.keyloggerAuthentication.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Базовый интерфейс для репозитория.
 *
 * @param <T> элемент репозитория.
 */
public interface BaseRepository<T, I> {
    UUID add(T dto);
}
